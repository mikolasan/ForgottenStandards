package xyz.neupokoev.forgottenstandards.converter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.getConversionRatio
import xyz.neupokoev.forgottenstandards.patternForDisplay

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

//        keyboardView = view.findViewById(R.id.keyboard)
//        keyboardButtonView = view.findViewById(R.id.keyboard_button)


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

//        keyboardFragment = keyboardView.getFragment()
//        keyboardButtonFragment = keyboardButtonView.getFragment()

        (activity as? MainActivity)?.workingUnits?.let { workingUnits ->
            restoreTopPanel(workingUnits.mainUnit)
            //restoreBottomPanel(workingUnits.bottomUnit)
            selectPanel(topPanel, bottomPanel)
            displayUnitValues()
        }

//        (activity as? MainActivity)?.updateKeyboard()
    }

    private fun selectPanel(new: ImperialUnitPanel, old: ImperialUnitPanel) {
        selectedPanel = new
        new.setHighlight(true)
        old.setHighlight(false)
        updateRatioLabel()
    }

    private fun restoreTopPanel(unit: ImperialUnit) {
        topPanel.activate()
        topPanel.changeUnit(unit)
    }

    private fun displayUnitValues() {
        topPanel.updateDisplayValue()
        bottomPanel.updateDisplayValue()
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