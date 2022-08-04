package io.github.mikolasan.imperialrussia

import org.junit.Test
import org.junit.Assert.*

class InverseFunctionTest {
    @Test
    fun nodeEvaluation() {
        val left = FunctionParser.Node("2", FunctionParser.NodeType.OPERAND)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertEquals(5.0, root.eval(), 1e-10)
    }

    @Test
    fun terminalNodeNotEvaluating() {
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        assertThrows(Exception::class.java) {
            root.eval()
        }
    }

    @Test
    fun variableInTreeNotEvaluating() {
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
    fun nodesAsString() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertEquals("x + 3", root.string())
    }

    @Test
    fun nodeReplaceChildNodeAndEvaluate() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        assertEquals(4.0, root.eval("1"), 1e-10)
    }


    @Test
    fun nestedNodesAsStringAddParenthesis() {
        // (x + 3) * 2
        //
        //     *
        //    / \
        //   +   2
        //  / \
        // x   3
        //
        // x + 3 * 2
        //
        //   +
        //  / \
        // x   *
        //    / \
        //   3   2
        //
        val root = FunctionParser.Node("*", FunctionParser.NodeType.OPERATION)
        root.left = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left!!.left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        root.left!!.right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        root.right = FunctionParser.Node("2", FunctionParser.NodeType.OPERAND)
        assertEquals("(x + 3) * 2", root.string())
        val node = FunctionParser().parse("x + 3 * 2")
        assertEquals("x + 3 * 2", node.string())
    }

    @Test
    fun nodeParseParenthesis() {
        val expression = "(x * 9.0 / 5.0) + 32.0"
        val node = FunctionParser().parse(expression)
//        assertEquals(expression, node.string())
        assertEquals("x * 9.0 / 5.0 + 32.0", node.string())

        val expression2 = "(x - 32.0) * 5.0 / 9.0"
        val node2 = FunctionParser().parse(expression2)
        assertEquals(expression2, node2.string())
    }

    @Test
    fun nodeInverseVariableIsIdentity() {
        assertEquals("x", FunctionParser().inverse("x"))
    }

    @Test
    fun nodeInvertReplaceEvaluate() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val inversion = root.invert() // x - 3
        assertEquals(-2.0, inversion.eval("1"), 1e-10)
    }

    @Test
    fun nodeParseInvertToString() {
        val root = FunctionParser().parse("x + 3")
        val inversion = root.invert() // x - 3
        assertEquals("x - 3", inversion.string())
        assertEquals(-2.0, inversion.eval("1"), 1e-10)

        assertEquals("x - 3", FunctionParser().inverse("x + 3"))
        assertEquals("x - 3", FunctionParser().inverse("3 + x"))
        assertEquals("x - 4 - 3", FunctionParser().inverse("x + 3 + 4"))
        assertEquals("x + 4 - 3", FunctionParser().inverse("x + 3 - 4"))
        assertEquals("x + 3", FunctionParser().inverse("x - 3"))
        assertEquals("3 - x", FunctionParser().inverse("3 - x"))
        assertEquals("x / 3", FunctionParser().inverse("x * 3"))
        assertEquals("x / 3", FunctionParser().inverse("3 * x"))
        assertEquals("x * 3", FunctionParser().inverse("x / 3"))
        assertEquals("3 / x", FunctionParser().inverse("3 / x"))
        // x * 5.0 / 9.0
        //
        //    '/'
        //    / \
        //   *   9
        //  / \
        // x   5
        //
        assertEquals("x * 9.0 / 5.0", FunctionParser().inverse("x * 5.0 / 9.0"))
    }

    @Test
    fun celsiusToFahrenheitConversion() {
        // (x * 9.0 / 5.0) + 32.0
        //
        //       +
        //      / \
        //    '/' 32
        //    / \
        //   *   5
        //  / \
        // x   9
        //
        // (x - 32.0) * 5.0 / 9.0
        //      '/'
        //      / \
        //     *  9
        //    / \
        //   -   5
        //  / \
        // x  32
        //
        assertEquals("(x - 32.0) * 5.0 / 9.0", FunctionParser().inverse("(x * 9.0 / 5.0) + 32.0"))
    }
}