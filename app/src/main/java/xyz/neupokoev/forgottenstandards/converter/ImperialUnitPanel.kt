package xyz.neupokoev.forgottenstandards.converter

import android.content.Context
import android.graphics.PorterDuff
import android.text.InputType
import android.text.SpannableString
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import xyz.neupokoev.forgottenstandards.BasicCalculator
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.getColor
import xyz.neupokoev.forgottenstandards.stringForDisplay
import xyz.neupokoev.forgottenstandards.valueForDisplay
import java.util.Locale

class ImperialUnitPanel(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    init {
        inflate(context, R.layout.big_unit_space, this)
    }

    var unit: ImperialUnit? = null
    private var isActive = false
    val input: TextView = findViewById(R.id.panel_input)
    private val title: TextView = findViewById(R.id.panel_title)
    private val hint: TextView = findViewById(R.id.panel_hint)
    private val description: TextView = findViewById(R.id.panel_description)
    private val layout: ConstraintLayout = findViewById(R.id.big_unit_space)
    val bookmark: View = findViewById(R.id.bookmark)
    private val controls: View = findViewById(R.id.panel_controls)
    private val buttonMinus: View = findViewById(R.id.button_minus)
    private val buttonPlus: View = findViewById(R.id.button_plus)

    private val colorInputNormal = getColor(R.color.input_font)
    private val colorInputSelected = getColor(R.color.input_selected_font)
    private val colorNormal = getColor(R.color.panel_font)
    private val colorSelected = getColor(R.color.panel_selected_font)

    init {
        val bookmarkColor = R.color.bookmark
        val color = bookmark.context.resources.getColor(bookmarkColor)
        if (bookmark is android.widget.ImageView) {
            bookmark.drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        }

        input.inputType = InputType.TYPE_CLASS_NUMBER
        input.setTextColor(colorInputNormal)
        
        buttonMinus.setOnClickListener {
            unit?.let { u ->
                val newValue = (u.value - 1.0).coerceAtLeast(0.0)
                if (newValue != u.value) {
                    u.value = newValue
                    updateDisplayValue()
                }
            }
        }
        
        buttonPlus.setOnClickListener {
            unit?.let { u ->
                val newValue = (u.value + 1.0).coerceAtMost(12.0)
                if (newValue != u.value) {
                    u.value = newValue
                    updateDisplayValue()
                }
            }
        }
        
        deactivate()
    }

    fun setHighlight(highlight: Boolean) {
        isActive = highlight
        layout.setBackgroundResource(if (highlight) R.color.panel_selected_back else R.color.panel_back)
        title.setTextColor(if (highlight) colorSelected else colorNormal)
        input.setBackgroundResource(if (highlight) R.color.input_selected_back else R.color.input_back)
        input.setTextColor(if (highlight) colorInputSelected else colorInputNormal)

        if (!isActive && hasUnitAssigned() && getString() == "") {
            setUnitValue(0.0)
            updateDisplayValue()
        } else if (isActive && unit?.value?.compareTo(0.0) == 0) {
            setString("")
        }
    }

    fun activate() {
        title.visibility = VISIBLE
        input.visibility = VISIBLE
        hint.visibility = INVISIBLE
        updateControlsVisibility()
    }

    fun deactivate() {
        title.visibility = INVISIBLE
        input.visibility = INVISIBLE
        hint.visibility = VISIBLE
        controls.visibility = GONE
        description.visibility = GONE
    }

    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit
        updateUnitText()
        updateControlsVisibility()
        updateDisplayValue()
    }

    private fun updateControlsVisibility() {
        if (unit?.unitName == ImperialUnitName.BEAUFORT && hasUnitAssigned()) {
            controls.visibility = VISIBLE
            input.isEnabled = false // Disable direct text input for Beaufort if using buttons
        } else {
            controls.visibility = GONE
            input.isEnabled = true
        }
    }

    private fun updateUnitText() {
        unit?.unitName?.name?.let { s ->
            val underlineText = SpannableString(s
                .lowercase(Locale.ROOT)
                .replace('_', ' ')
                .replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                })
            title.text = underlineText
        }
    }

    fun setHintText(newText: String) {
        hint.text = newText
    }

    fun hasUnitAssigned(): Boolean {
        return title.visibility == VISIBLE
    }

    fun getValue(): Double? = unit?.value

    fun setUnitValue(v: Double) {
        unit?.apply {
            value = if (unitName == ImperialUnitName.BEAUFORT) v.coerceIn(0.0, 12.0) else v
        }
    }

    fun updateDisplayValue() {
        val u = unit ?: return
        val v = u.value
        input.text = valueForDisplay(v)
        
        // Update descriptive name
        if (u.rangeValueNames.containsKey(v)) {
            description.text = u.rangeValueNames[v]
            description.visibility = VISIBLE
        } else if (u.unitName == ImperialUnitName.BEAUFORT) {
             // For Beaufort, we might need to find the name even if it's not an exact key 
             // because conversion can result in non-integer values if we aren't careful
             // but our convertValueFromRange returns an exact key.
             description.text = u.rangeValueNames[v.toInt().toDouble()] ?: ""
             description.visibility = VISIBLE
        } else {
            description.visibility = GONE
        }

        if (isActive && getString() == "0") {
            input.setText("")
        }
    }

    fun getString(): String {
        return input.text.toString()
    }

    fun setString(s: String) {
        if (isActive && s == "0") {
            input.setText("")
        } else {
            input.setText(s)
        }
    }

    fun appendString(c: Char) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT) return // No direct typing for Beaufort
        setString(getString() + c.toString())
    }

    private fun isValidNumber(s: String): Boolean {
        return try {
            val x = s.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun appendStringOrReplace(c: Char, replaceable: Set<Char>) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT) return // No direct typing for Beaufort
        val value = getString()
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

    fun dropLastChar() {
        if (unit?.unitName == ImperialUnitName.BEAUFORT) return
        setString(getString().dropLast(1))
    }

    fun evaluateString(s: String? = null) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT) return
        val expression = s ?: getString()
        val value = BasicCalculator(expression).eval()
        setUnitValue(value)
        updateDisplayValue()
    }

    fun hasExponent(): Boolean {
        return false
    }

    fun formatStringAndSet(s: String) {
        if (unit?.unitName == ImperialUnitName.BEAUFORT) return
        val expression = s ?: getString()
        val value = BasicCalculator(expression).eval()
        setUnitValue(value)
        input.text = stringForDisplay(expression)
        if (isActive && getString() == "0") {
            input.setText("")
        }
    }
}