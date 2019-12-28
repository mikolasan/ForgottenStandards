package io.github.mikolasan.imperialrussia

import android.text.InputType
import android.widget.EditText
import android.widget.TextView
import android.text.style.UnderlineSpan
import android.text.SpannableString
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout


class ImperialUnitPanel(private val layout: ConstraintLayout) {
    private var unit: ImperialUnit = ImperialUnit(0, ImperialUnitName.MILE, mutableMapOf())
    private val title = layout.findViewById<TextView>(R.id.title)
    private val input = layout.findViewById<EditText>(R.id.input)
    private val hint = layout.findViewById<TextView>(R.id.hint)

    init {
        //input.inputType = InputType.TYPE_NULL // hide keyboard on focus
        input.inputType = InputType.TYPE_CLASS_NUMBER
        title.visibility = View.INVISIBLE
        input.visibility = View.INVISIBLE
        hint.visibility = View.VISIBLE
    }

    fun changeUnit(newUnit: ImperialUnit) {
        unit = newUnit

        val underlineText = SpannableString(title.context.resources.getString(unit.resourceId))
        underlineText.setSpan(UnderlineSpan(), 0, underlineText.length, 0)
        title.text = underlineText
    }

    fun setHighlight(highlight: Boolean) {
        layout.setBackgroundResource(if (highlight) R.drawable.ic_selected_panel_back else R.drawable.ic_input_panel_back)
    }

    fun activate() {
        title.visibility = View.VISIBLE
        input.visibility = View.VISIBLE
        hint.visibility = View.INVISIBLE
    }

    fun deactivate() {
        title.visibility = View.INVISIBLE
        input.visibility = View.INVISIBLE
        hint.visibility = View.VISIBLE
    }
}