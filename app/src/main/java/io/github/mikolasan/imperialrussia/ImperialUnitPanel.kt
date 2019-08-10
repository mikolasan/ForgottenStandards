package io.github.mikolasan.imperialrussia

import android.widget.EditText
import android.widget.TextView

class ImperialUnitPanel(val title: TextView, val input: EditText, var unit: ImperialUnit) {
    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit
        title.text = unit.name
    }
}