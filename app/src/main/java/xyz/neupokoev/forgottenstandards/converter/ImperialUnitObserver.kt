package xyz.neupokoev.forgottenstandards.converter

import android.text.Editable
import android.text.SpannableStringBuilder
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import xyz.neupokoev.forgottenstandards.BasicCalculator
import xyz.neupokoev.forgottenstandards.ObserverCallable
import xyz.neupokoev.forgottenstandards.valueForDisplay
import java.util.Optional

class ImperialUnitObserver (var unit: ImperialUnit?) {
    private var observers: MutableMap<Any, ObserverCallable> = mutableMapOf()
    var onErase: (() -> Unit)? = null
    var value: Double = 0.0
    private var formatted = SpannableStringBuilder()

    fun addObserver(observer: Any, callable: ObserverCallable) {
        observers.put(observer, callable)
    }

    fun removeObserver(observer: Any) {
        observers.remove(observer)
    }

    fun getEditable(): Editable = formatted

    fun setValueAndNotify(v: Double) {
        var finalValue = v
        if (unit?.unitName == ImperialUnitName.BEAUFORT) {
            finalValue = v.coerceIn(0.0, 12.0)
        }
        
        if (finalValue == 0.0) {
            onErase?.invoke()
        }
        value = finalValue
        formatted = valueForDisplay(finalValue)
        unit?.let {
            it.value = finalValue
            it.range = Optional.empty()
        }
        onValueUpdated()
    }

    fun setUnitAndUpdateValue(u: ImperialUnit) {
        unit = u
        formatted = valueForDisplay(u.value)
    }

    fun addChar(char: Char) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT && !char.isDigit()) return
        val v = BasicCalculator(formatted.append(char).toString()).eval()
        setValueAndNotify(v)
    }

    fun appendString(c: Char) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT && !c.isDigit()) return
        
        if (formatted.toString() == "0") {
            setString(c.toString())
        } else {
            setString(formatted.toString() + c)
        }
        val v = BasicCalculator(formatted.toString()).eval()
        setValueAndNotify(v)
    }

    fun dropLastChar() {
        onErase?.invoke()
        if (formatted.isNotEmpty()) {
            formatted.delete(formatted.length - 1, formatted.length)
            if (formatted.length == 0) {
                formatted.append("0")
            }

            val v = BasicCalculator(formatted.toString()).eval()
            setValueAndNotify(v)
        }
    }

    fun appendStringOrReplace(c: Char, replaceable: Set<Char>) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT && !c.isDigit()) return
        
        val s = formatted.toString()
        when {
            s.isEmpty() -> {
                setString(c.toString())
            }
            replaceable.containsAll(listOf(s.last(), c)) -> {
                setString(s.dropLast(1) + c.toString())
            }
            c == '.' -> {
                val factor = s.takeLastWhile { char ->
                    char in '0'..'9' || char == '.'
                }
                if (factor.isEmpty()) {
                    setString(s + c.toString())
                    return
                }
                if (factor.contains('.') || !isValidNumber(factor)) return
                setString(s + c.toString())
            }
            else -> {
                setString(s + c.toString())
            }
        }
        val v = BasicCalculator(formatted.toString()).eval()
        setValueAndNotify(v)
    }

    private fun setString(s: String) {
        formatted = SpannableStringBuilder(s)
    }

    private fun onValueUpdated() {
        unit?.let { u ->
            observers.forEach { (_, callable) ->
                callable(u, value)
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