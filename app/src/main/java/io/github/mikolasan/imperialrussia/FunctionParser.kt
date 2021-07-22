package io.github.mikolasan.imperialrussia

import java.lang.Exception
import java.text.DecimalFormatSymbols
import kotlin.math.pow

// Expr    ← Sum
//Sum     ← Product (('+' / '-') Product)*
//Product ← Power (('*' / '/') Power)*
//Power   ← Value ('^' Power)?
//Value   ← [0-9]+ / '(' Expr ')'

class FunctionParser(val expression: String) {
    class Tree(val root: Node) {
        fun eval(): Double {
            return root.eval()
        }
    }

    open class Node(
        val atom: String,
        val left: Node? = null,
        val right: Node? = null
    ) {
        open fun eval(): Double {
            return 0.0
        }
    }

    class Operand(
        atom: String,
        left: Node? = null,
        right: Node? = null
    ): Node(atom, left, right) {
        override fun eval(): Double {
            return atom.toDouble();
        }
    }

    class Operation(
        atom: String,
        left: Node? = null,
        right: Node? = null
    ) : Node(atom, left, right) {
        override fun eval(): Double {
            val leftOp = left ?: throw Exception("Left operand is not defined")
            val rightOp = right ?: throw Exception("Right operand is not defined")
            return when (atom) {
                "/" -> leftOp.eval() / rightOp.eval()
                "*" -> leftOp.eval() * rightOp.eval()
                "-" -> leftOp.eval() - rightOp.eval()
                "+" -> leftOp.eval() + rightOp.eval()
                else -> 0.0
            }
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
        fun nextChar(expression: String): Char? {
            char = expression.elementAtOrNull(++pos)
            return char
        }
        fun findFactor(expression: String): String {
            val startPos = pos
            while (char in '0'..'9' || char == decimal || char == grouping) {
                nextChar(expression)
            }
            return expression.substring(startPos, pos).replace(grouping.toString(), "")
        }
    }
    val carriage = Carriage()



    private fun eat(charToEat: Char): Boolean {
        return carriage.onChar(charToEat) && carriage.hasNextChar(expression) && carriage.nextChar(expression) != null
    }
    fun parse(): Double {
        carriage.nextChar(expression)
        return parseExpression()
    }
    private fun parseExpression(): Double {
        var x = parseTerm()
        while (true) {
            when {
                eat('+') -> x += parseTerm()
                eat('-') -> x -= parseTerm()
                else -> return x
            }
        }
    }

    private fun parseTerm(): Double {
        var x = parseFactor() ?: 0.0
        while (true) {
            when {
                eat('×') -> {
                    val y = parseFactor() ?: 1.0
                    x *= y
                }
                eat('÷') -> {
                    val y = parseFactor() ?: 1.0
                    x /= y
                }
                else -> return x
            }
        }
    }

    private fun parseNumber(factor: String): Double {
        return try {
            factor.toDouble()
        } catch (e: NumberFormatException) {
            0.0
        }
    }

    private fun parseFactor(): Double {
        if (eat('+')) return parseFactor() // unary plus
        if (eat('-')) return -parseFactor() // unary minus
        val factor = carriage.findFactor(expression)
        if (factor.isEmpty() || factor == ".") {
            return 0.0
        }
        val x = parseNumber(factor)
        if (eat('^')) {
            return x.pow(parseFactor())
        }
        return x
    }

    fun inverse(): String {
        return expression
    }
}