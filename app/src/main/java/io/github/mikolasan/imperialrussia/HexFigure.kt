package io.github.mikolasan.imperialrussia

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class HexFigure {

    var width = 0f
    var height = 0f

    private companion object {
        val right = 0.5f
        val bottom = -0.5f
        val left = -0.5f
        val top = 0.5f

        val vertexBuffer = floatArrayOf(
            right, bottom, 0f, // v1
            right, top, 0f, // v2
            left, top, 0f, // v3
            left, bottom, 0f, // v4
        )
        val quad: FloatArray = floatArrayOf(
            right, bottom, 0f, 1.0f, -1.0f,
            right, top, 0f, 1.0f, 1.0f,
            left, top, 0f, -1.0f, 1.0f,
            left, bottom, 0f, -1.0f, -1.0f,
        )

        // TODO
//        #ifdef GL_ES
//        precision mediump float;
//        #endif

        val simpleVertexShader = """
            uniform mat4 uMVPMatrix;
            attribute vec4 aPosition;
            void main() {
                gl_Position = uMVPMatrix * aPosition;
            }""".trimIndent()

        val circleVertexShader = """
            uniform mat4 uMVPMatrix;
            attribute vec4 aPosition;
            attribute vec2 value;
            varying vec2 val;
            varying vec2 vUv;
            void main() { 
                val = value; 
                gl_Position = uMVPMatrix * aPosition; 
            }""".trimIndent()

        val simpleFragmentShader = """
            precision mediump float;
            uniform vec4 uColor;
            void main() {
                gl_FragColor = uColor;
            }""".trimIndent()

        // Why it is necessary to set precision for the fragment shader?
        // Because GLSL ES standard says so.
        val circleFragmentShader = """
            precision mediump float;
            uniform vec2 iResolution;
            varying vec2 val;
            varying vec2 vUv;
            void main() {
                float R = 1.0;
                float R2 = 0.9;
                float dist = sqrt(dot(val,val));
                if (dist >= R) {
                    discard;
                }
                vec4 color = vec4(0.0, 0.0, 0.0, 1.0);
                if (dist <= R2) {
                    vec2 st = gl_FragCoord.xy / iResolution.xy;

                    vec3 color1 = vec3(1.0, 0.55, 0.0);
                    vec3 color2 = vec3(0.226, 0.000, 0.615);
                    
                    float mixValue = distance(st,vec2(0,1));
                    vec3 colorm = mix(color1,color2,mixValue);
                    
                    color = vec4(ceil(sin(st.x * 40.)), 0.0, 0.5 , 1.0);

                }
                gl_FragData[0] = color;
            }""".trimIndent()
    }

    private var vPMatrixHandle: Int = 0
    private var mProgram: Int = 0

    private val radius1 = 0.4
    private val radius2 = 0.25
//    private val vertexBuffer = floatArrayOf()

    private var vbo: IntArray = IntArray(1)

    fun prepare() {
        if (mProgram != 0) {
            return
        }
        mProgram = createProgram(circleVertexShader, circleFragmentShader) ?: 0
        setupVbo()
    }

    private fun setupVbo() {
        GLES20.glGenBuffers(1, vbo, 0)
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, vbo[0])
        GLES20.glBufferData(
            GLES20.GL_ARRAY_BUFFER,
            quad.size * SIZE_OF_FLOAT,
            quad.toFloatBuffer(),
            GLES20.GL_STATIC_DRAW
        )
    }

    // Set color with red, green, blue and alpha (opacity) values
    val color = floatArrayOf(0.63671875f, 0.76953125f, 0.22265625f, 1.0f)

    private fun FloatArray.toFloatBuffer(): FloatBuffer =
        ByteBuffer.allocateDirect(size * 4)
            .order(ByteOrder.nativeOrder())
            .asFloatBuffer().also {
                it.put(this@toFloatBuffer)
                it.rewind()
            }

    // Circle
//    private var vertexBuffer: FloatBuffer =
//        // (number of coordinate values * 4 bytes per float)
//        ByteBuffer.allocateDirect(NUMBER_OF_VERTICES * 3 * 4).run {
//            // use the device hardware's native byte order
//            order(ByteOrder.nativeOrder())
//
//            // create a floating point buffer from the ByteBuffer
//            asFloatBuffer().apply {
//                for (i in 0 until NUMBER_OF_VERTICES) {
//                    val angle = i * 2 * PI / NUMBER_OF_VERTICES
//                    put((cos(angle) * radius1).toFloat())    // X coordinate
//                    put((sin(angle) * radius1).toFloat())    // Y coordinate
//                    put(0.0f)                   // Z coordinate
//                }
//                rewind()
//            }
//        }

    private var positionHandle: Int = 0
    private var mColorHandle: Int = 0

    private val vertexCount: Int = vertexBuffer.size / COORDS_PER_VERTEX
    private val vertexStride: Int = COORDS_PER_VERTEX * SIZE_OF_FLOAT // 4 bytes per vertex

    fun draw(mvpMatrix: FloatArray) {
        // Add program to OpenGL ES environment
        GLES20.glUseProgram(mProgram)

        val attribPosition = GLES20.glGetAttribLocation(mProgram, "aPosition")
        val attribValue = GLES20.glGetAttribLocation(mProgram, "value")
        val uniformMvpMatrix = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix")
        val uniformResolution = GLES20.glGetUniformLocation(mProgram, "iResolution")

        // Pass the projection and view transformation to the shader
        GLES20.glUniformMatrix4fv(uniformMvpMatrix, 1, false, mvpMatrix, 0)
        GLES20.glUniform2f(uniformResolution, width, height)
        GLES20.glEnableVertexAttribArray(attribPosition)
        GLES20.glEnableVertexAttribArray(attribValue)

        GLES20.glVertexAttribPointer(
            attribPosition,
            COORDS_PER_VERTEX,
            GLES20.GL_FLOAT,
            false,
            20,
            0
        )

        GLES20.glVertexAttribPointer(
            attribValue,
            2,
            GLES20.GL_FLOAT,
            false,
            20,
            12
        )

//            GLES20.glEnableVertexAttribArray(it)
//            GLES20.glVertexAttribPointer(
//                it,
//                COORDS_PER_VERTEX,
//                GLES20.GL_FLOAT,
//                false,
//                vertexStride,
//                vertexBuffer.toFloatBuffer()
//            )

//        mColorHandle = GLES20.glGetUniformLocation(mProgram, "uColor").also { it ->
//            GLES20.glUniform4fv(it, 1, color, 0)
//        }


//        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount)
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4)

        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribValue)
//        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0)
        GLES20.glUseProgram(0);
    }

}
