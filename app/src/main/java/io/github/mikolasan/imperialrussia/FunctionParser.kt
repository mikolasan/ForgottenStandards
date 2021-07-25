package io.github.mikolasan.imperialrussia

import java.lang.Exception
import java.text.DecimalFormatSymbols
import kotlin.math.exp
import kotlin.math.pow

// Expr    ← Sum
// Sum     ← Product (('+' / '-') Product)*
// Product ← Power (('*' / '/') Power)*
// Power   ← Value ('^' Power)?
// Value   ← [0-9]+ / '(' Expr ')'

class FunctionParser() {
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

        fun swapChildNodes() {
            val a = left
            left = right
            right = a
        }

        fun invert(l: Node? = null, r: Node? = null): Node {
            if (type == NodeType.OPERATION) {
                when (atom) {
                    "/" -> {
                        if (right?.type == NodeType.OPERAND) {
                            // y = x / a -> x = y * a
//                            atom = "*"
                            return Node("*", NodeType.OPERATION, left?.invert(), right)

                        } else if (left?.type == NodeType.OPERAND) {
                            // y = a / x -> x = a / y
                            // no changes
                            return Node("/", NodeType.OPERATION, left, right?.invert())
                        }
                    }
                    "*" -> {
                        if (right?.type == NodeType.OPERAND) {
                            // y = x * a -> x = y / a
//                            atom = "/"
                            return Node("/", NodeType.OPERATION, left?.invert(), right)
                        } else if (left?.type == NodeType.OPERAND) {
                            // y = a * x -> x = y / a
//                            swapChildNodes()
//                            atom = "/"
                            return Node("/", NodeType.OPERATION, right?.invert(), left)
                        }
                    }
                    "-" -> {
                        if (right?.type == NodeType.OPERAND) {
                            // y = x - a -> x = y + a
//                            atom = "+"
                            return Node("+", NodeType.OPERATION, left?.invert(), right)
                        } else if (left?.type == NodeType.OPERAND) {
                            // y = a - x -> x = a - y
                            // no changes
                            return Node("-", NodeType.OPERATION, left, right?.invert())
                        }
                    }
                    "+" -> {
                        if (right?.type == NodeType.OPERAND) {
                            // y = x + a -> x = y - a
//                            atom = "-"
                            val newLeft = Node("-", NodeType.OPERATION,
                                Node("x", NodeType.VARIABLE),
                                Node(right!!.atom, right!!.type))
                            if (left == null) {
                                return newLeft
                            } else {
                                return left!!.invert(newLeft, null)
                            }
                        } else if (left?.type == NodeType.OPERAND) {
                            // y = a + x -> x = y - a
//                            swapChildNodes()
//                            atom = "-"
                            return Node("-", NodeType.OPERATION, right?.invert(), left)
                        }
                    }
                    else -> throw Exception("No such operation")
                }
            }
            return if (l != null) l else this
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

    fun parse(expression: String): Node {
        val carriage = Carriage(expression)
        return parseExpression(carriage)
    }

    private fun parseExpression(carriage: Carriage): Node {
        var x = parseTerm(carriage)
        while (true) {
            when {
                carriage.eat('+') -> {
                    x = Node("+", NodeType.OPERATION, x, parseTerm(carriage))
                }
                carriage.eat('-') -> {
                    x = Node("-", NodeType.OPERATION, x, parseTerm(carriage))
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
                    x = Node("*", NodeType.OPERATION, x, parseFactor(carriage))
                }
                carriage.eat('/') -> {
                    x = Node("/", NodeType.OPERATION, x, parseFactor(carriage))
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
            return Node("+", NodeType.OPERATION, null, parseFactor(carriage))
        } else if (carriage.eat('-')) {
            return Node("-", NodeType.OPERATION, null, parseFactor(carriage))
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

    fun inverse(expression: String): String {
        val root = parse(expression)
        val inversion = root.invert()
        return inversion.string()
    }
}