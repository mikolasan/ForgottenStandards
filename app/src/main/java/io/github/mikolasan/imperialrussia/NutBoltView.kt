package io.github.mikolasan.imperialrussia

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView


const val TOUCH_SCALE_FACTOR: Float = 180.0f / 320f

class NutBoltView(context: Context, attributeSet: AttributeSet) : TextureView(context, attributeSet) {
    private val simpleSurfaceTextureListener = SimpleSurfaceTextureListener()
    private val refreshRate = getDisplayRefreshRate(context)
    private val dpi = getDpi(context)
    private var renderer: TestRenderer = TestRenderer(refreshRate, dpi)
    init {
        simpleSurfaceTextureListener.renderer = renderer
        surfaceTextureListener = simpleSurfaceTextureListener
    }
    private var previousX: Float = 0f
    private var previousY: Float = 0f

    @SuppressLint("NewApi")
    fun getDisplayRefreshRate(context: Context): Long {
        context.display?.let { display ->
            val displayFps: Double = display.refreshRate.toDouble()
            val refreshMilli = Math.round(1f / displayFps * 1000)
            println("refresh rate is $displayFps fps --> $refreshMilli millis")
            return refreshMilli
        }
        val defaulValue = (1f / 60f * 1000f).toLong()
        return defaulValue
    }

    fun getDpi(context: Context): Int {
        return context.resources.displayMetrics.densityDpi
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        val x: Float = e.x
        val y: Float = e.y
//        val width = renderer.width
//        val height = renderer.height

        when (e.action) {
            MotionEvent.ACTION_MOVE -> {

                var dx: Float = x - previousX
                var dy: Float = y - previousY

                renderer.positionX += dx / 1000f
                renderer.positionY -= dy / 1000f

                println("touch delta ${dx}, ${dy}")

                // reverse direction of rotation above the mid-line
                if (y > height / 2) {
                    dx *= -1
                }

                // reverse direction of rotation to left of the mid-line
                if (x < width / 2) {
                    dy *= -1
                }

//                renderer.angle += (dx + dy) * TOUCH_SCALE_FACTOR
//                requestRender()

            }
        }

        previousX = x
        previousY = y
        return true

    }

}