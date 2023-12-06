package io.github.mikolasan.imperialrussia

import android.graphics.SurfaceTexture
import android.opengl.EGL14
import android.opengl.GLES20
import javax.microedition.khronos.egl.EGL10
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.egl.EGLContext
import javax.microedition.khronos.egl.EGLDisplay

class TestRenderer(val surface: SurfaceTexture) : Thread() {
    var isStopped = false

    val config = intArrayOf(
        EGL10.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
        EGL10.EGL_RED_SIZE, 8,
        EGL10.EGL_GREEN_SIZE, 8,
        EGL10.EGL_BLUE_SIZE, 8,
        EGL10.EGL_ALPHA_SIZE, 8,
        EGL10.EGL_DEPTH_SIZE, 0,
        EGL10.EGL_STENCIL_SIZE, 0,
        EGL10.EGL_NONE
    )

    private var mTriangle: Triangle = Triangle()
    private var mSquare: Square = Square()

    fun chooseEglConfig(egl: EGL10, eglDisplay: EGLDisplay): EGLConfig {
        val configsCount = intArrayOf(0);
        val configs = arrayOfNulls<EGLConfig>(1);
        egl.eglChooseConfig(eglDisplay, config, configs, 1, configsCount)
        return configs[0]!!
    }


    override fun run() {
        super.run()

        val egl = EGLContext.getEGL() as EGL10
        val eglDisplay = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY)
        egl.eglInitialize(eglDisplay, intArrayOf(0, 0))   // getting OpenGL ES 2
        val eglConfig = chooseEglConfig(egl, eglDisplay);
        val eglContext = egl.eglCreateContext(
            eglDisplay,
            eglConfig,
            EGL10.EGL_NO_CONTEXT,
            intArrayOf(EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL10.EGL_NONE)
        );
        val eglSurface = egl.eglCreateWindowSurface(eglDisplay, eglConfig, surface, null)

        var colorVelocity = 0.01f
        var color = 0f
        while (!isStopped && egl.eglGetError() == EGL10.EGL_SUCCESS) {
            egl.eglMakeCurrent(eglDisplay, eglSurface, eglSurface, eglContext)

            if (color > 1 || color < 0) colorVelocity *= -1
            color += colorVelocity

            GLES20.glClearColor(color / 2, color, color, 1.0f)
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

            mTriangle.draw()
            egl.eglSwapBuffers(eglDisplay, eglSurface)

            Thread.sleep((1f / 60f * 1000f).toLong()) // in real life this sleep is more complicated
        }

        surface.release()
        egl.eglDestroyContext(eglDisplay, eglContext)
        egl.eglDestroySurface(eglDisplay, eglSurface)
    }
}