package xyz.neupokoev.forgottenstandards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import xyz.neupokoev.forgottenstandards.converter.DigitButton
import xyz.neupokoev.forgottenstandards.converter.ImperialUnitObserver
import xyz.neupokoev.forgottenstandards.converter.OperationButton

class KeyboardFragment : Fragment() {
    var observer: ImperialUnitObserver? = null
    private var root: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val context = requireContext()
        val frameLayout = FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
        root = frameLayout
        refreshLayout()
        return frameLayout
    }

    fun updateLayout() {
        refreshLayout()
    }

    private fun refreshLayout() {
        val frameLayout = root ?: return
        frameLayout.removeAllViews()
        
        val unitName = observer?.unit?.unitName
        val layoutId = if (unitName == ImperialUnitName.BEAUFORT) {
            R.layout.keyboard_beaufort
        } else {
            R.layout.keyboard_panel
        }
        
        val view = layoutInflater.inflate(layoutId, frameLayout, false)
        frameLayout.addView(view)
        setButtonListeners(view, unitName == ImperialUnitName.BEAUFORT)
    }

    private fun setButtonListeners(view: View, isBeaufort: Boolean) {
        val obs = observer ?: return
        if (isBeaufort) {
            setBeaufortButtonListeners(view, obs)
        } else {
            setStandardButtonListeners(view, obs)
        }
    }

    private fun setBeaufortButtonListeners(view: View, observer: ImperialUnitObserver) {
        val digitIds = listOf(
            R.id.digit_0, R.id.digit_1, R.id.digit_2, R.id.digit_3,
            R.id.digit_4, R.id.digit_5, R.id.digit_6, R.id.digit_7,
            R.id.digit_8, R.id.digit_9, R.id.digit_10, R.id.digit_11, R.id.digit_12
        )
        for (id in digitIds) {
            view.findViewById<Button>(id)?.setOnClickListener { v ->
                val btn = v as Button
                val value = btn.text.toString().toDoubleOrNull() ?: 0.0
                observer.setValueAndNotify(value)
            }
        }
        view.findViewById<View>(R.id.op_back)?.setOnClickListener {
            observer.dropLastChar()
        }
        view.findViewById<View>(R.id.op_clear)?.setOnClickListener {
            observer.setValueAndNotify(0.0)
        }
        view.findViewById<View>(R.id.hide_keyboard)?.setOnClickListener {
            (activity as MainActivity).showKeyboardButton()
        }
    }

    private fun setStandardButtonListeners(view: View, observer: ImperialUnitObserver) {
        view.run {
            findViewById<DigitButton>(R.id.digit_7)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_8)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_9)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_4)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_5)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_6)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_1)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_2)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_3)?.setOnClickPanel(observer)
            findViewById<DigitButton>(R.id.digit_0)?.setOnClickPanel(observer)

            findViewById<OperationButton>(R.id.op_back)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_clear)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_mult)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_div)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_plus)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_minus)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_dot)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.op_eval)?.setOnClickPanel(observer)
            findViewById<OperationButton>(R.id.hide_keyboard)?.setOnClickListener {
                (activity as MainActivity).showKeyboardButton()
            }
        }
    }
}