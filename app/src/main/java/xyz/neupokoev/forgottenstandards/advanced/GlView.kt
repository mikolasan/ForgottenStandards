package xyz.neupokoev.forgottenstandards.advanced

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView
import kotlin.math.sqrt


const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

interface GlViewClickListener {
    fun onGlViewClicked(x: Float, y: Float)
}

/**
 * A generic TextureView wrapper for GlRenderer implementations.
 */
class GlView(context: Context, attributeSet: AttributeSet?) : TextureView(context, attributeSet) {
    private val simpleSurfaceTextureListener = SimpleSurfaceTextureListener()

    /**
     * The renderer used to draw on this view. 
     * Setting this will automatically update the surface texture listener.
     */
    var renderer: GlRenderer? = null
        set(value) {
            field = value
            simpleSurfaceTextureListener.renderer = value
        }

    var clickListener: GlViewClickListener? = null

    init {
        surfaceTextureListener = simpleSurfaceTextureListener
    }
    
    private var previousX: Float = 0f
    private var previousY: Float = 0f
    private var startX: Float = 0f
    private var startY: Float = 0f
    private val clickThreshold = 10f // Pixels

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val currentRenderer = renderer ?: return false
        
        val x: Float = e.x
        val y: Float = e.y

        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = x
                startY = y
            }
            MotionEvent.ACTION_MOVE -> {

                val dx: Float = x - previousX
                val dy: Float = y - previousY

                if (currentRenderer is BoltRenderer) {
                    // BoltRenderer: Only vertical pan for scrolling through bolts
                    currentRenderer.positionY -= dy / 1000f
                } else if (currentRenderer is CalendarRenderer) {
                    // CalendarRenderer: Rotation control
                    // Use total motion delta for rotation, scaled
                    currentRenderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
                }
            }
            MotionEvent.ACTION_UP -> {
                val dist = sqrt((x - startX) * (x - startX) + (y - startY) * (y - startY))
                if (dist < clickThreshold) {
                    clickListener?.onGlViewClicked(x, y)
                }
            }
        }

        previousX = x
        previousY = y
        return true
    }
}
