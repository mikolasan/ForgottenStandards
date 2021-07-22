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
        val tree = FunctionParser.Tree(root)
        assertEquals(5.0, tree.eval(), 1e-10)
    }

    @Test
    fun tree_OperationNoNodes() {
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        val tree = FunctionParser.Tree(root)
        assertThrows(Exception::class.java) {
            tree.eval()
        }
    }

    @Test
    fun tree_VariableEval() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val tree = FunctionParser.Tree(root)
        assertThrows(Exception::class.java) {
            tree.eval()
        }
    }

    @Test
    fun tree_ToString() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val tree = FunctionParser.Tree(root)
        assertEquals("x + 3", tree.string())
    }

    @Test
    fun tree_MutateTree() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val tree = FunctionParser.Tree(root)
        root.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(4.0, tree.eval(), 1e-10)
    }

    @Test
    fun tree_VariableInvert() {
        val left = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val right = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = left
        root.right = right
        val tree = FunctionParser.Tree(root)
        val invertTree = tree.invert() // x - 3
        invertTree.root.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(-2.0, invertTree.eval(), 1e-10)
    }

    @Test
    fun tree_Invert() {
        val x = FunctionParser.Node("x", FunctionParser.NodeType.VARIABLE)
        val a = FunctionParser.Node("3", FunctionParser.NodeType.OPERAND)
        val root = FunctionParser.Node("+", FunctionParser.NodeType.OPERATION)
        root.left = x
        root.right = a
        val tree = FunctionParser.Tree(root)
        val invertTree = tree.invert() // x - 3
        invertTree.root.left = FunctionParser.Node("1", FunctionParser.NodeType.OPERAND)
        assertEquals(-2.0, invertTree.eval(), 1e-10)
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