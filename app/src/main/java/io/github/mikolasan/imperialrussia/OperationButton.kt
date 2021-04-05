package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatButton

/**
 * Yet another simple extension to a standard button with very specific usage of ImperialUnitPanel
 * when the button is pressed. The button adds an operation symbol or evaluates an expression.
 * If value is already formatted in scientific notation, then it resets the value.
 */
class OperationButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    private val operations = setOf('÷', '×', '+', '-')

    fun setOnClickPanel(fragment: ConverterFragment) {
        setOnClickListener{ button ->
            val panel: ImperialUnitPanel = fragment.selectedPanel
            if (button.id != R.id.op_eval && panel.hasExponent()) {
                panel.setUnitValue(0.0)
                panel.setString("")
            }
            when (button.id) {
                R.id.op_back -> panel.dropLastChar()
                R.id.op_clear -> {
                    panel.setUnitValue(0.0)
                    panel.setString("")
                }
                R.id.op_mult -> panel.appendStringOrReplace('×', operations)
                R.id.op_div -> panel.appendStringOrReplace('÷', operations)
                R.id.op_plus -> panel.appendStringOrReplace('+', operations)
                R.id.op_minus -> panel.appendStringOrReplace('-', operations)
                R.id.op_dot -> panel.appendStringOrReplace('.', operations)
                R.id.op_eval -> if (panel.hasExponent()) panel.evaluateString()
            }
        }
    }
}