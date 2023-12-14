package io.github.mikolasan.imperialrussia

import android.opengl.Matrix
import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.EGLConfig
import android.opengl.EGLDisplay
import android.opengl.GLES20

class TestRenderer : Thread() {
    lateinit var surface: SurfaceTexture
    var isStopped = false

    private var mTriangle: Triangle = Triangle()
    private var mSquare: Square = Square()
    @Volatile
    var angle: Float = 0f
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
        val mvpMatrix = FloatArray(16)
        Matrix.setIdentityM(mvpMatrix, 0)
        var colorVelocity = 0.01f
        var color = 0f
        while (!isStopped && EGL14.eglGetError() == EGL14.EGL_SUCCESS) {
            EGL14.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

//            if (color > 1 || color < 0) colorVelocity *= -1
//            color += colorVelocity

            GLES20.glClearColor(color / 2, color, color, 1.0f)
            GLES20.glDisable(GLES20.GL_DEPTH_TEST)
            GLES20.glDisable(GLES20.GL_CULL_FACE)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            val scratch = FloatArray(16)


            // Create a rotation for the triangle
            // long time = SystemClock.uptimeMillis() % 4000L;
            // float angle = 0.090f * ((int) time);
            Matrix.setRotateM(rotationMatrix, 0, angle, 0f, 0f, -1.0f)

            // Combine the rotation matrix with the projection and camera view
            // Note that the mvpMatrix factor *must be first* in order
            // for the matrix multiplication product to be correct.
            Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0)

//            GLES20.glViewport(0, 0, width, height);

            mTriangle.createProgram()
            mTriangle.draw()

            EGL14.eglSwapBuffers(eglDisplay, eglSurface)

            sleep((1f / 60f * 1000f).toLong()) // in real life this sleep is more complicated
        }

        surface.release()
        EGL14.eglDestroyContext(eglDisplay, eglContext)
        EGL14.eglDestroySurface(eglDisplay, eglSurface)
    }
}