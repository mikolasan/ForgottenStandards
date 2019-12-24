package io.github.mikolasan.imperialrussia

import android.widget.EditText
import android.widget.TextView
import android.text.style.UnderlineSpan
import android.text.SpannableString
import androidx.constraintlayout.widget.ConstraintLayout


class ImperialUnitPanel(val layout: ConstraintLayout, val title: TextView, val input: EditText) {
    private var unit: ImperialUnit = ImperialUnit(0, ImperialUnitName.MILE, mutableMapOf())

    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit

        val underlineText = SpannableString(title.context.resources.getString(unit.resourceId))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
        title.text = underlineText
    }

    fun setHighlight(highlight: Boolean) {
        layout.setBackgroundResource(if (highlight) R.drawable.ic_selected_panel_back else R.drawable.ic_input_panel_back)
    }
}