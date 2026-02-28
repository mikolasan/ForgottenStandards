package xyz.neupokoev.forgottenstandards

import android.content.Context
import android.graphics.Color
import android.opengl.GLES20
import android.os.Build
import android.view.Display
import androidx.core.content.ContextCompat
import androidx.core.graphics.alpha
import androidx.core.graphics.blue
import androidx.core.graphics.green
import androidx.core.graphics.red
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val SIZE_OF_FLOAT = 4
const val COORDS_PER_VERTEX = 3

fun getNormalizedColor(context: Context, colorResId: Int): FloatArray {
    // 1. Get the color as an integer (0xAARRGGBB) from resources
    val colorInt = ContextCompat.getColor(context, colorResId)

    // 2. Extract components (0-255)
    val red255 = Color.red(colorInt)
    val green255 = Color.green(colorInt)
    val blue255 = Color.blue(colorInt)
    val alpha255 = Color.alpha(colorInt)

    // 3. Normalize to 0.0f - 1.0f
    return floatArrayOf(
        red255 / 255f,
        green255 / 255f,
        blue255 / 255f,
        alpha255 / 255f
    )
}

fun FloatArray.toFloatBuffer(): FloatBuffer =
    ByteBuffer.allocateDirect(size * 4)
        .order(ByteOrder.nativeOrder())
        .asFloatBuffer().also {
            it.put(this@toFloatBuffer)
            it.rewind()
        }


fun loadShader(type: Int, shaderCode: String): Int? {
    return GLES20.glCreateShader(type).also { shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)

        val compiled = IntArray(1)
        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
        if (compiled[0] == GLES20.GL_FALSE) {
            val info = GLES20.glGetShaderInfoLog(shader)
            println(info)
            return null
        }
    }
}

fun createProgram(vertexShader: String, fragmentShader: String): Int? {
    val vs: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShader) ?: return null
    val fs: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShader) ?: return null

    // create empty OpenGL ES Program
    return GLES20.glCreateProgram().also { program ->
        GLES20.glAttachShader(program, vs)
        GLES20.glAttachShader(program, fs)
        GLES20.glLinkProgram(program)

        val linked = IntArray(1)
        GLES20.glGetShaderiv(program, GLES20.GL_LINK_STATUS, linked, 0)
        if (linked[0] == GLES20.GL_FALSE) {
            val info = GLES20.glGetProgramInfoLog(program)
            println(info)
            return null
        }
    }
}

fun getDpi(context: Context): Int {
    return context.resources.displayMetrics.densityDpi
}

@Suppress("DEPRECATION")
fun getDisplayRefreshRate(context: Context): Long {
    val display: Display? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        try {
            context.display
        } catch (e: UnsupportedOperationException) {
            null
        }
    } else {
        null
    }

    display?.let {
        val displayFps: Double = it.refreshRate.toDouble()
        return Math.round(1000.0 / displayFps)
    }

    // Default to 60 FPS (16.67ms per frame)
    return 16L
}
