package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log

// Theme Colors (reused from BoltRenderer for consistency)
private val THEME_BACKGROUND_COLOR = floatArrayOf(0.125f, 0.113f, 0.368f, 1.0f) // #321D5E

// Season/Month Colors (Placeholders)
private val WINTER_COLOR = floatArrayOf(0.5f, 0.5f, 0.8f, 1.0f) // Light Blue/Gray
private val SPRING_COLOR = floatArrayOf(0.2f, 0.7f, 0.2f, 1.0f) // Green
private val AUTUMN_COLOR = floatArrayOf(0.8f, 0.4f, 0.1f, 1.0f) // Orange/Brown

private const val DAYS_IN_YEAR = 365
private const val DAY_ANGLE = 360f / DAYS_IN_YEAR.toFloat() // ~0.9863 degrees per day

data class Month(
    val name: String,
    val days: Int,
    val color: FloatArray,
    var startAngle: Float = 0f,
    val sweepAngle: Float = days.toFloat() * DAY_ANGLE
)

class CalendarRenderer(val refreshRate: Long, val dpi: Int) : Thread("CalendarRendererThread"), GlRenderer {
    private lateinit var surfaceTexture: SurfaceTexture

    @Volatile
    override var isStopped: Boolean = false

    // GlRenderer properties
    @Volatile
    override var width: Int = 0
    @Volatile
    override var height: Int = 0
    @Volatile
    override var positionY: Float = 0f 
    
    // Calendar-specific properties
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)
    private val rotationMatrix = FloatArray(16)
    @Volatile
    override var angle: Float = 0f
    @Volatile
    override var positionX: Float = 0f

    // Define the full calendar structure
    private val months: List<Month> = listOf(
        // WINTER (3 months)
        Month("Vyugas", 40, WINTER_COLOR),
        Month("Dzyamets", 41, WINTER_COLOR),
        Month("Skrezhen", 40, WINTER_COLOR),
        // SPRING (3 months)
        Month("Pronizh", 41, SPRING_COLOR),
        Month("Veles", 40, SPRING_COLOR),
        Month("Polesen", 41, SPRING_COLOR),
        // AUTUMN (3 months)
        Month("Trest", 40, AUTUMN_COLOR),
        Month("Listven", 41, AUTUMN_COLOR),
        Month("Hmaryen", 41, AUTUMN_COLOR)
    )

    // Calculate start angles for all months
    init {
        var currentAngle = 0f
        months.forEach { month ->
            month.startAngle = currentAngle
            currentAngle += month.sweepAngle
        }
    }

    // The main drawing primitive (all months share the same size/shape)
    private val sectorFigure = SectorFigure(radius = 0.9f)


    // GlRenderer implementation
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
            EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8,
            EGL14.EGL_ALPHA_SIZE, 8, EGL14.EGL_RENDERABLE_TYPE, renderableType,
            EGL14.EGL_NONE, 0, EGL14.EGL_NONE
        )
        val configsCount = intArrayOf(0);
        val configs = arrayOfNulls<EGLConfig>(1);
        EGL14.eglChooseConfig(eglDisplay, attribList, 0, configs, 0, configs.size, configsCount, 0)
        return configs[0]!!
    }

    override fun run() {
        val eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY)
        val version = intArrayOf(0, 0)
        EGL14.eglInitialize(eglDisplay, version, 0, version, 1)
        val eglConfig = getConfig(eglDisplay)
        val attribList = intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE)
        val eglContext = EGL14.eglCreateContext(eglDisplay, eglConfig, EGL14.EGL_NO_CONTEXT, attribList, 0)
        val surfaceAttribs = intArrayOf(EGL14.EGL_NONE)
        val eglSurface = EGL14.eglCreateWindowSurface(eglDisplay, eglConfig, surfaceTexture, surfaceAttribs, 0)

        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 2f)

        // Prepare the shared drawing primitive
        sectorFigure.prepare()

        while (!isStopped && EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
            EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

            GLES20.glClearColor(THEME_BACKGROUND_COLOR[0], THEME_BACKGROUND_COLOR[1], THEME_BACKGROUND_COLOR[2], THEME_BACKGROUND_COLOR[3])
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
            GLES20.glDisable(GLES20.GL_CULL_FACE)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            // Calculate VPM Matrix
            val scratch = FloatArray(16)
            
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            
            Matrix.translateM(scratch, 0, positionX, positionY, 0f)

            Matrix.setLookAtM(viewMatrix, 0, 0f, 0f, 1f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)
            // Final MVP matrix is now in `scratch`

            // --- DEBUG DRAW: Draw a single 360-degree sector ---
            sectorFigure.color = WINTER_COLOR
            sectorFigure.startAngleDeg = 0f
            sectorFigure.sweepAngleDeg = 360f
            sectorFigure.draw(scratch)
            // ----------------------------------------------------

            EGL14.eglSwapBuffers(eglDisplay, eglSurface)
            sleep(refreshRate)
        }

        surfaceTexture.release()
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
    }
}
