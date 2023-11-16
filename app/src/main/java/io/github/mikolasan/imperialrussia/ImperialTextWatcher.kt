package io.github.mikolasan.imperialrussia

import android.text.Editable
import android.text.TextWatcher

open class ImperialTextWatcher(val panel: ImperialUnitPanel, val fragment: ConverterFragment, val activity: MainActivity) : TextWatcher {
    var selfEditing = false
    val input = panel.input

    override fun afterTextChanged(s: Editable?) {
        println("[topInput] afterTextChanged ${s.toString()}")
        if (fragment.selectedPanel?.input != input)
            return
        if (selfEditing)
            return

        s?.let {
            input.setSelection(input.text.length)
            val inputValue = BasicCalculator(s.toString()).eval()
            panel.setUnitValue(inputValue)
            selfEditing = true
            activity.onPanelTextChanged(panel, s)
            selfEditing = false
        }
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) { }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) { }
}