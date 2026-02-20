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

class HexFigure(val radius: Float, defaultColor: FloatArray) {

    var width = 0f
    var height = 0f

    var offset: Float = 0f
    var xOffset: Float = 0f
    private val scaleMatrix = FloatArray(16)
    init {
        Matrix.setIdentityM(scaleMatrix, 0)
    }

    private companion object {
        const val NUMBER_OF_VERTICES = 6
        const val ringSize = 0.05

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
        if (mProgram != 0) return
        mProgram = createProgram(simpleVertexShader, simpleFragmentShader) ?: 0
    }

    var color = defaultColor

    private var vertexBuffer: FloatBuffer =
        ByteBuffer.allocateDirect((NUMBER_OF_VERTICES * 2 + 2) * 3 * 4).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply {
                val innerRadius = if (radius > (radius * 0.1f)) radius * 0.8f else 0.01f
                for (i in 0 until NUMBER_OF_VERTICES) {
                    val angle = i * 2 * PI / NUMBER_OF_VERTICES
                    put((cos(angle) * radius).toFloat())
                    put((sin(angle) * radius).toFloat())
                    put(0.0f)
                    put((cos(angle) * innerRadius).toFloat())
                    put((sin(angle) * innerRadius).toFloat())
                    put(0.0f)
                }
                put(radius.toFloat())
                put(0.0f)
                put(0.0f)
                put(innerRadius.toFloat())
                put(0.0f)
                put(0.0f)
                rewind()
            }
        }


    fun draw(mvpMatrix: FloatArray) {
        if (mProgram == 0) prepare()
        GLES20.glUseProgram(mProgram)

        val attribPosition = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val uniformMvpMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        val uniformColor = GLES20.glGetUniformLocation(mProgram, "uColor")

        val finalTransform = FloatArray(16)
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, xOffset, offset, 0f)
        Matrix.multiplyMM(finalTransform, 0, mvpMatrix, 0, modelMatrix, 0)
        
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

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, NUMBER_OF_VERTICES * 2 + 2)
        GLES20.glDisableVertexAttribArray(attribPosition)
    }
}
