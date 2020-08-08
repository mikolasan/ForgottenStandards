package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatButton

class OperationButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    private val operations = setOf('÷', '×', '+', '-')

    fun setOnClickPanel(selectedPanel: ImperialUnitPanel) {
        setOnClickListener{ button ->
            selectedPanel.let { panel ->
                if (panel.hasExponent()) {
                    if (button.id != R.id.op_eval) {
                        panel.setUnitValue(0.0)
                        panel.setString("")
                    } else {
                        return@let
                    }
                }
                when (button.id) {
                    R.id.op_back -> panel.dropLastChar()
                    R.id.op_clear -> {
                        panel.setUnitValue(0.0)
                        panel.setString("")
                    }
                    R.id.op_mult -> panel.appendString('×', operations)
                    R.id.op_div -> panel.appendString('÷', operations)
                    R.id.op_plus -> panel.appendString('+', operations)
                    R.id.op_minus -> panel.appendString('-', operations)
                    R.id.op_dot -> panel.appendString('.', operations)
                    R.id.op_eval -> panel.evaluateString()
                }
            }
        }
    }
}