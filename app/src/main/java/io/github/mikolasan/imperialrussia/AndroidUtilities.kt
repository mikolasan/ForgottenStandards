package io.github.mikolasan.imperialrussia

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

const val SIZE_OF_FLOAT = 4
const val COORDS_PER_VERTEX = 3


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