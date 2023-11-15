package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit

class ConverterFragment : Fragment() {

    lateinit var bottomPanel: ImperialUnitPanel
    lateinit var topPanel: ImperialUnitPanel
    lateinit var selectedPanel: ImperialUnitPanel
    private lateinit var ratioLabel: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_converter, container, false)
        bottomPanel = view.findViewById(R.id.convert_from)
        topPanel = view.findViewById(R.id.convert_to)
        topPanel.setHintText(view.context.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(view.context.resources.getString(R.string.select_unit_2_hint))
        ratioLabel = view.findViewById(R.id.ratio_label)
        selectedPanel = topPanel // init before use
        setPanelListeners(view)

//        arguments?.let {
//            val categoryName = it.getString("category")
//            val topUnitName = it.getString("topUnit")
//            val bottomUnitName = it.getString("bottomUnit")
//            val topUnit =
//        }
//        setKeyboardButtonListeners(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        (activity as? MainActivity)?.workingUnits?.let { workingUnits ->
            restoreTopPanel(workingUnits.topUnit)
            restoreBottomPanel(workingUnits.bottomUnit)
            selectPanel(topPanel, bottomPanel)
            displayUnitValues()
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

        (activity as? MainActivity)?.onBottomPanelUnitChanged(unit)

        updateRatioLabel()
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

        val selectedUnit = selectedPanel.unit ?: return
        selectedPanel = if (topUnit == selectedUnit) {
            bottomPanel
        } else {
            topPanel
        }

        topPanel.changeUnit(bottomUnit)
        bottomPanel.changeUnit(topUnit)

        topPanel.setUnitValue(bottomValue)
        bottomPanel.setUnitValue(topValue)

        topPanel.setString(bottomString)
        bottomPanel.setString(topString)

        updateRatioLabel()
    }

    private fun setPanelListeners(view: View) {

        val topPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                (activity as MainActivity).onPanelSelected(selectedPanel)
            }
        }
        val bottomPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                (activity as MainActivity).onPanelSelected(selectedPanel)
            }
        }

        topPanel.setOnClickListener(topPanelOnClickListener)
        bottomPanel.setOnClickListener(bottomPanelOnClickListener)

        val topInput = topPanel.input
        val bottomInput = bottomPanel.input
        topInput.setOnClickListener(topPanelOnClickListener)
        bottomInput.setOnClickListener(bottomPanelOnClickListener)
        topInput.addTextChangedListener(object : ImperialTextWatcher(topPanel, this, activity as MainActivity) {})
        bottomInput.addTextChangedListener(object : ImperialTextWatcher(bottomPanel, this, activity as MainActivity) {})
    }

    private fun updateRatioLabel() {
        val fromUnit = selectedPanel.unit
        val toUnit = if (fromUnit == topPanel.unit) bottomPanel.unit else topPanel.unit
        if (fromUnit == null || toUnit == null) {
            ratioLabel.text = ""
        } else {
            val ratio = getConversionRatio(fromUnit, toUnit)
            val format = "1 ${fromUnit.unitName.name} = [value] ${toUnit.unitName.name}"
            ratioLabel.text = patternForDisplay(format, ratio)
        }
    }

}