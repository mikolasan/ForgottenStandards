package xyz.neupokoev.forgottenstandards.advanced

import android.content.Context
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import android.opengl.GLES20
import android.opengl.Matrix
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.getNormalizedColor
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

private lateinit var BACKGROUND_COLOR: FloatArray
private lateinit var BOLT_HEAD_COLOR: FloatArray
private lateinit var BOLT_HEAD_SELECTED_COLOR: FloatArray
private lateinit var BOLT_SHAFT_COLOR: FloatArray
private lateinit var BOLT_SHAFT_SELECTED_COLOR: FloatArray

// --- WORLD COORDINATE OFFSETS ---
// Moved closer to center as requested
private const val METRIC_X_OFFSET = -0.25f
private const val IMPERIAL_X_OFFSET = 0.25f

class BoltRenderer(private val context: Context, val refreshRate: Long, val dpi: Int) : Thread("BoltRendererThread"), GlRenderer {
    private lateinit var surfaceTexture: SurfaceTexture

    @Volatile
    override var isStopped: Boolean = false

    // GlRenderer properties
    @Volatile
    override var width: Int = 0
    @Volatile
    override var height: Int = 0
    @Volatile
    override var positionY: Float = 0.0f

    var labelUpdateListener: LabelUpdateListener? = null
    private var lastCenteredBoltName: String = ""

    private val metricToMm = mapOf(
        "M6" to 6f, "M7" to 7f, "M8" to 8f, "M10" to 10f, "M12" to 12f,
        "M14" to 14f, "M16" to 16f, "M18" to 18f, "M20" to 20f,
    )
    private val imperialToMm = mapOf(
        "1/4\"" to 6.35f, "5/16\"" to 7.94f, "3/8\"" to 9.52f, "7/16\"" to 11.11f,
        "1/2\"" to 12.70f, "9/16\"" to 14.29f, "5/8\"" to 15.87f, "3/4\"" to 19.05f,
    )

    private var mBoltPairs: List<BoltPair> = emptyList()

    init {
        val appContext = context.applicationContext
        BACKGROUND_COLOR = getNormalizedColor(appContext, R.color.background)
        BOLT_HEAD_COLOR = getNormalizedColor(appContext, R.color.bolt_head)
        BOLT_HEAD_SELECTED_COLOR = getNormalizedColor(appContext, R.color.bolt_head_selected)
        BOLT_SHAFT_COLOR = getNormalizedColor(appContext, R.color.bolt_shaft)
        BOLT_SHAFT_SELECTED_COLOR = getNormalizedColor(appContext, R.color.bolt_shaft_selected)
    }

    private fun initBolts() {
        if (height <= 0) return

        val metricBolts = metricToMm.map { (name, nominalMm) ->
            Bolt(name, nominalMm, createBoltFigures(nominalMm, BOLT_HEAD_COLOR, BOLT_SHAFT_COLOR, METRIC_X_OFFSET))
        }.sortedBy { it.nominalMm }

        val imperialBolts = imperialToMm.map { (name, nominalMm) ->
            Bolt(name, nominalMm, createBoltFigures(nominalMm, BOLT_HEAD_COLOR, BOLT_SHAFT_COLOR, IMPERIAL_X_OFFSET))
        }.sortedBy { it.nominalMm }

        mBoltPairs = metricBolts.mapNotNull { metricBolt ->
            imperialBolts.minByOrNull { imperialBolt ->
                abs(metricBolt.nominalMm - imperialBolt.nominalMm)
            }?.let { closestImperial ->
                BoltPair(metricBolt, closestImperial)
            }
        }.distinctBy { it.metric.name }

        var acc = 0.7f 
        mBoltPairs.forEach { pair ->
            val spacingRadius = pair.metric.figures.hex.radius
            pair.offset = acc
            pair.metric.figures.hex.offset = acc
            pair.metric.figures.body.offset = acc
            pair.imperial.figures.hex.offset = acc
            pair.imperial.figures.body.offset = acc
            acc -= 2.2f * spacingRadius
        }
    }

