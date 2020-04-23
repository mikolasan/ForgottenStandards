package io.github.mikolasan.imperialrussia

import java.text.DecimalFormatSymbols

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
        return parse()
    }


    fun hasNextChar(): Boolean {
        return pos + 1 < expression.length
    }

    fun nextChar(): Char? {
        char = expression.elementAtOrNull(++pos)
        return char
    }

    fun eat(charToEat: Char): Boolean {
        return (char == charToEat) && hasNextChar() && nextChar() != null
    }

    fun parse(): Double{
        nextChar()
        return parseExpression()
    }

    // Grammar:
    // expression = term | expression `+` term | expression `-` term
    // term = factor | term `*` factor | term `/` factor
    // factor = `+` factor | `-` factor | number

    fun parseExpression(): Double {
        var x = parseTerm()
        while (true) {
            when {
                eat('+') -> x += parseTerm()
                eat('-') -> x -= parseTerm()
                else -> return x
            }
        }
    }

    fun parseTerm(): Double {
        var x = parseFactor() ?: 0.0
        while (true) {
            when {
                eat('×') -> {
                    x *= parseFactor() ?: 1.0
                }
                eat('÷') -> {
                    x /= parseFactor() ?: 1.0
                }
                else -> return x
            }
        }
    }

    fun parseNumber(factor: String): Double {
        var x = .0
        try {
            x = factor.toDouble()
        } catch (e: NumberFormatException) {
            if (e.message?.compareTo("multiple points") == 0) { // TODO
                x = parseNumber(factor.dropLast(1))
            }
        }
        return x
    }

    fun parseFactor(): Double? {
        if (eat('+')) return parseFactor() // unary plus
        if (eat('-')) return -parseFactor()!! // unary minus
        val startPos = pos
        while (char in '0'..'9' || char == decimal || char == grouping) {
            nextChar()
        }
        val factor = expression.substring(startPos, pos).replace(grouping.toString(), "")
        return when {
            factor.isEmpty() -> null
            factor == "." -> 0.0
            else -> parseNumber(factor)
        }
    }
}