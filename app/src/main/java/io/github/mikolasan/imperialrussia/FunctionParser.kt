package io.github.mikolasan.imperialrussia

import java.lang.Exception
import java.text.DecimalFormatSymbols
import kotlin.math.pow

// Expr    ← Sum
//Sum     ← Product (('+' / '-') Product)*
//Product ← Power (('*' / '/') Power)*
//Power   ← Value ('^' Power)?
//Value   ← [0-9]+ / '(' Expr ')'

class FunctionParser(private val expression: String) {
    class Tree(val root: Node) {
        // ?
    }

    enum class NodeType {
        OPERAND,
        OPERATION,
        VARIABLE
    }

    class Node(var atom: String,
               val type: NodeType,
               var left: Node? = null,
               var right: Node? = null,
    ) {
        fun string(): String {
            return left?.atom + " " + atom + " " + right?.atom
        }

        fun eval(): Double {
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

        fun invert(): Node {
            when (atom) {
                "/" -> {
                    if (left?.type == NodeType.VARIABLE) {
                        // y = x / a -> x = y * a
                        atom = "*"
                    } else if (right?.type == NodeType.VARIABLE) {
                        // y = a / x -> x = a / y
                        // no changes
                    }
                }
                "*" -> {
                    if (left?.type == NodeType.VARIABLE) {
                        // y = x * a -> x = y / a
                        atom = "/"
                    } else if (right?.type == NodeType.VARIABLE) {
                        // y = a * x -> x = y / a
                        val a = left
                        left = right
                        right = a
                        atom = "/"
                    }
                }
                "-" -> {
                    if (left?.type == NodeType.VARIABLE) {
                        // y = x - a -> x = y + a
                        atom = "+"
                    } else if (right?.type == NodeType.VARIABLE) {
                        // y = a - x -> x = a - y
                        // no changes
                    }
                }
                "+" -> {
                    if (left?.type == NodeType.VARIABLE) {
                        // y = x + a -> x = y - a
                        atom = "-"
                    } else if (right?.type == NodeType.VARIABLE) {
                        // y = a + x -> x = y - a
                        val a = left
                        left = right
                        right = a
                        atom = "-"
                    }
                }
                else -> throw Exception("No such operation")
            }
            return this
        }
    }

    class Carriage(var char: Char? = null, var pos: Int = -1) {
        private val grouping = DecimalFormatSymbols.getInstance().groupingSeparator
        private val decimal = DecimalFormatSymbols.getInstance().decimalSeparator

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
        fun findFactor(expression: String): String {
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
    }

    private val carriage = Carriage()

    private fun eat(charToEat: Char): Boolean {
        return carriage.onChar(charToEat)
                && carriage.hasNextChar(expression)
                && carriage.fastForward(expression) != null
    }

    fun parse(): Node {
        carriage.fastForward(expression)
        return parseExpression()
    }

    private fun parseExpression(): Node {
        var x = parseTerm()
        while (true) {
            when {
                eat('+') -> {
                    x = Node("+", NodeType.OPERATION, x, parseTerm())
//                    x.left = x
//                    x.left?.left = null
//                    x.left?.right = null
//                    x.atom = "+"
//                    x.right = parseTerm()
                }
                eat('-') -> {
                    x = Node("-", NodeType.OPERATION, x, parseTerm())
//                    x.left = x
//                    x.left?.left = null
//                    x.left?.right = null
//                    x.atom = "-"
//                    x.right = parseTerm()
                }
                else -> return x
            }
        }
    }

    private fun parseTerm(): Node {
        var x = parseFactor()
        while (true) {
            when {
                eat('*') -> {
                    x = Node("*", NodeType.OPERATION, x, parseFactor())
//                    val y = parseFactor()
//                    x *= y
                }
                eat('/') -> {
                    x = Node("/", NodeType.OPERATION, x, parseFactor())
//                    val y = parseFactor()
//                    x /= y
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
//        return try {
//            factor.toDouble()
//        } catch (e: NumberFormatException) {
//            0.0
//        }
    }

    private fun parseFactor(): Node {
        if (eat('+')) {
            // unary plus
            return Node("+", NodeType.OPERATION, null, parseFactor())
//            return parseFactor()
        }
        if (eat('-')) {
            // unary minus
            return Node("-", NodeType.OPERATION, null, parseFactor())
//            return -parseFactor()
        }
        val factor = carriage.findFactor(expression)
        if (factor.isEmpty() || factor == ".") {
            return Node("0.0", NodeType.OPERAND)
//            return 0.0
        }
        val x = parseNumber(factor)
//        if (eat('^')) {
//            return x.pow(parseFactor())
//        }
        return x
    }

    fun inverse(): String {
        val root = parse()
        val inversion = root.invert()
        return inversion.string()
    }
}