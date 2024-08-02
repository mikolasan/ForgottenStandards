package xyz.neupokoev.forgottenstandards

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class CalculatorUnitTest {
    @Test
    fun eval_parsesMultiplication() {
        assertEquals(4.0, BasicCalculator("2×2").eval(), 1e-10)
    }

    @Test
    fun eval_parsesDivision() {
        assertEquals(4.0, BasicCalculator("8÷2").eval(), 1e-10)
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
        assertEquals(4.0, BasicCalculator("4×").eval(), 1e-10)
    }

    @Test
    fun eval_ignoresSlash() {
        assertEquals(4.0, BasicCalculator("4÷").eval(), 1e-10)
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
    fun eval_ignoresSingleDot() {
        assertEquals(0.0, BasicCalculator(".").eval(), 1e-10)
    }

    @Test
    fun eval_safeToInputMinus() {
        assertEquals(0.0, BasicCalculator("-").eval(), 1e-10)
    }

    @Test
    fun eval_safeToInputMinusAndDot() {
        assertEquals(0.0, BasicCalculator("-.").eval(), 1e-10)
    }

    @Test
    fun eval_parsesUnaryMinus() {
        assertEquals(-0.5, BasicCalculator("-.5").eval(), 1e-10)
    }

    @Test
    fun eval_parsesDecimalWithoutZero() {
        assertEquals(0.15, BasicCalculator(".15").eval(), 1e-10)
    }

    @Test
    fun eval_parsesSumOfDecimals() {
        assertEquals(0.025, BasicCalculator(".012+.013").eval(), 1e-10)
    }

    @Test
    fun eval_operationPriority() {
        assertEquals(7.0, BasicCalculator("1+2×3").eval(), 1e-10)
        assertEquals(7.0, BasicCalculator("2×3+1").eval(), 1e-10)
        assertEquals(7.0, BasicCalculator("1+3×2").eval(), 1e-10)
        assertEquals(7.0, BasicCalculator("3×2+1").eval(), 1e-10)
    }

    @Test
    fun eval_parsesPower() {
        assertEquals(-4.0, BasicCalculator("-2^2").eval(), 1e-10)
    }

    @Test
    fun eval_parsesSequentialPower() {
        assertEquals(81.0, BasicCalculator("3^2^2").eval(), 1e-10)
        assertEquals(512.0, BasicCalculator("2^3^2").eval(), 1e-10)
    }

    @Test
    fun eval_powerOperationPriority() {
        assertEquals(18.0, BasicCalculator("2×3^2").eval(), 1e-10)
    }
}