    private fun createBoltFigures(nominalMm: Float, headColor: FloatArray, shaftColor: FloatArray, xOffset: Float): BoltFigures {
        val nominalDiameterCm: Float = nominalMm / 10f
        val inches: Float = nominalDiameterCm / 2.54f
        val nominalPixelSize = dpi * inches

        val threadRadius = nominalPixelSize / height.toFloat()
        val hexRadius = (nominalPixelSize * 1.75f) / height.toFloat()

        return BoltFigures(
            hex = HexFigure(hexRadius, headColor).apply { this.xOffset = xOffset },
            body = CircleFigure(threadRadius, shaftColor).apply { this.xOffset = xOffset }
        )
    }

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    
    @Volatile
    override var angle: Float = 0f 
    @Volatile
    override var positionX: Float = 0f 

    override fun setSurface(surface: SurfaceTexture) { this.surfaceTexture = surface }
    override fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
        initBolts()
    }
    override fun startRendering() { this.start() }
    override fun stopRendering() { this.isStopped = true }
    override fun getThread(): Thread = this

    private fun getConfig(eglDisplay: EGLDisplay): EGLConfig {
        val renderableType = EGL14.EGL_OPENGL_ES2_BIT
        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8,
            EGL14.EGL_GREEN_SIZE, 8,
            EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, renderableType,
            EGL14.EGL_NONE
        )
        val configsCount = intArrayOf(0)
        val configs = arrayOfNulls<EGLConfig>(1)
        EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, configs.size, configsCount, 0)
        return configs[0] ?: throw RuntimeException("eglChooseConfig failed")
    }

    private fun findAndHighlightCenteredBolt() {
        val centeredY = -positionY
        val closestPair = mBoltPairs.minByOrNull { pair -> abs(pair.offset - centeredY) } ?: return
        val newCenteredName = closestPair.metric.name

        mBoltPairs.forEach { pair ->
            val isCentered = pair.metric.name == newCenteredName
            val color = if (isCentered) BOLT_HEAD_SELECTED_COLOR else BOLT_HEAD_COLOR
            pair.metric.figures.hex.color = color
            pair.imperial.figures.hex.color = color
        }
        if (newCenteredName != lastCenteredBoltName) {
            labelUpdateListener?.onCenteredBoltChanged(newCenteredName)
            lastCenteredBoltName = newCenteredName
        }
    }

    override fun run() {
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = intArrayOf(0, 0)
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1)
        val eglConfig = getConfig(eglDisplay)
        val eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE), 0)
        val eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, intArrayOf(EGL14.EGL_NONE), 0)

        if (!EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)) {
            return
        }

        while (!isStopped) {
            GLES20.glViewport(0, 0, width, height)
            val ratio: Float = if (height > 0) width.toFloat() / height.toFloat() else 1.0f
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 2f)

            GLES20.glClearColor(BACKGROUND_COLOR[0], BACKGROUND_COLOR[1], BACKGROUND_COLOR[2], BACKGROUND_COLOR[3])
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
            GLES20.glDisable(GLES20.GL_CULL_FACE)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            findAndHighlightCenteredBolt()

            val scratch = FloatArray(16)
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            Matrix.translateM(scratch, 0, positionX, positionY, 0f)

            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            mBoltPairs.forEach { pair ->
                pair.metric.figures.hex.draw(scratch) // head
                pair.metric.figures.body.draw(scratch) // shaft
                pair.imperial.figures.hex.draw(scratch) // head
                pair.imperial.figures.body.draw(scratch) // shaft
            }
            
            labelUpdateListener?.onAllBoltsUpdated(mBoltPairs)
            EGL14.eglSwapBuffers(eglDisplay, eglSurface)
            
            try {
                sleep(refreshRate)
            } catch (e: InterruptedException) {
                break
            }
        }

        EGL14.eglMakeCurrent(eglDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT)
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
        EGL14.eglTerminate(eglDisplay)
    }
}
