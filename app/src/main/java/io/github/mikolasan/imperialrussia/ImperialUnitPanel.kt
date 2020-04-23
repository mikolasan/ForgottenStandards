package io.github.mikolasan.imperialrussia

import android.os.Build
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import android.text.SpannableString
import android.text.style.SubscriptSpan
import android.text.style.SuperscriptSpan
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.slide_area.view.*


class ImperialUnitPanel(private val layout: ConstraintLayout) {
    var unit: ImperialUnit? = null
    var isSelected = false
    val input: EditText = layout.findViewById(R.id.panel_input)
    private val title: TextView = layout.findViewById(R.id.panel_title)
    private val hint: TextView = layout.findViewById(R.id.panel_hint)

    private fun getColor(resourceId: Int): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
            val theme = null
            layout.context.resources.getColor(resourceId, theme)
        } else {
            @Suppress("DEPRECATION")
            layout.context.resources.getColor(resourceId)
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
        val resourceId = unit?.resourceId
        if (resourceId != null) {
            val underlineText = SpannableString(title.context.resources.getString(resourceId))
            //underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
            title.text = underlineText
        }
    }

    fun setHintText(newText: String) {
        hint.text = newText
    }

    fun setHighlight(highlight: Boolean) {
        isSelected = highlight
        layout.setBackgroundResource(if (highlight) R.drawable.ic_selected_panel_back else R.drawable.ic_input_panel_back)
        input.setTextColor(if (highlight) colorInputSelected else colorInputNormal)
        if (!isSelected && isActivated() && getString() == "") {
            setUnitValue(0.0)
            updateDisplayValue()
        } else if (isSelected && unit?.value?.compareTo(0.0) == 0) {
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

    fun isActivated(): Boolean {
        return title.visibility == View.VISIBLE
    }

    fun getValue(): Double? = unit?.value

    fun setUnitValue(v: Double?) {
        unit?.value = v
    }

    fun updateDisplayValue() {
        val v = unit?.value
        input.text = valueForDisplay(v)
        if (isSelected && getString() == "0") {
            input.setText("")
        }
    }

    fun getString(): String {
        return input.text.toString()
    }

    fun setString(s: String) {
        if (isSelected && s == "0") {
            input.setText("")
        } else {
            input.setText(s)
        }
    }

    fun appendString(c: Char, replaceable: Set<Char>? = null) {
        if (replaceable != null) {
            val value = getString()
            if (value.isNotEmpty()) {
                if (replaceable.containsAll(listOf(value.last(), c))) {
                    setString(value.dropLast(1) + c.toString())
                } else if (c == '.') {
                    val factor = value.takeLastWhile { char ->
                        char in '0'..'9' || char == '.'
                    }
                    if (factor.isEmpty()) {
                        setString(value + c.toString())
                        return
                    }
                    if (factor.contains('.')) return
                    try {
                        val x = factor.toDouble()
                    } catch (e: NumberFormatException) {
                        return
                    }
                    setString(value + c.toString())
                } else {
                    setString(value + c.toString())
                }
            } else {
                setString(c.toString())
            }
        } else {
            setString(getString() + c.toString())
        }
    }

    fun dropLastChar() {
        setString(getString().dropLast(1))
    }

    fun evaluateString() {
        val expression = getString()
        val value = BasicCalculator(expression).eval()
        setUnitValue(value)
        updateDisplayValue()
    }

    fun makeSerializedString(): String {
//        val spans = input.text.getSpans(0, input.text.length, SubscriptSpan::class.java))
        return getString()
    }

    fun hasExponent(): Boolean {
        return input.text.getSpans(0, input.text.length, SuperscriptSpan::class.java).isNotEmpty()
    }
}