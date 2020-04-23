package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.Transition
import android.view.View
import android.widget.*
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*



class MeasuringActivity : Activity() {

    private val languageSetting = "language"
    private val preferencesFile = "ImperialRussiaPref.3"
    private var newLocale: Locale? = null

    private var selectedPanel: ImperialUnitPanel? = null
    private lateinit var unitsList: ListView

    enum class UISide {
        floating,
        list,
        input
    }
    var uiSide = UISide.list

    val bottomPanel by lazy {
        val bottomLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)
        ImperialUnitPanel(bottomLayout)
    }
    val topPanel by lazy {
        val topLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        ImperialUnitPanel(topLayout)
    }
    val listAdapter by lazy {
        createListAdapter(applicationContext)
    }
    val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy getPreferences().edit()
    }

    private fun getPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
    }

    // TODO:
    private fun createListAdapter(context: Context): ImperialListAdapter {
        val orderedUnits = restoreOrderedUnits()
        val adapter = ImperialListAdapter(context, orderedUnits)
        adapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            Toast.makeText(context, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { _, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
        }
        adapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            Toast.makeText(context, "'${unit.unitName.name}' has been moved to the top + scroll", Toast.LENGTH_SHORT).show()
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { _, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
            unitsList.setSelectionAfterHeaderView()
        }
        return adapter
    }

    private fun restoreUnit(storedName: String): ImperialUnit? {
        if (storedName != "") {
            val unitName = ImperialUnitName.valueOf(storedName)
            return LengthUnits.imperialUnits[unitName]
        }
        return null
    }

    private fun createNewActivity() {
        setListeners()
        restorePanels()
        //applyLanguageSettings()
    }

    private fun applyLanguageSettings() {
//        val prefs = getPreferences()
//        var currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
//        val editor = prefs.edit()
//
//        val languageButton = findViewById<Button>(R.id.language)
//
//        languageButton.text = currentLang
//        languageButton.setOnClickListener { view ->
//            val btn = view as Button
//            if (currentLang == "ru") {
//                currentLang = "en"
//            } else if (currentLang == "en") {
//                currentLang = "ru"
//            }
//
//            newLocale = Locale(currentLang)
//            Locale.setDefault(newLocale!!)
//            btn.text = currentLang
//            editor.putString(languageSetting, currentLang)
//            editor.apply()
//
//            val config = resources.configuration
//            config.locale = newLocale
//            resources.updateConfiguration(config, resources.displayMetrics)
//
//
//            listAdapter.notifyDataSetChanged()
//            bottomPanel.updateUnitText()
//            topPanel.updateUnitText()
//        }
    }

    private fun restoreOrderedUnits(): MutableList<ImperialUnit> {
        val preferences = getPreferences()

        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")

        val orderedUnits = LengthUnits.lengthUnits.toMutableList()
        LengthUnits.lengthUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            var p = preferences.getInt(settingName, i)
            if (p < 0) {
                System.err.println("Shit: pos ${i}, unit ${unitName} got ${p}")
                p = i
            }
            if (!preferences.contains(settingName)) {
                preferencesEditor.putInt(settingName, i)
            }
            orderedUnits[p] = u
        }
        preferencesEditor.apply()
        bottomPanelUnit?.let {
            if (orderedUnits[1] != it) {
                orderedUnits.moveToFront(it)
            }
        }
        topPanelUnit?.let {
            if (orderedUnits[1] != it) {
                orderedUnits.moveToFront(it)
            }
        }

        return orderedUnits
    }

    private fun restorePanels() {
        restoreSelectedUnits()

        val preferences = getPreferences()
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")
        val topPanelValue = BasicCalculator(preferences.getString("topPanelValue", "") ?: "").eval()
        val bottomPanelValue = preferences.getString("bottomPanelValue", "") ?: ""
        topPanel.setUnitValue(topPanelValue)
        topPanel.updateDisplayValue()
        listAdapter.updateAllValues(topPanel.unit, topPanelValue)
        bottomPanelUnit?.let { unit ->
            val bottomValue = convertValue(topPanelUnit, unit, topPanelValue)
            bottomPanel.updateDisplayValue()
            val displayValue = bottomPanel.getString()
            if (bottomPanelValue != displayValue) {
                preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
                preferencesEditor.apply()
            }
        }

        selectPanel(topPanel, bottomPanel)
    }

    private fun restoreSelectedUnits() {
        val preferences = getPreferences()
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")
        topPanelUnit?.let {
            topPanel.activate()
            topPanel.changeUnit(it)
            listAdapter.setSelectedUnit(it)
        }
        bottomPanelUnit?.let {
            bottomPanel.activate()
            bottomPanel.changeUnit(it)
            listAdapter.setSecondUnit(it)
        }
    }

    private fun restoreInputValues() {
        val preferences = getPreferences()
        val topPanelValue = preferences.getString("topPanelValue", "") ?: ""
        val bottomPanelValue = preferences.getString("bottomPanelValue", "") ?: ""
        topPanel.setString(topPanelValue)
        bottomPanel.setString(bottomPanelValue)
    }

    private fun updateRatioLabel() {
        val ratioLabel = findViewById<TextView>(R.id.ratio_label)
        val fromUnit = selectedPanel?.unit
        val toUnit = if (fromUnit == topPanel.unit) bottomPanel.unit else topPanel.unit
        if (fromUnit == null || toUnit == null) {
            ratioLabel.text = ""
        } else {
            val ratio = findConversionRatio(fromUnit, toUnit)
            val format = "1 ${fromUnit.unitName.name} = [value] ${toUnit.unitName.name}"
            ratioLabel.text = formatForDisplay(format, ratio)
        }
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

        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(bottomPanel.unit)
        listAdapter.notifyDataSetChanged()

        preferencesEditor.putString("topPanelUnit", unit.unitName.name)
        preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
        preferencesEditor.apply()
        updateRatioLabel()
    }

    private fun setBottomPanel(unit: ImperialUnit) {
        bottomPanel.changeUnit(unit)
        val bottomValue = convertValue(topPanel.unit, unit, topPanel.getValue()
                ?: 1.0)
        bottomPanel.setUnitValue(bottomValue)
        bottomPanel.updateDisplayValue()

        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(topPanel.unit)
        listAdapter.notifyDataSetChanged()

        preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
        preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
        preferencesEditor.apply()
        updateRatioLabel()
    }

    private fun swapPanels() {
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

        listAdapter.swapSelection()
        updateRatioLabel()
    }

    private fun selectPanel(new: ImperialUnitPanel?, old: ImperialUnitPanel?) {
        selectedPanel = new
        new?.setHighlight(true)
        old?.setHighlight(false)
        updateRatioLabel()
    }


    private fun setListeners() {
        unitsList.setOnItemClickListener { _, _, position, _ ->
            val unit = listAdapter.getItem(position) as ImperialUnit
            if (!topPanel.isActivated()) {
                topPanel.activate()
                setTopPanel(unit, 1.0)
                selectPanel(topPanel, bottomPanel)
            } else if (!bottomPanel.isActivated() && topPanel.unit != unit) {
                bottomPanel.activate()
                setBottomPanel(unit)
                selectPanel(bottomPanel, topPanel)
            } else {
                if (selectedPanel == topPanel) {
                    if (bottomPanel.unit != unit) {
                        setTopPanel(unit, null)
                    } else if (topPanel.unit != unit) {
                        selectPanel(bottomPanel, topPanel)
                        listAdapter.swapSelection()
                        listAdapter.notifyDataSetChanged()
                    }
                } else if (selectedPanel == bottomPanel) {
                    if (topPanel.unit != unit) {
                        setBottomPanel(unit)
                    } else if (bottomPanel.unit != unit){
                        selectPanel(topPanel, bottomPanel)
                        listAdapter.swapSelection()
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val topPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                listAdapter.swapSelection()
                listAdapter.notifyDataSetChanged()
            }
        }
        val bottomPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                listAdapter.swapSelection()
                listAdapter.notifyDataSetChanged()
            }
        }
        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)


        (mainLayout as? MotionLayout)?.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, triggerId: Int, positive: Boolean, progress: Float) {}

            override fun onTransitionStarted(motionLayout: MotionLayout?, startId: Int, endId: Int) {
//                uiSide = UISide.floating
//                println("onTransitionCompleted ${uiSide.name}")
            }

            override fun onTransitionChange(motionLayout: MotionLayout?, startId: Int, endId: Int, progress: Float) {
//                println("onTransitionChange")
//                if (progress == 0.0f) {
//                    uiSide = if (startId == R.id.show_input_constraint) UISide.input else UISide.list
//                } else if (progress == 1.0f) {
//                    uiSide = if (startId == R.id.show_input_constraint) UISide.list else UISide.input
//                }
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                uiSide = if (currentId == R.id.show_input_constraint) UISide.input else UISide.list
                println("onTransitionCompleted ${uiSide.name}")
            }

        })

        val topLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        val bottomLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)

        val topInput = topPanel.input
        val bottomInput = bottomPanel.input
        topLayout.setOnClickListener(topPanelOnClickListener)
        bottomLayout.setOnClickListener(bottomPanelOnClickListener)
        topInput.setOnClickListener(topPanelOnClickListener)
        bottomInput.setOnClickListener(bottomPanelOnClickListener)
        topInput.addTextChangedListener(object : TextWatcher {
            var selfEditing = false

            override fun afterTextChanged(s: Editable?) {
                println("[topInput] afterTextChanged ${s.toString()}")
                if (selectedPanel?.input != topInput)
                    return

                if (mainLayout is MotionLayout && uiSide == UISide.list)
                    return

                if (selfEditing)
                    return

                s?.let {
                    topInput.setSelection(topInput.text.length)
                    val inputValue = BasicCalculator(s.toString()).eval()
                    topPanel.setUnitValue(inputValue)
//                    selfEditing = true
//                    topPanel.evaluateString(s.toString())
//                    selfEditing = false
                    listAdapter.updateAllValues(topPanel.unit, topPanel.unit?.value ?: 0.0)
                    if (bottomPanel.isActivated()) {
                        bottomPanel.updateDisplayValue()
                        preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
                    }
                    preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
                    preferencesEditor.apply()
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

                if (mainLayout is MotionLayout && uiSide == UISide.list)
                    return
                
                if (selfEditing)
                    return
                
                s?.let {
                    bottomInput.setSelection(bottomInput.text.length)
                    val inputValue = BasicCalculator(s.toString()).eval()
                    bottomPanel.setUnitValue(inputValue)
//                    selfEditing = true
//                    topPanel.evaluateString(s.toString())
//                    selfEditing = false
                    listAdapter.updateAllValues(bottomPanel.unit, bottomPanel.unit?.value ?: 0.0)
                    if (topPanel.isActivated()) {
                        topPanel.updateDisplayValue()
                        preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
                    }
                    preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                println("[bottomInput] beforeTextChanged $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                println("[bottomInput] onTextChanged $s")
            }
        })

        val digitButtonOnClickListener: (View) -> Unit = { view ->
            val button = view as Button

            selectedPanel?.let { panel ->
                if (!panel.isActivated()) return@let
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
            selectedPanel?.let { panel ->
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

    private fun recreatePreviousActivity(savedInstanceState: Bundle) {
        setListeners()
        restoreSelectedUnits()

        listAdapter.notifyDataSetChanged()
        selectPanel(topPanel, bottomPanel)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)
        topPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_2_hint))

        unitsList = findViewById(R.id.units_list)
        unitsList.adapter = listAdapter

        if (savedInstanceState == null) {
            createNewActivity()
        } else {
            recreatePreviousActivity(savedInstanceState)
        }
    }

//    override fun onResume() {
//        super.onResume()
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // onRestoreInstanceState works against me. I include layouts and included layouts do not have
        // unique ids. And it calls TextChangedListener on my inputs with wrong values.
        //super.onRestoreInstanceState(savedInstanceState)
        restoreInputValues()
    }

    override fun onStart() {
        super.onStart()

        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)
        if (mainLayout is MotionLayout && topPanel.isActivated() && bottomPanel.isActivated()
                && mainLayout.startState == R.id.show_list_constraint) {
            mainLayout.progress = 1.0f
            uiSide = UISide.input
        }
    }
//
//    // --- Running ---
//
//    // foreground -> visible process
//    override fun onPause() {
//        super.onPause()
//    }
//
//    // visible -> cached
//    override fun onStop() {
//        super.onStop()
//    }



    override fun attachBaseContext(newBase: Context) {

        val prefs = newBase.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
        newLocale = Locale(currentLang)

        val context = ImperialContextWrapper.wrap(newBase, newLocale!!)
        super.attachBaseContext(context)
    }

}

