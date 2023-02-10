package io.github.mikolasan.convertmeifyoucan

import java.lang.Exception
import java.text.DecimalFormatSymbols
import kotlin.math.exp
import kotlin.math.pow

// Expr    ← Sum
// Sum     ← Product (('+' / '-') Product)*
// Product ← Power (('*' / '/') Power)*
// Power   ← Value ('^' Power)?
// Value   ← [0-9]+ / '(' Expr ')'

class FunctionParser {
    class Tree(val root: Node) {
        // ?
    }

    enum class NodeType {
        OPERAND,
        OPERATION,
        VARIABLE
    }

    class Node(var atom: String,
               var type: NodeType,
               var left: Node? = null,
               var right: Node? = null,
               var parent: Node? = null,
    ) {
        fun string(surroundingOperation: String? = null): String {
            var addParenthesis = false
            if (surroundingOperation != null && type == NodeType.OPERATION) {
                if ((surroundingOperation == "/" || surroundingOperation == "*") && (atom == "+" || atom == "-")) {
                    addParenthesis = true
                }
            }
            val leftString = if (left == null) "" else left!!.string(if (type == NodeType.OPERATION) atom else null) + " "
            val rightString = if (right == null) "" else " " + right!!.string(if (type == NodeType.OPERATION) atom else null)
            return if (addParenthesis) "(" + leftString + atom + rightString + ")" else leftString + atom + rightString
        }

        fun eval(x: String? = null): Double {
            if (x != null) {
                val xNode = findVariable()
                xNode?.atom = x
                xNode?.type = NodeType.OPERAND
            }
            when (type) {
                NodeType.OPERAND -> return atom.toDouble()
                NodeType.OPERATION -> {
                    val leftOp = left ?: throw Exception("Left operand is not defined")
                    val rightOp = right ?: throw Exception("Right operand is not defined")
                    return when (atom) {
                        "/" -> leftOp.eval() / rightOp.eval()
                        "*" -> leftOp.eval() * rightOp.eval()
                        "-" -> leftOp.eval() - rightOp.eval()
                        "+" -> leftOp.eval() + rightOp.eval()
                        else -> throw Exception("No such operation")
                    }
                }
                else -> throw Exception("Function must be inverted before evaluation")
            }
        }

        private fun newOperationNode(operation: String, left: Node, right: Node) =
            Node(operation, NodeType.OPERATION, left, right)

        private fun copyLeftNode(src: Node) = Node(src.left!!.atom, src.left!!.type)

        private fun copyRightNode(src: Node) = Node(src.right!!.atom, src.right!!.type)

        fun invert(): Node {
            var inversion = Node("x", NodeType.VARIABLE)
            var pointer: Node? = this
            while (pointer?.type == NodeType.OPERATION) {
                val (movedOperand, newPointer) = when (pointer.atom) {
                    "+" -> when {
                        pointer.right?.type == NodeType.OPERAND -> {
                            // y = x + a -> y - a = x
                            Pair(newOperationNode("-", inversion, copyRightNode(pointer)),
                                pointer.left)
                        }
                        pointer.left?.type == NodeType.OPERAND -> {
                            // y = a + x -> y - a = x
                            Pair(newOperationNode("-", inversion, copyLeftNode(pointer)),
                                pointer.right)
                        }
                        else -> Pair(inversion, null)
                    }
                    "*" -> when {
                        pointer.right?.type == NodeType.OPERAND -> {
                            // y = x * a -> y / a = x
                            Pair(newOperationNode("/", inversion, copyRightNode(pointer)),
                                pointer.left)
                        }
                        pointer.left?.type == NodeType.OPERAND -> {
                            // y = a * x -> y / a = x
                            Pair(newOperationNode("/", inversion, copyLeftNode(pointer)),
                                pointer.right)
                        }
                        else -> Pair(inversion, null)
                    }
                    "-" -> when {
                        pointer.right?.type == NodeType.OPERAND -> {
                            // y = x - a -> y + a = x
                            Pair(newOperationNode("+", inversion, copyRightNode(pointer)),
                                pointer.left)
                        }
                        pointer.left?.type == NodeType.OPERAND -> {
                            // y = a - x -> a - y = x
                            Pair(newOperationNode("-", copyLeftNode(pointer), inversion),
                                pointer.right)
                        }
                        else -> Pair(inversion, null)
                    }
                    "/" -> when {
                        pointer.right?.type == NodeType.OPERAND -> {
                            // y = x / a -> y * a = x
                            Pair(newOperationNode("*", inversion, copyRightNode(pointer)),
                                pointer.left)
                        }
                        pointer.left?.type == NodeType.OPERAND -> {
                            // y = a / x -> a / y = x
                            Pair(newOperationNode("/", copyLeftNode(pointer), inversion),
                                pointer.right)
                        }
                        else -> Pair(inversion, null)
                    }
                    else -> throw Exception("No such operation")
                }
                inversion = movedOperand
                pointer = newPointer
            }
            return inversion
        }

        fun findVariable(): Node? {
            var variableNode: Node?
            variableNode = left?.let { leftNode ->
                if (leftNode.type == NodeType.VARIABLE) {
                    return leftNode
                }
                return@let leftNode.findVariable()
            }
            if (variableNode == null) {
                variableNode = right?.let { rightNode ->
                    if (rightNode.type == NodeType.VARIABLE) {
                        return rightNode
                    }
                    return@let rightNode.findVariable()
                }
            }
            return variableNode
        }
    }

