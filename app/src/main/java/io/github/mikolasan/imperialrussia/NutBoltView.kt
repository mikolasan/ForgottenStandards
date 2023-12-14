package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView

class NutBoltView(context: Context, attributeSet: AttributeSet) : TextureView(context, attributeSet) {
    private lateinit var renderer: TestRenderer

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    fun setRenderer(r: TestRenderer) {
        renderer = r
    }

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