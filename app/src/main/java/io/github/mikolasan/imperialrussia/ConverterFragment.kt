package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit

class ConverterFragment : Fragment() {

    lateinit var bottomPanel: ImperialUnitPanel
    lateinit var topPanel: ImperialUnitPanel
    private lateinit var selectedPanel: ImperialUnitPanel
    private lateinit var ratioLabel: TextView

    private fun setListeners(view: View) {

        val topPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                (activity as MainActivity).onPanelsSwapped()
            }
        }
        val bottomPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                (activity as MainActivity).onPanelsSwapped()
            }
        }

        topPanel.setOnClickListener(topPanelOnClickListener)
        bottomPanel.setOnClickListener(bottomPanelOnClickListener)

        val topInput = topPanel.input
        val bottomInput = bottomPanel.input
        topInput.setOnClickListener(topPanelOnClickListener)
        bottomInput.setOnClickListener(bottomPanelOnClickListener)

        topInput.addTextChangedListener(object : TextWatcher {
            var selfEditing = false

            override fun afterTextChanged(s: Editable?) {
                println("[topInput] afterTextChanged ${s.toString()}")
                if (selectedPanel?.input != topInput)
                    return
                if (selfEditing)
                    return

                s?.let {
                    topInput.setSelection(topInput.text.length)
                    val inputValue = BasicCalculator(s.toString()).eval()
                    topPanel.setUnitValue(inputValue)
                    selfEditing = true
                    (activity as MainActivity).onPanelTextChanged(topPanel, s)
                    selfEditing = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("[topInput] beforeTextChanged $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("[topInput] onTextChanged $s")
            }
        })

        bottomInput.addTextChangedListener(object : TextWatcher {
            var selfEditing = false

            override fun afterTextChanged(s: Editable?) {
                println("[bottomInput] afterTextChanged ${s.toString()}")
                if (selectedPanel?.input != bottomInput)
                    return
                if (selfEditing)
                    return

                s?.let {
                    bottomInput.setSelection(bottomInput.text.length)
                    val inputValue = BasicCalculator(s.toString()).eval()
                    bottomPanel.setUnitValue(inputValue)
                    selfEditing = true
                    (activity as MainActivity).onPanelTextChanged(bottomPanel, s)
                    selfEditing = false
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("[bottomInput] beforeTextChanged $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("[bottomInput] onTextChanged $s")
            }
        })

        view.run {
            val digitButtonOnClickListener: (View) -> Unit = { view ->
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
            findViewById<DigitButton>(R.id.digit_1).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_2).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_3).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_4).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_5).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_6).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_7).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_8).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_9).setOnClickListener(digitButtonOnClickListener)
            findViewById<DigitButton>(R.id.digit_0).setOnClickListener(digitButtonOnClickListener)

            val operations = setOf('÷', '×', '+', '-')
            val operationButtonOnClickListener: (View) -> Unit = { button ->
                selectedPanel.let { panel ->
                    if (panel.hasExponent()) {
                        if (button.id != R.id.op_eval) {
                            panel.setUnitValue(0.0)
                            panel.setString("")
                        } else {
                            return@let
                        }
                    }
                    when (button.id) {
                        R.id.op_back -> panel.dropLastChar()
                        R.id.op_clear -> {
                            panel.setUnitValue(0.0)
                            panel.setString("")
                        }
                        R.id.op_mult -> panel.appendString('×', operations)
                        R.id.op_div -> panel.appendString('÷', operations)
                        R.id.op_plus -> panel.appendString('+', operations)
                        R.id.op_minus -> panel.appendString('-', operations)
                        R.id.op_dot -> panel.appendString('.', operations)
                        R.id.op_eval -> panel.evaluateString()
                    }
                }
            }
            findViewById<OperationButton>(R.id.op_back).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_clear).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_mult).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_div).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_plus).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_minus).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_dot).setOnClickListener(operationButtonOnClickListener)
            findViewById<OperationButton>(R.id.op_eval).setOnClickListener(operationButtonOnClickListener)
        }

    }

    private fun updateRatioLabel() {
        val fromUnit = selectedPanel.unit
        val toUnit = if (fromUnit == topPanel.unit) bottomPanel.unit else topPanel.unit
        if (fromUnit == null || toUnit == null) {
            ratioLabel.text = ""
        } else {
            val ratio = findConversionRatio(fromUnit, toUnit)
            val format = "1 ${fromUnit.unitName.name} = [value] ${toUnit.unitName.name}"
            ratioLabel.text = patternForDisplay(format, ratio)
        }
    }

    fun selectPanel(new: ImperialUnitPanel, old: ImperialUnitPanel) {
        selectedPanel = new
        new.setHighlight(true)
        old.setHighlight(false)
        updateRatioLabel()
    }

    fun selectTopPanel() {
        selectPanel(topPanel, bottomPanel)
    }

    fun selectBottomPanel() {
        selectPanel(bottomPanel, topPanel)
    }

    private fun setTopPanel(unit: ImperialUnit, value: Double?) {
        topPanel.changeUnit(unit)
        if (value == null) {
            val topValue = convertValue(bottomPanel.unit, unit, bottomPanel.getValue()
                    ?: 1.0)
            topPanel.setUnitValue(topValue)
        } else {
            topPanel.setUnitValue(value)
        }
        topPanel.updateDisplayValue()

        (activity as MainActivity).onTopPanelUnitChanged(unit)

        updateRatioLabel()
    }

    private fun setBottomPanel(unit: ImperialUnit) {
        bottomPanel.changeUnit(unit)
        val bottomValue = convertValue(topPanel.unit, unit, topPanel.getValue()
                ?: 1.0)
        bottomPanel.setUnitValue(bottomValue)
        bottomPanel.updateDisplayValue()

        (activity as MainActivity).onBottomPanelUnitChanged(unit)

        updateRatioLabel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)
        bottomPanel = view.findViewById<ImperialUnitPanel>(R.id.convert_from)
        topPanel = view.findViewById<ImperialUnitPanel>(R.id.convert_to)
        topPanel.setHintText(view.context.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(view.context.resources.getString(R.string.select_unit_2_hint))
        ratioLabel = view.findViewById<TextView>(R.id.ratio_label)
        setListeners(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).restoreAllValues(this)
    }

    fun restoreTopPanel(unit: ImperialUnit) {
        topPanel.activate()
        topPanel.changeUnit(unit)
    }

    fun restoreBottomPanel(unit: ImperialUnit) {
        bottomPanel.activate()
        bottomPanel.changeUnit(unit)
    }

    fun restoreInputValues(topPanelValue: String, bottomPanelValue: String) {
        topPanel.formatStringAndSet(topPanelValue)
        bottomPanel.formatStringAndSet(bottomPanelValue)
    }

    fun displayUnitValues() {
        topPanel.updateDisplayValue()
        bottomPanel.updateDisplayValue()
    }

    fun onTopPanelClicked() {
        if (selectedPanel != topPanel) {
            selectPanel(topPanel, bottomPanel)
        }
    }

    fun onBottomPanelClicked() {
        if (selectedPanel != topPanel) {
            selectPanel(bottomPanel, topPanel)
        }
    }

    fun onUnitSelected(oldUnit: ImperialUnit, newUnit: ImperialUnit) {
//        if (!topPanel.hasUnitAssigned()) {
//            topPanel.activate()
//            setTopPanel(unit, 1.0)
//            selectPanel(topPanel, bottomPanel)
//        } else if (!bottomPanel.hasUnitAssigned() && topPanel.unit != unit) {
//            bottomPanel.activate()
//            setBottomPanel(unit)
//            selectPanel(bottomPanel, topPanel)
//        } else {

            if (oldUnit == topPanel.unit) {
                if (bottomPanel.unit != newUnit) {
                    setTopPanel(newUnit, null)
                } else if (topPanel.unit != newUnit) {
                    selectPanel(bottomPanel, topPanel)
                    (activity as MainActivity).onPanelsSwapped()
                }
            } else if (oldUnit == bottomPanel.unit) {
                if (topPanel.unit != newUnit) {
                    setBottomPanel(newUnit)
                } else if (bottomPanel.unit != newUnit){
                    selectPanel(topPanel, bottomPanel)
                    (activity as MainActivity).onPanelsSwapped()
                }
            }

//        }
    }

    fun swapPanels() {
        val topUnit = topPanel.unit ?: return
        val bottomUnit = bottomPanel.unit ?: return
        val topValue = topPanel.getValue() ?: return
        val bottomValue = bottomPanel.getValue() ?: return
        val topString = topPanel.getString()
        val bottomString = bottomPanel.getString()
        topPanel.changeUnit(bottomUnit)
        bottomPanel.changeUnit(topUnit)

        topPanel.setUnitValue(bottomValue)
        bottomPanel.setUnitValue(topValue)

        topPanel.setString(bottomString)
        bottomPanel.setString(topString)

        updateRatioLabel()
    }
}