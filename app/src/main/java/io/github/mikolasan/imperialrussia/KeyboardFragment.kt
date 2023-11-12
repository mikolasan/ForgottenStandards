package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class KeyboardFragment : Fragment() {
    var selectedPanel: ImperialUnitPanel? = null
    var observer: ImperialUnitObserver? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.keyboard_panel, container, false)
        setButtonListeners(view)
        return view
    }

    override fun onStart() {
        super.onStart()
        (activity as MainActivity).onKeyboardConnected(this)
    }


    private fun setButtonListeners(view: View) {
        observer?.let {observer ->
            view.run {
                findViewById<DigitButton>(R.id.digit_2).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_1).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_3).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_4).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_5).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_6).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_7).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_8).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_9).setOnClickPanel(observer)
                findViewById<DigitButton>(R.id.digit_0).setOnClickPanel(observer)

                findViewById<OperationButton>(R.id.op_back).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_clear).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_mult).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_div).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_plus).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_minus).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_dot).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.op_eval).setOnClickPanel(observer)
                findViewById<OperationButton>(R.id.hide_keyboard).setOnClickListener {
                    (activity as MainActivity).showKeyboardButton()
                }
            }
        }
    }
}