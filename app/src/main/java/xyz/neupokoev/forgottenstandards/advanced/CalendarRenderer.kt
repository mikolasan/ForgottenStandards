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
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.sqrt

// Theme Colors
private lateinit var BACKGROUND_COLOR: FloatArray

// Season/Month Colors
private val WINTER_1 = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f)
private val WINTER_2 = floatArrayOf(0.8f, 0.8f, 0.9f, 1.0f)
private val WINTER_3 = floatArrayOf(0.7f, 0.7f, 0.8f, 1.0f)
private val SPRING_1 = floatArrayOf(0.5f, 0.9f, 0.5f, 1.0f)
private val SPRING_2 = floatArrayOf(0.2f, 0.8f, 0.2f, 1.0f)
private val SPRING_3 = floatArrayOf(0.0f, 0.6f, 0.0f, 1.0f)
private val AUTUMN_1 = floatArrayOf(1.0f, 0.9f, 0.0f, 1.0f)
private val AUTUMN_2 = floatArrayOf(1.0f, 0.5f, 0.0f, 1.0f)
private val AUTUMN_3 = floatArrayOf(0.8f, 0.1f, 0.0f, 1.0f)

private const val DAYS_IN_YEAR = 365
private const val DAY_ANGLE = 360f / DAYS_IN_YEAR.toFloat()

data class Month(
    val name: String,
    val days: Int,
    val color: FloatArray,
    var startAngle: Float = 0f,
    val sweepAngle: Float = days.toFloat() * DAY_ANGLE
)

interface CalendarLabelUpdateListener {
    fun onCalendarUpdated(months: List<Month>, globalAngle: Float)
}

class CalendarRenderer(private val context: Context, val refreshRate: Long, val dpi: Int) : Thread("CalendarRendererThread"), GlRenderer {
    private lateinit var surfaceTexture: SurfaceTexture

    @Volatile override var isStopped: Boolean = false
    @Volatile override var width: Int = 0
    @Volatile override var height: Int = 0
    @Volatile override var positionY: Float = 0f 
    @Volatile override var angle: Float = 0f 
    @Volatile override var positionX: Float = 0f 

    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)

    var labelUpdateListener: CalendarLabelUpdateListener? = null

    val months: List<Month> = listOf(
        Month("Vyugas", 40, WINTER_1), Month("Dzyamets", 41, WINTER_2), Month("Skrezhen", 40, WINTER_3),
        Month("Pronizh", 41, SPRING_1), Month("Veles", 40, SPRING_2), Month("Polesen", 41, SPRING_3),
        Month("Trest", 40, AUTUMN_1), Month("Listven", 41, AUTUMN_2), Month("Hmaryen", 41, AUTUMN_3)
    )

    init {

        val appContext = context.applicationContext
        BACKGROUND_COLOR = getNormalizedColor(appContext, R.color.background)

        // Start from 90 degrees (12 o'clock) and stack months CLOCKWISE
        var currentAngle = 90f
        months.forEach { month ->
            month.startAngle = normalizeAngle(currentAngle - month.sweepAngle)
            currentAngle -= month.sweepAngle
        }
    }

    private fun normalizeAngle(a: Float): Float {
        var angle = a % 360f
        if (angle < 0) angle += 360f
        return angle
    }

    private val sectorFigure = SectorFigure(radius = 0.9f)

    override fun setSurface(surface: SurfaceTexture) { this.surfaceTexture = surface }
    override fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }
    override fun startRendering() { this.start() }
    override fun stopRendering() { this.isStopped = true }
    override fun getThread(): Thread = this

    private fun getConfig(eglDisplay: EGLDisplay): EGLConfig {
        val renderableType = EGL14.EGL_OPENGL_ES2_BIT
        val attribList = intArrayOf(
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8, EGL14.EGL_ALPHA_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, renderableType, EGL14.EGL_NONE
        )
        val configsCount = intArrayOf(0)
        val configs = arrayOfNulls<EGLConfig>(1)
        EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, configs.size, configsCount, 0)
        return configs[0] ?: throw RuntimeException("eglChooseConfig failed")
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

        sectorFigure.prepare()

        while (!isStopped) {
            GLES20.glViewport(0, 0, width, height)
            val ratio: Float = if (height > 0) width.toFloat() / height.toFloat() else 1.0f
            Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 2f)

            GLES20.glClearColor(BACKGROUND_COLOR[0], BACKGROUND_COLOR[1], BACKGROUND_COLOR[2], BACKGROUND_COLOR[3])
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

            val scratch = FloatArray(16)
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            Matrix.translateM(scratch, 0, positionX, positionY, 0f)

            months.forEach { month ->
                sectorFigure.color = month.color
                sectorFigure.startAngleDeg = month.startAngle
                sectorFigure.sweepAngleDeg = month.sweepAngle
                sectorFigure.draw(scratch)
            }
            
            labelUpdateListener?.onCalendarUpdated(months, angle)
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

    fun resolveMonth(x: Float, y: Float): Month? {
        if (width <= 0 || height <= 0) return null
        val dx = x - width / 2f
        val dy = height / 2f - y 
        val dist = sqrt(dx * dx + dy * dy)
        if (dist > (0.9f / 2f) * height) return null
        
        var clickAngleDeg = atan2(dy, dx) * 180f / PI.toFloat()
        clickAngleDeg = normalizeAngle(clickAngleDeg)
        
        // Month space angle = Screen angle + global rotation angle
        val monthSpaceAngle = normalizeAngle(clickAngleDeg + angle)
        
        return months.find { month ->
            // Check if the angle is within sweepAngle distance from startAngle (CCW)
            var diff = monthSpaceAngle - month.startAngle
            if (diff < 0) diff += 360f
            diff < month.sweepAngle
        }
    }
}
