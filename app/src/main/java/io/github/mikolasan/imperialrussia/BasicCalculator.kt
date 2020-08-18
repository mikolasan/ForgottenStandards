package io.github.mikolasan.imperialrussia

import java.text.DecimalFormatSymbols
import kotlin.math.pow

/*
 It does addition, subtraction, multiplication, division
 and it gets the operator precedence and associativity rules correct.

 https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
 */

class BasicCalculator(val expression: String) {
    val grouping = DecimalFormatSymbols.getInstance().groupingSeparator
    val decimal = DecimalFormatSymbols.getInstance().decimalSeparator

    var char: Char? = null
    var pos = -1
    fun eval(): Double {
        if (expression.isEmpty()) return 0.0
        return parse()
    }


    private fun hasNextChar(): Boolean {
        return pos + 1 < expression.length
    }

    private fun nextChar(): Char? {
        char = expression.elementAtOrNull(++pos)
        return char
    }

    private fun eat(charToEat: Char): Boolean {
        return (char == charToEat) && hasNextChar() && nextChar() != null
    }

    private fun parse(): Double{
        nextChar()
        return parseExpression()
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | number | factor `^` factor

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
        val startPos = pos
        while (char in '0'..'9' || char == decimal || char == grouping) {
            nextChar()
        }
        val factor = expression.substring(startPos, pos).replace(grouping.toString(), "")
        if (factor.isEmpty() || factor == ".") {
            return 0.0
        }
        val x = parseNumber(factor)
        if (eat('^')) {
            return x.pow(parseFactor())
        }
        return x
    }
}
