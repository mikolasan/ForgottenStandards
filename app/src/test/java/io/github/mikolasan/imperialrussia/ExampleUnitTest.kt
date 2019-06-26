package io.github.mikolasan.imperialrussia

import org.junit.Test

import org.junit.Assert.*
import org.junit.Rule
import org.junit.rules.ExpectedException
import java.lang.NumberFormatException

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun eval_parsesMultiplication() {
        assertEquals(4.0, BasicCalculator("2*2").eval(), 1e-10)
    }

    @Test
    fun eval_parsesDivision() {
        assertEquals(4.0, BasicCalculator("8/2").eval(), 1e-10)
    }

    @Test
    fun eval_parsesSum() {
        assertEquals(4.0, BasicCalculator("2+2").eval(), 1e-10)
    }

    @Test
    fun eval_parsesSubstraction() {
        assertEquals(4.0, BasicCalculator("10-6").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresStar() {
        assertEquals(4.0, BasicCalculator("4*").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresSlash() {
        assertEquals(4.0, BasicCalculator("4/").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresPlus() {
        assertEquals(4.0, BasicCalculator("4+").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresMinus() {
        assertEquals(4.0, BasicCalculator("4-").eval(), 1e-10)
    }

    @Test
    fun eval_appliesMinusToSum() {
        assertEquals(4.0, BasicCalculator("-6+10").eval(), 1e-10)
    }

    @Test
    fun eval_appliesMinusToSub() {
        assertEquals(-10.0, BasicCalculator("-2-8").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleDot() {
        assertEquals(-10.0, BasicCalculator("-10..").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleSlash() {
        assertEquals(-10.0, BasicCalculator("-10//").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresDoubleStar() {
        assertEquals(-10.0, BasicCalculator("-10**").eval(), 1e-10)
    }

    @Test
    fun eval_parsesDecimalWithoutZero() {
        assertEquals(0.15, BasicCalculator(".15").eval(), 1e-10)
    }
}
