package xyz.neupokoev.forgottenstandards.advanced

import android.opengl.GLES20
import android.opengl.Matrix
import xyz.neupokoev.forgottenstandards.COORDS_PER_VERTEX
import xyz.neupokoev.forgottenstandards.SIZE_OF_FLOAT
import xyz.neupokoev.forgottenstandards.createProgram
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class CircleFigure(val size: Float) {

    var width = 0f
    var height = 0f

    var offset: Float = 0f
    var xOffset: Float = 0f // New horizontal offset
    val radius: Float
    private val scaleMatrix = FloatArray(16)
    init {
        // size - in pixels. This is the diameter of the thread.
        radius = (size / 1000.0).toFloat() // Divide by 2000.0 (size / 2 / 1000.0) to get half the size and scale it to the OpenGL coordinate system
        Matrix.setIdentityM(scaleMatrix, 0)
    }

    private companion object {
        const val NUMBER_OF_SEGMENTS = 36 // A higher number for a smoother circle

        val simpleVertexShader = """
            uniform mat4 uMVPMatrix;
            attribute vec4 aPosition;
            void main() {
                gl_Position = uMVPMatrix * aPosition;
            }""".trimIndent()

        val simpleFragmentShader = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                gl_FragColor = uColor;
            }""".trimIndent()
    }

    private var mProgram: Int = 0

    fun prepare() {
        if (mProgram != 0) {
            return
        }
        mProgram = createProgram(simpleVertexShader, simpleFragmentShader) ?: 0
    }

    // Set color to a simple gray for the bolt body
    val color = floatArrayOf(0.4f, 0.4f, 0.4f, 1.0f)

    // Circle vertices: Center (0,0,0) + NUMBER_OF_SEGMENTS + closing vertex
    private var vertexBuffer: FloatBuffer =
        // (1 center + NUMBER_OF_SEGMENTS + 1 closing vertex) * 3 coords * 4 bytes
        ByteBuffer.allocateDirect((NUMBER_OF_SEGMENTS + 2) * COORDS_PER_VERTEX * SIZE_OF_FLOAT).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                put(0.0f) // Center X
                put(0.0f) // Center Y
                put(0.0f) // Center Z

                for (i in 0..NUMBER_OF_SEGMENTS) {
                    val angle = i * 2 * PI / NUMBER_OF_SEGMENTS
                    put((cos(angle) * radius).toFloat())    // X coordinate
                    put((sin(angle) * radius).toFloat())    // Y coordinate
                    put(0.0f)                   // Z coordinate
                }
                rewind()
            }
        }


    fun draw(mvpMatrix: FloatArray) {
        GLES20.glUseProgram(mProgram)

        val attribPosition = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val uniformMvpMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        val uniformColor = GLES20.glGetUniformLocation(mProgram, "uColor")

        // Pass the projection and view transformation to the shader
        val finalTransform = FloatArray(16)
        Matrix.multiplyMM(finalTransform, 0, mvpMatrix, 0, scaleMatrix, 0)
        // Apply X and Y offset
        Matrix.translateM(finalTransform, 0, xOffset, offset, 0f)
        GLES20.glUniformMatrix4fv(uniformMvpMatrix, 1, false, finalTransform, 0)
        GLES20.glUniform4fv(uniformColor, 1, color, 0)

        GLES20.glEnableVertexAttribArray(attribPosition)
        GLES20.glVertexAttribPointer(
            attribPosition,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            COORDS_PER_VERTEX * SIZE_OF_FLOAT,
            vertexBuffer
        )

        // Draw the circle using a Triangle Fan, starting from the center (0) and going through all segments (+1 for closing)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, NUMBER_OF_SEGMENTS + 2)

        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glUseProgram(0);
    }
}
