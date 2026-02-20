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

class SectorFigure(
    val radius: Float,
    val segments: Int = 36
) {
    var xOffset: Float = 0f
    var yOffset: Float = 0f

    private var mProgram: Int = 0

    // Properties to be set for each draw call
    var startAngleDeg: Float = 0f
    var sweepAngleDeg: Float = 0f
    var color = floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f) // Default to white

    private var vertexBuffer: FloatBuffer
    private val vertexCount: Int

    init {
        // Vertices: Center (1) + Segments (N) + Closing Vertex (1) = N + 2
        vertexCount = segments + 2

        // A temporary mutable buffer to hold only the vertices for the current segment
        // The size of this buffer will be enough for the largest possible segment
        vertexBuffer = ByteBuffer.allocateDirect(vertexCount * COORDS_PER_VERTEX * SIZE_OF_FLOAT).run {
            order(ByteOrder.nativeOrder())
            asFloatBuffer().apply { rewind() }
        }
    }

    private companion object {
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


    fun prepare() {
        if (mProgram != 0) return
        mProgram = createProgram(simpleVertexShader, simpleFragmentShader) ?: 0
    }
    
    // Helper to generate the vertex data for the specific sector
    private fun generateSectorVertices(startAngle: Float, sweepAngle: Float): Int {
        val buffer = vertexBuffer
        buffer.clear()
        
        // 1. Center vertex
        buffer.put(0.0f) // X
        buffer.put(0.0f) // Y
        buffer.put(0.0f) // Z

        val startRad = startAngle * PI.toFloat() / 180f
        val endRad = (startAngle + sweepAngle) * PI.toFloat() / 180f
        
        // The number of segments is proportional to the sweep angle
        val actualSegments = (segments * (sweepAngle / 360f)).toInt().coerceAtLeast(1)

        for (i in 0..actualSegments) {
            val angleRad = startRad + (i.toFloat() / actualSegments.toFloat()) * (endRad - startRad)
            buffer.put(cos(angleRad) * radius) // X
            buffer.put(sin(angleRad) * radius) // Y
            buffer.put(0.0f)                   // Z
        }
        buffer.rewind()
        
        // Return vertex count: Center (1) + actualSegments + Closing vertex (1)
        return actualSegments + 2
    }

    fun draw(mvpMatrix: FloatArray) {
        if (mProgram == 0) prepare()

        // Dynamically generate the vertex data for the slice
        val drawCount = generateSectorVertices(startAngleDeg, sweepAngleDeg)

        GLES20.glUseProgram(mProgram)

        val attribPosition = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val uniformMvpMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        val uniformColor = GLES20.glGetUniformLocation(mProgram, "uColor")
        
        // Defensive check against the 0x501 attribute error
        if (attribPosition < 0) {
            // Log.e("SectorFigure", "aPosition attribute not found!")
            return
        }

        // 1. Calculate MVP Matrix (Apply position and rotation)
        val finalTransform = FloatArray(16)
        val modelMatrix = FloatArray(16)
        Matrix.setIdentityM(modelMatrix, 0)
        Matrix.translateM(modelMatrix, 0, xOffset, yOffset, 0f)
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
        
        // Draw the sector using GL_TRIANGLE_FAN, starting from the center (index 0)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, drawCount)
        
        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glUseProgram(0)
    }
}
