package io.github.mikolasan.imperialrussia

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton

class DigitButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    fun setOnClickPanel(selectedPanel: ImperialUnitPanel) {
        setOnClickListener{ view ->
            val button = view as Button

            selectedPanel.let { panel ->
                if (!panel.hasUnitAssigned()) return@let
                if (panel.hasExponent()) {
                    panel.setUnitValue(0.0)
                    panel.setString("")
                }
                val text = panel.getString() ?: ""
                if (text.length <= maxDisplayLength) {
                    panel.appendString(button.text[0])
                }
            }
        }
    }
}