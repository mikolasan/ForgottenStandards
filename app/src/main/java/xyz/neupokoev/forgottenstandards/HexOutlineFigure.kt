package xyz.neupokoev.forgottenstandards

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

class HexOutlineFigure {

    var width = 0f
    var height = 0f

    private companion object {
        const val NUMBER_OF_VERTICES = 6
        const val radius = 0.5

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

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    // Hex
    private var vertexBuffer: FloatBuffer =
        // (number of coordinate values * 4 bytes per float)
        ByteBuffer.allocateDirect(NUMBER_OF_VERTICES * 3 * 4).run {
            // use the device hardware's native byte order
            order(ByteOrder.nativeOrder())

            // create a floating point buffer from the ByteBuffer
            asFloatBuffer().apply {
                for (i in 0 until NUMBER_OF_VERTICES) {
                    val angle = i * 2 * PI / NUMBER_OF_VERTICES
                    put((cos(angle) * radius).toFloat())    // X coordinate
                    put((sin(angle) * radius).toFloat())    // Y coordinate
                    put(0.0f)                   // Z coordinate
                }
                rewind()
            }
        }


    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        val attribPosition = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val uniformMvpMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        val uniformColor = GLES20.glGetUniformLocation(mProgram, "uColor")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(uniformMvpMatrix, 1, false, mvpMatrix, 0)
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

        GLES20.glDrawArrays(GLES20.GL_LINE_LOOP, 0, NUMBER_OF_VERTICES)

        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glUseProgram(0);
    }

}
