package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton

class OperationButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    private val operations = setOf('÷', '×', '+', '-')

    fun setOnClickPanel(panel: ImperialUnitPanel) {
        setOnClickListener{ button ->
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