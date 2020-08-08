package io.github.mikolasan.imperialrussia

import android.content.Context
import android.os.Build
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import android.text.SpannableString
import android.text.style.SuperscriptSpan
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.mikolasan.ratiogenerator.ImperialUnit
import java.text.DecimalFormat


class ImperialUnitPanel(context: Context, attributeSet: AttributeSet) : ConstraintLayout(context, attributeSet) {
    init {
        View.inflate(context, R.layout.big_unit_space, this)
    }

    var unit: ImperialUnit? = null
    private var isActive = false
    val input: EditText = findViewById(R.id.panel_input)
    private val title: TextView = findViewById(R.id.panel_title)
    private val hint: TextView = findViewById(R.id.panel_hint)
    private val layout: ConstraintLayout = findViewById(R.id.big_unit_space)

    private fun getColor(resourceId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
            val theme = null
            context.resources.getColor(resourceId, theme)
        } else {
            @Suppress("DEPRECATION")
            context.resources.getColor(resourceId)
        }
    }
    private val colorInputSelected = getColor(R.color.inputSelected)
    private val colorInputNormal = getColor(R.color.inputNormal)

    init {
        //input.inputType = InputType.TYPE_NULL // hide keyboard on focus
        input.inputType = InputType.TYPE_CLASS_NUMBER
        deactivate()
    }

    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit
        updateUnitText()
    }

    private fun updateUnitText() {
        unit?.unitName?.name?.let { s ->
            val underlineText = SpannableString(s.toLowerCase().capitalize())
            //underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
            title.text = underlineText
        }
    }

    fun setHintText(newText: String) {
        hint.text = newText
    }

    fun setHighlight(highlight: Boolean) {
        isActive = highlight
        layout.setBackgroundResource(if (highlight) R.drawable.ic_selected_panel_back else R.drawable.ic_input_panel_back)
        input.setTextColor(if (highlight) colorInputSelected else colorInputNormal)
        if (!isActive && hasUnitAssigned() && getString() == "") {
            setUnitValue(0.0)
            updateDisplayValue()
        } else if (isActive && unit?.value?.compareTo(0.0) == 0) {
            setString("")
        }
    }

    fun activate() {
        title.visibility = View.VISIBLE
        input.isEnabled = true
        input.visibility = View.VISIBLE
        hint.visibility = View.INVISIBLE
    }

    fun deactivate() {
        title.visibility = View.INVISIBLE
        input.isEnabled = false
        input.visibility = View.INVISIBLE
        hint.visibility = View.VISIBLE
    }

    fun hasUnitAssigned(): Boolean {
        return title.visibility == View.VISIBLE
    }

    fun getValue(): Double? = unit?.value

    fun setUnitValue(v: Double) {
        unit?.value = v
    }

    fun updateDisplayValue() {
        val v = unit?.value
        input.text = valueForDisplay(v)
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
                if (factor.contains('.')) return
                if (!isValidNumber(factor)) return
                setString(value + c.toString())
            }
            else -> {
                setString(value + c.toString())
            }
        }
    }

    fun dropLastChar() {
        setString(getString().dropLast(1))
    }

    fun evaluateString(s: String? = null) {
        val expression = s ?: getString()
        val value = BasicCalculator(expression).eval()
        setUnitValue(value)
        updateDisplayValue()
    }

    fun makeSerializedString(): String {
        val sep = (DecimalFormat.getInstance() as DecimalFormat).decimalFormatSymbols.groupingSeparator.toString()
        val s = input.text.toString().replace(sep, "")
        val span = input.text.getSpans(0, input.text.length, SuperscriptSpan::class.java).singleOrNull()
        return if (span == null) {
            s
        } else {
            StringBuilder(s).insert(input.text.getSpanStart(span), '^').toString()
        }
    }

    fun hasExponent(): Boolean {
        return input.text.getSpans(0, input.text.length, SuperscriptSpan::class.java).isNotEmpty()
    }

    fun formatStringAndSet(s: String) {
        val expression = s ?: getString()
        val value = BasicCalculator(expression).eval()
        setUnitValue(value)
        input.text = stringForDisplay(expression)
        if (isActive && getString() == "0") {
            input.setText("")
        }
    }
}