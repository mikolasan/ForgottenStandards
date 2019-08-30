package io.github.mikolasan.imperialrussia

import android.widget.EditText
import android.widget.TextView
import android.text.style.UnderlineSpan
import android.text.SpannableString



class ImperialUnitPanel(val title: TextView, val input: EditText, var unit: ImperialUnit) {
    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit
        val underlineText = SpannableString(unit.name)
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
        title.text = underlineText
    }
}