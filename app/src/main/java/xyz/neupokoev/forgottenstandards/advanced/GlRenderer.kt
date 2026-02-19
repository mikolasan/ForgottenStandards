package xyz.neupokoev.forgottenstandards.advanced

import android.graphics.SurfaceTexture

/**
 * Interface for all custom OpenGL ES 2.0 renderers used in the application.
 *
 * This allows a common view (like NutBoltView/GlView) to host different rendering logic
 * without changing the view itself.
 */
interface GlRenderer {
    var isStopped: Boolean

    // View properties needed for projection matrix and calculation
    val width: Int
    val height: Int
    var positionY: Float // Changed to var for pan control
    var positionX: Float // Added for pan control
    var angle: Float // Added for rotation control

    fun setSurface(surface: SurfaceTexture)
    fun setSize(width: Int, height: Int)
    fun stopRendering()
    fun startRendering()
    fun getThread(): Thread
}
