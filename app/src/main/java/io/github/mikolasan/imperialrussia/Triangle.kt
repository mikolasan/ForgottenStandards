package io.github.mikolasan.imperialrussia

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

// number of coordinates per vertex in this array
const val NUMBER_OF_VERTICES = 16
const val COORDS_PER_VERTEX = 3
var triangleCoords = floatArrayOf(     // in counterclockwise order:
    0.0f, 0.622008459f, 0.0f,      // top
    -0.5f, -0.311004243f, 0.0f,    // bottom left
    0.5f, -0.311004243f, 0.0f      // bottom right
)

fun loadShader(type: Int, shaderCode: String): Int {
    return GLES20.glCreateShader(type).also { shader ->
        GLES20.glShaderSource(shader, shaderCode)
        GLES20.glCompileShader(shader)
//        val compiled = intArrayOf(0)
//        GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0)
    }
}


class Triangle {

    private val vertexShaderCode =
        "uniform mat4 uMVPMatrix;" +
        "attribute vec4 aPosition;" +
        "void main() {" +
        "    gl_Position = uMVPMatrix * aPosition;" +
        "}"

    private val fragmentShaderCode =
        "precision mediump float;" +
        "uniform vec4 uColor;" +
        "void main() {" +
        "    gl_FragColor = uColor;" +
        "}"

    private var vPMatrixHandle: Int = 0
    private var mProgram: Int = -1

    private val radius1 = 0.4
    private val radius2 = 0.25
//    private val vertexBuffer = floatArrayOf()

    fun createProgram() {
        val vertexShader: Int = loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode)
        val fragmentShader: Int = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode)

        // create empty OpenGL ES Program
        mProgram = GLES20.glCreateProgram().also {
            GLES20.glAttachShader(it, vertexShader)
            GLES20.glAttachShader(it, fragmentShader)
            GLES20.glLinkProgram(it)
//            val linked = intArrayOf(0)
//            GLES20.glGetShaderiv(it, GLES20.GL_LINK_STATUS, linked, 0)
        }
    }

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

//    // Triangle
//    private var vertexBuffer: FloatBuffer =
//        // (number of coordinate values * 4 bytes per float)
//        ByteBuffer.allocateDirect(triangleCoords.size * 4).run {
//            // use the device hardware's native byte order
//            order(ByteOrder.nativeOrder())
//
//            // create a floating point buffer from the ByteBuffer
//            asFloatBuffer().apply {
//                // add the coordinates to the FloatBuffer
//                put(triangleCoords)
//                // set the buffer to read the first coordinate
//                position(0)
//            }
//        }

    // Circle
    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(NUMBER_OF_VERTICES * 3 * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                for (i in 0 until NUMBER_OF_VERTICES) {
                    val angle = i * 2 * PI / NUMBER_OF_VERTICES
                    put((cos(angle) * radius1).toFloat())    // X coordinate
                    put((sin(angle) * radius1).toFloat())    // Y coordinate
                    put(0.0f)                   // Z coordinate
                }
                rewind()
            }
        }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = triangleCoords.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * 4 // 4 bytes per vertex

    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        // get handle to vertex shader's vPosition member
//        positionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition").also {
//            GLES20.glEnableVertexAttribArray(it)
//            GLES20.glVertexAttribPointer(
//                it,
//                COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT,
//                false,
//                vertexStride,
//                vertexBuffer
//            )
//        }
        positionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition").also {
            GLES20.glEnableVertexAttribArray(it)
            GLES20.glVertexAttribPointer(
                it,
                COORDS_PER_VERTEX,
                GLES20.GL_FLOAT,
                false,
                vertexStride,
                vertexBuffer
            )
        }
//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, NUMBER_OF_VERTICES)

        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor").also { it ->
            GLES20.glUniform4fv(it, 1, color, 0)
        }

        vPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix").also {
            // Pass the projection and view transformation to the shader
            GLES20.glUniformMatrix4fv(it, 1, false, mvpMatrix, 0)
        }

//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, NUMBER_OF_VERTICES)
        GLES20.glDisableVertexAttribArray(positionHandle)
//        GLES20.glUseProgram(0);
    }

}
