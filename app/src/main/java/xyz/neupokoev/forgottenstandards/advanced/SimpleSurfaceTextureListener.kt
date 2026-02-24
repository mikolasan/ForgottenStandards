package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.SurfaceTexture
import android.view.TextureView

class SimpleSurfaceTextureListener : TextureView.SurfaceTextureListener {
    // Renderer is set by GlView
    var renderer: GlRenderer? = null

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
        renderer?.let {
            it.setSurface(surface)
            it.setSize(width, height)
            it.startRendering()
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        renderer?.setSize(width, height)
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        renderer?.let {
            it.stopRendering()
            try {
                it.getThread().join()
            } catch (e: InterruptedException) {
                // Ignore
            }
        }
        return true 
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
    }
}
