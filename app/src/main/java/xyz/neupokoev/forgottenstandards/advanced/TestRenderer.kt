package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import android.opengl.GLES20
import android.opengl.Matrix
import kotlin.math.abs

// --- DATA CLASSES for Bolt Representation ---
data class BoltFigures(val hex: HexFigure, val body: CircleFigure)
data class Bolt(val name: String, val nominalMm: Float, val figures: BoltFigures)
data class BoltPair(val metric: Bolt, val imperial: Bolt, var offset: Float = 0f)

// --- LISTENER INTERFACE ---
interface LabelUpdateListener {
    fun onAllBoltsUpdated(boltData: List<BoltPair>)
    fun onCenteredBoltChanged(boltName: String)
}

// --- THEME COLORS ---
private val THEME_BACKGROUND_COLOR = floatArrayOf(0.125f, 0.113f, 0.368f, 1.0f) // #321D5E
private val THEME_BOLT_COLOR = floatArrayOf(0.298f, 0.192f, 0.518f, 1.0f) // #4C3184
private val THEME_HIGHLIGHT_COLOR = floatArrayOf(0.98f, 0.98f, 0.98f, 1.0f) // #FAFAFA

// --- WORLD COORDINATE OFFSETS ---
private const val METRIC_X_OFFSET = -0.75f
private const val IMPERIAL_X_OFFSET = 0.75f

class TestRenderer(val refreshRate: Long, val dpi: Int) : Thread() {
    lateinit var surface: SurfaceTexture
    var isStopped = false

    var labelUpdateListener: LabelUpdateListener? = null
    private var lastCenteredBoltName: String = ""

    private val DEFAULT_BOLT_COLOR = THEME_BOLT_COLOR
    private val HIGHLIGHT_BOLT_COLOR = THEME_HIGHLIGHT_COLOR

    // Metric sizes from previous implementation, plus one that was missing
    private val metricToMm = mapOf(
        "M6" to 6f,
        "M7" to 7f,
        "M8" to 8f,
        "M10" to 10f,
        "M12" to 12f,
        "M14" to 14f,
        "M16" to 16f,
        "M18" to 18f,
        "M20" to 20f,
    )
    
    // Imperial sizes to pair with metric
    private val imperialToMm = mapOf(
        "1/4\"" to 6.35f,
        "5/16\"" to 7.94f,
        "3/8\"" to 9.52f,
        "7/16\"" to 11.11f,
        "1/2\"" to 12.70f,
        "9/16\"" to 14.29f,
        "5/8\"" to 15.87f,
        "3/4\"" to 19.05f,
    )

    private val mBoltPairs: List<BoltPair>

    init {
        // Create individual metric bolts
        val metricBolts = metricToMm.map { (name, nominalMm) ->
            Bolt(name, nominalMm, createBoltFigures(nominalMm, DEFAULT_BOLT_COLOR, METRIC_X_OFFSET))
        }.sortedBy { it.nominalMm }

        // Create individual imperial bolts
        val imperialBolts = imperialToMm.map { (name, nominalMm) ->
            Bolt(name, nominalMm, createBoltFigures(nominalMm, DEFAULT_BOLT_COLOR, IMPERIAL_X_OFFSET))
        }.sortedBy { it.nominalMm }

        // Group the closest comparable metric and imperial sizes together
        mBoltPairs = metricBolts.mapNotNull { metricBolt ->
            imperialBolts.minByOrNull { imperialBolt ->
                abs(metricBolt.nominalMm - imperialBolt.nominalMm)
            }?.let { closestImperial ->
                BoltPair(metricBolt, closestImperial)
            }
        }.distinctBy { it.metric.name }

        // Calculate vertical offsets for the pairs
        var acc = -1.0f // Start offset on the Y-axis (OpenGL coordinate space)
        mBoltPairs.forEach { pair ->
            // Use the largest component's radius for spacing (Metric hex is often largest)
            val spacingRadius = pair.metric.figures.hex.radius
            
            // Set offset for the pair (World Y)
            pair.offset = acc

            // Update bolt figure vertical offsets
            pair.metric.figures.hex.offset = acc
            pair.metric.figures.body.offset = acc
            
            pair.imperial.figures.hex.offset = acc
            pair.imperial.figures.body.offset = acc

            acc += 2.0f * spacingRadius // Move to the next bolt position
        }
    }

