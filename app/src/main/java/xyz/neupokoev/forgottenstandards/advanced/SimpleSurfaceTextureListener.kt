package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.SurfaceTexture
import android.view.TextureView

class SimpleSurfaceTextureListener : TextureView.SurfaceTextureListener {
    // Now uses the generic GlRenderer interface
    lateinit var renderer: GlRenderer

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        renderer.setSurface(surface)
        renderer.setSize(width, height)
        renderer.startRendering()
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        // Only size matters for projection matrix update
        renderer.setSize(width, height)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        renderer.stopRendering()
        // Wait for the rendering thread to finish (optional but safer)
        renderer.getThread().join()
        return true // Surface is released in the renderer's run() method
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
}