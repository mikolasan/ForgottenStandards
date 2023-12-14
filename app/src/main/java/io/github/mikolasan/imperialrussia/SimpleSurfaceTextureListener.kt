package io.github.mikolasan.imperialrussia

import android.graphics.SurfaceTexture
import android.view.TextureView

class SimpleSurfaceTextureListener : TextureView.SurfaceTextureListener {
    lateinit var renderer: TestRenderer

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        renderer = TestRenderer(surface)
        renderer.width = width
        renderer.height = height
        renderer.start()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        renderer.width = width
        renderer.height = height
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        renderer.isStopped = true
        return false                // surface.release() manually, after the last render
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
}