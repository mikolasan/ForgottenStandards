package io.github.mikolasan.imperialrussia

import org.junit.Test
import org.junit.Assert.*

class InverseFunctionTest {
    @Test
    fun tree_Operations() {
        val left = FunctionParser.Operand("2")
        val right = FunctionParser.Operand("3")
        val root = FunctionParser.Operation("+", left, right)
        val tree = FunctionParser.Tree(root)
        assertEquals(5.0, tree.eval(), 1e-10)
    }

    @Test
    fun inverse_x() {
        assertEquals("x", FunctionParser("x").inverse())
    }

    @Test
    fun inverse_celsiusToFahrenheitConversion() {
        assertEquals("(x - 32.0) * 5.0 / 9.0", FunctionParser("(x * 9.0 / 5.0) + 32.0").inverse())
    }
}