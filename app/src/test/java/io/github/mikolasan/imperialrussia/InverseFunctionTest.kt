package io.github.mikolasan.imperialrussia

import org.junit.Test
import org.junit.Assert.*

class InverseFunctionTest {
    @Test
    fun tree_Operations() {
        val left = FunctionParser.Node("2", FunctionParser.NodeType.OPERAND)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertEquals(5.0, root.eval(), 1e-10)
    }

    @Test
    fun tree_OperationNoNodes() {
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        assertThrows(Exception::class.java) {
            root.eval()
        }
    }

    @Test
    fun tree_VariableEval() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertThrows(Exception::class.java) {
            root.eval()
        }
    }

    @Test
    fun tree_ToString() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertEquals("x + 3", root.string())
    }

    @Test
    fun tree_MutateTree() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        root.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(4.0, root.eval(), 1e-10)
    }

    @Test
    fun tree_VariableInvert() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val inversion = root.invert() // x - 3
        inversion.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(-2.0, inversion.eval(), 1e-10)
    }

    @Test
    fun tree_ParseAndInvert() {
        val x = FunctionParser("x + 3")
        val root = x.parse()
        val inversion = root.invert() // x - 3
        assertEquals("x - 3", inversion.string())
        inversion.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(-2.0, inversion.eval(), 1e-10)

        assertEquals("x - 3", FunctionParser("x + 3").inverse())
        assertEquals("x - 3", FunctionParser("3 + x").inverse())
        assertEquals("x + 3", FunctionParser("x - 3").inverse())
        assertEquals("3 - x", FunctionParser("3 - x").inverse())
        assertEquals("x / 3", FunctionParser("x * 3").inverse())
        assertEquals("x / 3", FunctionParser("3 * x").inverse())
        assertEquals("x * 3", FunctionParser("x / 3").inverse())
        assertEquals("3 / x", FunctionParser("3 / x").inverse())
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