    class Carriage(val expression: String) {
        private val grouping = DecimalFormatSymbols.getInstance().groupingSeparator
        private val decimal = DecimalFormatSymbols.getInstance().decimalSeparator
        var char: Char? = null
        var pos: Int = -1

        init {
            fastForward(expression)
        }

        fun onChar(check: Char): Boolean {
            return char == check
        }
        fun hasNextChar(expression: String): Boolean {
            return pos + 1 < expression.length
        }
        fun fastForward(expression: String): Char? {
            do {
                char = expression.elementAtOrNull(++pos)
            } while (char == ' ')
            return char
        }
        fun nextChar(expression: String): Char? {
            char = expression.elementAtOrNull(++pos)
            return char
        }
        fun eat(charToEat: Char): Boolean {
            return onChar(charToEat)
                    && hasNextChar(expression)
                    && fastForward(expression) != null
        }
        fun findFactor(): String {
            val startPos = pos
            while (char == 'x' || char in '0'..'9' || char == decimal || char == grouping) {
                nextChar(expression)
            }
            val endPos = pos
            fastForward(expression)
            return expression
                .substring(startPos, endPos)
                .replace(grouping.toString(), "")
        }
        fun inParenthesis(): String {
            val startPos = pos
            while (char != ')') {
                nextChar(expression)
            }
            val endPos = pos
            fastForward(expression)
            return expression.substring(startPos, endPos)
        }
    }

    private fun parseExpression(carriage: Carriage): Node {
        var x = parseTerm(carriage)
        while (true) {
            when {
                carriage.eat('+') -> {
                    val y = Node("+", NodeType.OPERATION)
                    y.left = x
                    y.left?.parent = y
                    y.right = parseTerm(carriage)
                    y.right?.parent = y
                    x = y
                }
                carriage.eat('-') -> {
                    val y = Node("-", NodeType.OPERATION)
                    y.left = x
                    y.left?.parent = y
                    y.right = parseTerm(carriage)
                    y.right?.parent = y
                    x = y
                }
                else -> return x
            }
        }
    }

    private fun parseTerm(carriage: Carriage): Node {
        var x = parseFactor(carriage)
        while (true) {
            when {
                carriage.eat('*') -> {
                    val y = Node("*", NodeType.OPERATION)
                    y.left = x
                    y.left?.parent = y
                    y.right = parseFactor(carriage)
                    y.right?.parent = y
                    x = y
                }
                carriage.eat('/') -> {
                    val y = Node("/", NodeType.OPERATION)
                    y.left = x
                    y.left?.parent = y
                    y.right = parseFactor(carriage)
                    y.right?.parent = y
                    x = y
                }
                else -> return x
            }
        }
    }

    private fun parseNumber(factor: String): Node {
        if (factor == "x") {
            return Node("x", NodeType.VARIABLE)
        }
        return Node(factor, NodeType.OPERAND)
    }

    private fun parseFactor(carriage: Carriage): Node {
        if (carriage.eat('(')) {
            return parseExpression(Carriage(carriage.inParenthesis()))
        } else if (carriage.eat('+')) {
            val y = Node("+", NodeType.OPERATION)
            y.right = parseFactor(carriage)
            return y
        } else if (carriage.eat('-')) {
            val y = Node("-", NodeType.OPERATION)
            y.right = parseFactor(carriage)
            return y
        }
        val factor = carriage.findFactor()
        if (factor.isEmpty() || factor == ".") {
            return Node("0.0", NodeType.OPERAND)
        }
        val x = parseNumber(factor)
//        if (eat('^')) {
//            return x.pow(parseFactor())
//        }
        return x
    }

    fun parse(expression: String): Node {
        val carriage = Carriage(expression)
        return parseExpression(carriage)
    }

    fun parse(array: Array<String>): Array<Node> = array
        .map {f -> parse(f)}
        .asReversed()
        .toTypedArray()

    fun inverse(expression: String): String {
        val root = parse(expression)
        return root.invert().string()
//        val x = root.findVariable()
//        val inversion = x?.parent?.invert()
//        return inversion?.string() ?: ""
    }

    fun inverse(array: Array<String>): Array<String> = array
            .map {f -> inverse(f) }
            .asReversed()
            .toTypedArray()
}