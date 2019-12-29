package io.github.mikolasan.imperialrussia

import android.icu.text.Replaceable
import android.os.Build
import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout


class ImperialUnitPanel(private val layout: ConstraintLayout) {
    var unit: ImperialUnit = ImperialUnit(0, ImperialUnitName.MILE, mutableMapOf())
    private val title: TextView = layout.findViewById(R.id.title)
    val input: EditText = layout.findViewById(R.id.input)
    private val hint: TextView = layout.findViewById(R.id.hint)

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

        val underlineText = SpannableString(title.context.resources.getString(unit.resourceId))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
        title.text = underlineText
        activate()
    }

    fun setHighlight(highlight: Boolean) {
        layout.setBackgroundResource(if (highlight) R.drawable.ic_selected_panel_back else R.drawable.ic_input_panel_back)
        input.setTextColor(if (highlight) colorInputSelected else colorInputNormal)
    }

    fun activate() {
        title.visibility = View.VISIBLE
        input.visibility = View.VISIBLE
        hint.visibility = View.INVISIBLE
    }

    fun deactivate() {
        title.visibility = View.INVISIBLE
        input.visibility = View.INVISIBLE
        hint.visibility = View.VISIBLE
    }

    fun isActivated(): Boolean {
        return title.visibility == View.VISIBLE
    }

    fun getValue(): Double {
        return input.text.toString().toDouble()
    }

    fun setValue(v: Double) {
        input.setText(valueForDisplay(v))
    }

    fun getString(): String {
        return input.text.toString()
    }

    fun setString(s: String) {
        input.setText(s)
    }

    fun appendString(c: Char, replaceable: Set<Char>? = null) {
        if (replaceable != null) {
            var value = getString()
            if (replaceable.contains(value.last()))
                value = value.dropLast(1)
            setString(value + c.toString())
        } else {
            setString(getString() + c.toString())
        }
    }

    fun dropLastChar() {
        setString(getString().dropLast(1))
    }

    fun evaluateString() {
        val value = getString()
        val calculator = BasicCalculator(value)
        setString(calculator.eval().toString())
    }
}