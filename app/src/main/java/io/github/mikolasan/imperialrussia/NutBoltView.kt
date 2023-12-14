package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView

const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class NutBoltView(context: Context, attributeSet: AttributeSet) : TextureView(context, attributeSet) {
    private val simpleSurfaceTextureListener = SimpleSurfaceTextureListener()
    private var renderer: TestRenderer = TestRenderer()
    init {
        simpleSurfaceTextureListener.renderer = renderer
        surfaceTextureListener = simpleSurfaceTextureListener
    }
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y
        val width = 500
        val height = 500

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }

                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
//                requestRender()
            }
        }

        previousX = x
        previousY = y
        return true

//        return super.onTouchEvent(event)
    }

}