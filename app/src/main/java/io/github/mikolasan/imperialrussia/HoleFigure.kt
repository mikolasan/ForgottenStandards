package io.github.mikolasan.imperialrussia

import android.opengl.GLES20
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

class HoleFigure {

    var width = 0f
    var height = 0f

    private companion object {
        val right = 0.5f
        val bottom = -0.5f
        val left = -0.5f
        val top = 0.5f

//        val vertexBuffer = floatArrayOf(
//            right, bottom, 0f, // v1
//            right, top, 0f, // v2
//            left, top, 0f, // v3
//            left, bottom, 0f, // v4
//        )
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

    private var mProgram: Int = 0
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
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4)

        GLES20.glDisableVertexAttribArray(attribPosition)
        GLES20.glDisableVertexAttribArray(attribValue)
        GLES20.glUseProgram(0);
    }

}
