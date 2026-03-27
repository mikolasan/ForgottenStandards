package xyz.neupokoev.forgottenstandards.converter

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.appcompat.widget.AppCompatButton
import xyz.neupokoev.forgottenstandards.maxDisplayLength

/**
 * Very simple extension to a standard button with very specific usage of ImperialUnitPanel
 * when the button is pressed. Normally it just adds symbol depicted on the button to the panel,
 * except one case when the panel displays a value formatted in scientific notation (1.234x10^4).
 */
class DigitButton(context: Context, attributeSet: AttributeSet) : AppCompatButton(context, attributeSet) {
    fun setOnClickPanel(observer: ImperialUnitObserver) {
        setOnClickListener{ view ->
            val button = view as Button
            val textToAppend = button.text.toString()
            
            if (textToAppend.length > maxDisplayLength) {
                return@setOnClickListener
            }

            // If it's a single digit, append it normally.
            // If it's multiple digits (like "10", "11", "12" for Beaufort), 
            // it might mean we want to SET the value instead of appending.
            // But the observer appendString/appendStringOrReplace logic is based on characters.
            
            // For Beaufort, we actually set the value directly in KeyboardFragment 
            // using standard Button listener if I kept it that way.
            // But I used DigitButton in the xml.
            
            // Let's make DigitButton smarter: if it's more than 1 char, it should probably call setString or similar.
            if (textToAppend.length > 1) {
                observer.setValueAndNotify(textToAppend.toDoubleOrNull() ?: 0.0)
            } else {
                observer.appendString(textToAppend[0])
            }
        }
    }
}