package xyz.neupokoev.forgottenstandards

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import android.opengl.GLES20
import android.opengl.Matrix

class TestRenderer(val refreshRate: Long, val dpi: Int) : Thread() {
    lateinit var surface: SurfaceTexture
    var isStopped = false

    val metricGauge = arrayOf("M6", "M7", "M8", "M10", "M12", "M14", "M16", "M18", "M20")
    val nameToMm = mapOf<String, Float>(
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

    private val mHexFigures: Array<HexFigure> = metricGauge
        .map { s: String ->
            val centimeters: Float = (nameToMm[s] ?: 0f) / 10f
            val inchesPerCm = centimeters / 2.54f
            val pixelSize = dpi * inchesPerCm
            HexFigure(pixelSize)
        }.toTypedArray()

    init {
        // change the offset
        mHexFigures.fold(-1.0f) { acc, f ->
            f.offset = acc
            acc + 2.0f * f.radius
        }
    }
    private val vPMatrix = FloatArray(16)
    private val projectionMatrix = FloatArray(16)
    private val viewMatrix = FloatArray(16)

    @Volatile
    var angle: Float = 0f
    @Volatile
    var positionX: Float = 0f
    @Volatile
    var positionY: Float = 0f
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
            //EGL14.EGL_DEPTH_SIZE, 16,
            //EGL14.EGL_STENCIL_SIZE, 8,
            EGL14.EGL_RENDERABLE_TYPE, renderableType,
            EGL14.EGL_NONE, 0,      // placeholder for recordable [@-3]
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

//    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
//        GLES20.glViewport(0, 0, width, height)
//
//        val ratio: Float = width.toFloat() / height.toFloat()
//
//        // this projection matrix is applied to object coordinates
//        // in the onDrawFrame() method
//        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
//    }


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
//        val mvpMatrix = FloatArray(16)
//        Matrix.setIdentityM(mvpMatrix, 0)
        var colorVelocity = 0.01f
        var color = 1.0f

        GLES20.glViewport(0, 0, width, height)
        val ratio: Float = width.toFloat() / height.toFloat()

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1f, 1f, 1f, 2f)
//        Matrix.orthoM(projectionMatrix,
//            0,
//            -width / 2f,
//            width / 2f,
//            -height / 2f,
//            height / 2f,
//            0f,
//            10f
//        );


        while (!isStopped && EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
            EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

//            if (color > 1 || color < 0) colorVelocity *= -1
//            color += colorVelocity

            GLES20.glClearColor(color, color, color, 1.0f)
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
            GLES20.glDisable(GLES20.GL_CULL_FACE)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)



            val scratch = FloatArray(16)


            // Create a rotation for the triangle
            // long time = SystemClock.uptimeMillis() % 4000L;
            // float angle = 0.090f * ((int) time);
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)


            // Combine the rotation matrix with the projection and camera view
            // Note that the vPMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, vPMatrix, 0, rotationMatrix, 0)
            Matrix.translateM(scratch, 0, positionX, positionY, 0f)

//            GLES20.glViewport(0, 0, width, height);

            // Set the camera position (View matrix)
            Matrix.setLookAtM(viewMatrix,
                0,
                0f, 0f, 1f,
                0f, 0f, 0f,
                0f, 1.0f, 0.0f)

            // Calculate the projection and view transformation
            Matrix.multiplyMM(vPMatrix, 0, projectionMatrix, 0, viewMatrix, 0)

//            mHoleFigure.width = width.toFloat()
//            mHoleFigure.height = height.toFloat()
//            mHoleFigure.prepare()
//            mHoleFigure.draw(scratch)

            mHexFigures.forEach { f ->
                f.width = width.toFloat()
                f.height = height.toFloat()
                f.prepare()
                f.draw(scratch)
            }

            EGL14.eglSwapBuffers(eglDisplay, eglSurface)

            sleep(refreshRate) // in real life this sleep is more complicated
        }

        surface.release()
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
    }
}