package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton

/**
 * Very simple extension to a standard button with very specific usage of ImperialUnitPanel
 * when the button is pressed. Normally it just adds symbol depicted on the button to the panel,
 * except one case when the panel displays a value formatted in scientific notation (1.234x10^4).
 */
class DigitButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    fun setOnClickPanel(observer: ImperialUnitObserver) {
        setOnClickListener{ view ->
            if (text.length > maxDisplayLength) {
                return@setOnClickListener
            }

            val button = view as Button
            val char = button.text[0]
            observer.appendString(char)

//            fragment.selectedPanel?.let { panel ->
//                if (!panel.hasUnitAssigned()) return@setOnClickListener
//                if (panel.hasExponent()) {
//                    panel.setUnitValue(0.0)
//                    panel.setString("")
//                }
//                val text = panel.getString() ?: ""
//                val button = view as Button
//                if (text.length <= maxDisplayLength) {
//                    panel.appendString(button.text[0])
//                }
//            }
        }
    }
}