    private fun createBoltFigures(nominalMm: Float, color: FloatArray, xOffset: Float): BoltFigures {
        val nominalDiameterCm: Float = nominalMm / 10f
        val inches: Float = nominalDiameterCm / 2.54f
        val nominalPixelSize = dpi * inches // in pixels, proportional to nominal diameter (D)

        val hexHeadSize = nominalPixelSize * 1.5f
        val threadSize = nominalPixelSize

        return BoltFigures(
            hex = HexFigure(hexHeadSize, color).apply { this.xOffset = xOffset },
            body = CircleFigure(threadSize).apply { this.xOffset = xOffset }
        )
    }

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    @Volatile
    var angle: Float = 0f
    @Volatile
    var positionX: Float = 0f
    // Set initial positionY to 1.0f so the first bolt (at offset -1.0f) is centered in the view.
    @Volatile
    var positionY: Float = 1.0f
    @Volatile
    var width: Int = 0
    @Volatile
    var height: Int = 0


    fun getConfig(eglDisplay: EGLDisplay): EGLConfig {
        val renderableType = EGL14.EGL_OPENGL_ES2_BIT
        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, renderableType,
            EGL14.EGL_NONE, 0,
            EGL14.EGL_NONE
        )
        val flags = 0
        val configsCount = intArrayOf(0);
        val configs = arrayOfNulls<EGLConfig>(1);
        EGL14.eglChooseConfig(eglDisplay,
            attribList,
            0,
            configs,
            0,
            configs.size,
            configsCount,
            0)
        return configs[0]!!
    }

    private fun findAndHighlightCenteredBolt() {
        // The camera translation is positionY, so the world origin is at -positionY
        val centeredY = -positionY

        // Find the bolt pair whose offset is closest to the centeredY position
        val closestPair = mBoltPairs.minByOrNull { pair ->
            abs(pair.offset - centeredY)
        } ?: return

        // 1. Highlight in OpenGL: Highlight BOTH bolts in the closest pair
        val newCenteredName = closestPair.metric.name // Use metric name for change detection

        mBoltPairs.forEach { pair ->
            val isCentered = pair.metric.name == newCenteredName
            
            val color = if (isCentered) HIGHLIGHT_BOLT_COLOR else DEFAULT_BOLT_COLOR
            
            // Apply color to both metric and imperial hex heads
            pair.metric.figures.hex.color = color
            pair.imperial.figures.hex.color = color
        }

        // 2. Notify listener (UI thread)
        if (newCenteredName != lastCenteredBoltName) {
            // We notify the change using the metric name, but the Fragment can display both
            labelUpdateListener?.onCenteredBoltChanged(newCenteredName)
            lastCenteredBoltName = newCenteredName
        }
    }

    override fun run() {
        super.run()

        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = intArrayOf(0, 0)
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1)
        val eglConfig = getConfig(eglDisplay)
        val attribList = intArrayOf(
            EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
            EGL14.EGL_NONE)
        val eglContext = EGL14.eglCreateContext(
            eglDisplay,
            eglConfig,
            EGL14.EGL_NO_CONTEXT,
            attribList,
            0
        )
        val surfaceAttribs = intArrayOf(
            EGL14.EGL_NONE
        )
        val eglSurface = EGL14.eglCreateWindowSurface(
            eglDisplay,
            eglConfig,
            surface,
            surfaceAttribs,
            0)

        val rotationMatrix = FloatArray(16)

        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 2f)

        while (!isStopped && EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
            EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

            GLES20.glClearColor(THEME_BACKGROUND_COLOR[0], THEME_BACKGROUND_COLOR[1], THEME_BACKGROUND_COLOR[2], THEME_BACKGROUND_COLOR[3])
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
            GLES20.glDisable(GLES20.GL_CULL_FACE)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            findAndHighlightCenteredBolt()

            val scratch = FloatArray(16)

            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            Matrix.translateM(scratch, 0, positionX, positionY, 0f)

            Matrix.setLookAtM(viewMatrix,
                0,
                0f, 0f, 1f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)

            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            mBoltPairs.forEach { pair ->
                // Draw Metric Bolt
                pair.metric.figures.body.draw(scratch)
                pair.metric.figures.hex.draw(scratch)

                // Draw Imperial Bolt
                pair.imperial.figures.body.draw(scratch)
                pair.imperial.figures.hex.draw(scratch)
            }
            
            // Notify fragment of all bolt pair positions (Y offset)
            labelUpdateListener?.onAllBoltsUpdated(mBoltPairs)

            EGL14.eglSwapBuffers(eglDisplay, eglSurface)

            sleep(refreshRate)
        }

        surface.release()
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
    }
}
