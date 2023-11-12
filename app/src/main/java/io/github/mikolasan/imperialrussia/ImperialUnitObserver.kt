package io.github.mikolasan.imperialrussia

import android.text.Editable
import android.text.SpannableStringBuilder
import android.widget.TextView
import io.github.mikolasan.ratiogenerator.ImperialUnit

typealias ObserverCallable = (ImperialUnit, Double) -> Unit

class ImperialUnitObserver (var unit: ImperialUnit?) {
    private var observers: MutableList<ObserverCallable> = mutableListOf()
    var value: Double = 0.0
    private var formatted = SpannableStringBuilder()

    fun addObserver(observer: ObserverCallable) {
        observers.add(observer)
    }

    fun getEditable(): Editable = formatted

    fun setValueAndNotify(v: Double) {
        unit?.let {
            it.value = v
        }
        value = v
        formatted = valueForDisplay(v)
        onValueUpdated()
    }

    fun setUnitAndUpdateValue(u: ImperialUnit) {
        unit = u
//        value save?
//        u.value = value
    }

    fun addChar(char: Char) {
        val v = BasicCalculator(formatted.append(char).toString()).eval()
        setValueAndNotify(v)
    }

    fun dropLastChar() {
        val v = BasicCalculator(formatted.dropLast(1).toString()).eval()
        setValueAndNotify(v)
    }

    fun setString(s: String) {
        formatted = SpannableStringBuilder(s)
    }

    fun appendStringOrReplace(c: Char, replaceable: Set<Char>) {
        val value = formatted.toString()
        when {
            value.isEmpty() -> {
                setString(c.toString())
            }
            replaceable.containsAll(listOf(value.last(), c)) -> {
                setString(value.dropLast(1) + c.toString())
            }
            c == '.' -> {
                val factor = value.takeLastWhile { char ->
                    char in '0'..'9' || char == '.'
                }
                if (factor.isEmpty()) {
                    setString(value + c.toString())
                    return
                }
                if (factor.contains('.') || !isValidNumber(factor)) return
                setString(value + c.toString())
            }
            else -> {
                setString(value + c.toString())
            }
        }
    }

    private fun onValueUpdated() {
        unit?.let { u ->
            observers.forEach {
                it(u, value)
            }
        }
    }

    private fun isValidNumber(s: String): Boolean {
        return try {
            val x = s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }
}