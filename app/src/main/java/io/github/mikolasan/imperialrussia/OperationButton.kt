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

    fun setOnClickPanel(observer: ImperialUnitObserver) {
        setOnClickListener{ button ->
//            if (button.id != R.id.op_eval) {
//                observer.setValueAndNotify(0.0)
//            }
            when (button.id) {
                R.id.op_back -> observer.dropLastChar()
                R.id.op_clear -> {
                    observer.setValueAndNotify(0.0)
                }

                R.id.op_mult -> observer.appendStringOrReplace('×', operations)
                R.id.op_div -> observer.appendStringOrReplace('÷', operations)
                R.id.op_plus -> observer.appendStringOrReplace('+', operations)
                R.id.op_minus -> observer.appendStringOrReplace('-', operations)
                R.id.op_dot -> observer.appendStringOrReplace('.', operations)
//                R.id.op_eval -> if (panel.hasExponent()) panel.evaluateString()
            }
        }
    }
}