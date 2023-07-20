package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class KeyboardFragment : Fragment() {
    var selectedPanel: ImperialUnitPanel? = null

    private fun setKeyboardButtonListeners(view: View) {
        val fragment = this
        view.run {
            findViewById<DigitButton>(R.id.digit_1).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_2).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_3).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_4).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_5).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_6).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_7).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_8).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_9).setOnClickPanel(fragment)
            findViewById<DigitButton>(R.id.digit_0).setOnClickPanel(fragment)

            findViewById<OperationButton>(R.id.op_back).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_clear).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_mult).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_div).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_plus).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_minus).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_dot).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.op_eval).setOnClickPanel(fragment)
            findViewById<OperationButton>(R.id.hide_keyboard).setOnClickListener { button ->

            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.keyboard_panel, container, false)
        return view
    }
}