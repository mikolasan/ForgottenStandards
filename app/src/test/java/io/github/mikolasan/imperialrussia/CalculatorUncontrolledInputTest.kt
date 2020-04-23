package io.github.mikolasan.imperialrussia

import org.junit.Test

import org.junit.Assert.*

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CalculatorUncontrolledInputTest {
    @Test
    fun eval_emptyStringIsZero() {
        assertEquals(0.0, BasicCalculator("").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleDot() {
        assertEquals(-10.0, BasicCalculator("-10..").eval(), 1e-10)
    }

    @Test
    fun eval_safeToPrintPlusPlus() {
        assertEquals(0.0, BasicCalculator("++").eval(), 1e-10)
    }

    @Test
    fun eval_safeToPrintMinusMinus() {
        assertEquals(0.0, BasicCalculator("--").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleSlash() {
        assertEquals(-10.0, BasicCalculator("-10÷÷").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleStar() {
        assertEquals(-10.0, BasicCalculator("-10××").eval(), 1e-10)
    }

    @Test
    fun eval_ignoreDoubleHat() {
        assertEquals(10.0, BasicCalculator("10^^").eval(), 1e-10)
    }

}
