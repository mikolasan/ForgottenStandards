package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import kotlinx.android.synthetic.main.imperial_length_list.*
import java.util.*


class MeasuringActivity : Activity() {

    private val languageSetting = "language"
    private val preferencesFile = "ImperialRussiaStore"
    private var newLocale: Locale? = null
    private var selectedPanel: ImperialUnitPanel? = null

    inner class DigitButton(button: Button, digit: Int) {
        private val digitSym: Char = digit.toString()[0]
        init {
            button.setOnClickListener {
                val text = selectedPanel?.getString() ?: ""
                if (text.length <= maxDisplayLength)
                    selectedPanel?.appendString(digitSym)
            }
        }
    }

    enum class Operation {
        PLUS,
        MINUS,
        DIV,
        MULT,
        BACK,
        CLEAR,
        DOT,
        EVAL,
    }

    inner class OperationButton(button: Button, operation: Operation) {
        private val operations = setOf('/', '*', '+', '-')

        private fun addSymbol(sym: Char) {
            selectedPanel?.appendString(sym, operations)
        }

        init {
            button.setOnClickListener {
                when (operation) {
                    Operation.MULT -> addSymbol('*')
                    Operation.DIV -> addSymbol('/')
                    Operation.PLUS -> addSymbol('+')
                    Operation.MINUS -> addSymbol('-')
                    Operation.CLEAR -> selectedPanel?.setString("")
                    Operation.BACK -> selectedPanel?.dropLastChar()
                    Operation.DOT -> addSymbol('.')
                    Operation.EVAL -> selectedPanel?.evaluateString()
                }
            }
        }
    }

    private fun restoreUnit(storedName: String): ImperialUnit? {
        if (storedName != "") {
            val unitName = ImperialUnitName.valueOf(storedName)
            return LengthUnits.imperialUnits[unitName]
        }
        return null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        val preferences = applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val preferencesEditor = preferences.edit()
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")

        val bottomLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)
        val topLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        val bottomPanel = ImperialUnitPanel(bottomLayout)
        val topPanel = ImperialUnitPanel(topLayout)
        val bottomInput = bottomPanel.input
        val topInput = topPanel.input

        val orderedUnits = LengthUnits.lengthUnits.toMutableList()
        LengthUnits.lengthUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            val p = preferences.getInt(settingName, i)
            if (preferences.contains(settingName)) {
                preferencesEditor.putInt(settingName, i)
            }
            orderedUnits[p] = u
        }
        preferencesEditor.apply()
        bottomPanelUnit?.let {
            if (!orderedUnits.take(2).contains(it)) {
                orderedUnits.moveToFront(it)
            }
        }
        topPanelUnit?.let {
            if (!orderedUnits.take(2).contains(it)) {
                orderedUnits.moveToFront(it)
            }
        }
        val lengthAdapter = LengthAdapter(this, orderedUnits)

        topPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_2_hint))

        fun setCursor(editText: EditText?) {
            editText?.isCursorVisible = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                editText?.textCursorDrawable = resources.getDrawable(R.drawable.ic_cursor)
            }
            editText?.setSelection(editText.text.length)
        }

        fun selectPanel(new: ImperialUnitPanel?, old: ImperialUnitPanel?) {
            selectedPanel = new
            new?.let {
                if (new.unit?.value?.compareTo(0.0) == 0) {
                    new.setString("")
                }
                new.setHighlight(true)
                //new.input.requestFocus()
                //new?.input?.isCursorVisible = true
                setCursor(new.input)
            }
            old?.let {
                if (old.getString() == "") {
                    old.setValue(0.0)
                }
                old.setHighlight(false)
                old.input.isCursorVisible = false
            }
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

            topPanel.setValue(bottomValue)
            bottomPanel.setValue(topValue)

            topPanel.setString(bottomString)
            bottomPanel.setString(topString)

            lengthAdapter.swapSelection()
        }

        fun setTopPanel(unit: ImperialUnit, value: Double?) {
            topPanel.changeUnit(unit)
            val currentValue = value ?: 1.0
            if (value == null) {
//                bottomPanel.setValue(currentValue)
                val topValue = LengthUnits.convertValue(bottomPanel.unit, unit, bottomPanel.getValue() ?: 1.0)
                topPanel.setValue(topValue)
            } else {
//                val topValue = LengthUnits.convertValue(bottomPanel.unit, unit, currentValue)
//                topPanel.setValue(topValue)
                topPanel.setValue(value)
            }
            lengthAdapter.setSelectedUnit(unit)
            lengthAdapter.setSecondUnit(bottomPanel.unit)
            lengthAdapter.notifyDataSetChanged()
            preferencesEditor.putString("topPanelUnit", unit.unitName.name)
            preferencesEditor.putFloat("topPanelValue", currentValue.toFloat())
            preferencesEditor.apply()
        }

        fun setBottomPanel(unit: ImperialUnit, value: Double? = null) {
            bottomPanel.changeUnit(unit)
            val currentValue = value ?: 1.0
            if (value == null) {
//                bottomPanel.setValue(currentValue)
                val bottomValue = LengthUnits.convertValue(topPanel.unit, unit, topPanel.getValue() ?: 1.0)
                bottomPanel.setValue(bottomValue)
            } else {
                val topValue = LengthUnits.convertValue(unit, topPanel.unit, value)
                topPanel.setValue(topValue)
            }
            // lengthAdapter.setCurrentValue(unit, currentValue)
            lengthAdapter.setSelectedUnit(unit)
            lengthAdapter.setSecondUnit(topPanel.unit)
            lengthAdapter.notifyDataSetChanged()
            preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
            preferencesEditor.putFloat("bottomPanelValue", currentValue.toFloat())
            preferencesEditor.apply()
        }

        fun unsetTopPanel() {
            preferencesEditor.putString("topPanelUnit", "")
            preferencesEditor.apply()
            topPanel.unit = null
            topPanel.deactivate()
            lengthAdapter.setSelectedUnit(null)
            if (!bottomPanel.isActivated()) {
                lengthAdapter.resetValues()
            } else {
                lengthAdapter.notifyDataSetChanged()
            }
        }

        fun unsetBottomPanel() {
            preferencesEditor.putString("bottomPanelUnit", "")
            preferencesEditor.apply()
            bottomPanel.unit = null
            bottomPanel.deactivate()
            lengthAdapter.setSelectedUnit(null)
            if (!topPanel.isActivated()) {
                lengthAdapter.resetValues()
            } else {
                lengthAdapter.notifyDataSetChanged()
            }
        }

        val unitsList = findViewById<ListView>(R.id.units_list)
        lengthAdapter.setOnArrowClickListener { position: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { i, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
        }
        lengthAdapter.setOnArrowLongClickListener { position: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { i, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
            unitsList.setSelectionAfterHeaderView()
        }
        unitsList.adapter = lengthAdapter
        unitsList.setOnItemClickListener{ _, _, position, id ->
            val unit = lengthAdapter.getItem(position) as? ImperialUnit
            unit?.let {
                // top == to
                // bottom = from
                if (!topPanel.isActivated()) {
                    if (bottomPanel.unit != unit) {
                        selectPanel(topPanel, bottomPanel)
                        setTopPanel(unit, 1.0)
                    } else {
//                        selectPanel(null, bottomPanel)
//                        unsetBottomPanel()
                    }
                } else if (!bottomPanel.isActivated()) {
                    if (topPanel.unit != unit) {
                        selectPanel(bottomPanel, topPanel)
                        setBottomPanel(unit, null)
                    } else {
//                        selectPanel(null, topPanel)
//                        unsetTopPanel()
                    }
                } else {
                    if (selectedPanel == topPanel) {
                        if (topPanel.unit == unit) {
//                            selectPanel(bottomPanel, topPanel)
//                            unsetTopPanel()
                        } else if (bottomPanel.unit != unit) {
                            setTopPanel(unit, null)
                        } else {
                            selectPanel(bottomPanel, topPanel)
                            lengthAdapter.swapSelection()
                            lengthAdapter.notifyDataSetChanged()
                        }
                    } else if (selectedPanel == bottomPanel) {
                        if (bottomPanel.unit == unit) {
//                            selectPanel(topPanel, bottomPanel)
//                            unsetBottomPanel()
                        } else if (topPanel.unit != unit) {
                            setBottomPanel(unit, null)
                        } else {
                            selectPanel(topPanel, bottomPanel)
                            lengthAdapter.swapSelection()
                            lengthAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        bottomLayout.setOnClickListener{ view ->
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                lengthAdapter.swapSelection()
                lengthAdapter.notifyDataSetChanged()
            }
        }

        bottomInput.setOnClickListener{ view ->
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                lengthAdapter.swapSelection()
                lengthAdapter.notifyDataSetChanged()
            }
        }

        topLayout.setOnClickListener{ view ->
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                lengthAdapter.swapSelection()
                lengthAdapter.notifyDataSetChanged()
            }
        }

        topInput.setOnClickListener{ view ->
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                lengthAdapter.swapSelection()
                lengthAdapter.notifyDataSetChanged()
            }
        }

        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)
        bottomInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel?.input != bottomInput)
                    return

                if (mainLayout is MotionLayout && mainLayout.progress.equals(1.0))
                    return

                s?.let {
                    val inputValue = BasicCalculator(s.toString()).eval()
                    val convertedValue = LengthUnits.convertValue(bottomPanel.unit, topPanel.unit, inputValue)
                    topInput.text = valueForDisplay(convertedValue)
                    lengthAdapter.setCurrentValue(bottomPanel.unit, inputValue)
                    bottomInput.setSelection(bottomInput.text.length)
                    preferencesEditor.putFloat("topPanelValue", convertedValue.toFloat())
                    preferencesEditor.putFloat("bottomPanelValue", inputValue.toFloat())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        topInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel?.input != topInput)
                    return

                if (mainLayout is MotionLayout && mainLayout.progress.equals(1.0))
                    return

                s?.let {
                    val inputValue = BasicCalculator(s.toString()).eval()
                    val convertedValue = LengthUnits.convertValue(topPanel.unit, bottomPanel.unit, inputValue)
                    bottomInput.text = valueForDisplay(convertedValue)
                    lengthAdapter.setCurrentValue(topPanel.unit, inputValue)
                    topInput.setSelection(topInput.text.length)
                    preferencesEditor.putFloat("topPanelValue", inputValue.toFloat())
                    preferencesEditor.putFloat("bottomPanelValue", convertedValue.toFloat())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        // restore
        val topPanelValue = preferences.getFloat("topPanelValue", 0.0f).toDouble()
        topPanelUnit?.let {
            setTopPanel(it, topPanelValue)
        }
        bottomPanelUnit?.let {
            setBottomPanel(it, null)
        }
        selectPanel(topPanel, bottomPanel)
        lengthAdapter.swapSelection()
        lengthAdapter.setCurrentValue(topPanel.unit, topPanelValue)

        val key_1 = DigitButton(findViewById(R.id.digit_1), 1)
        val key_2 = DigitButton(findViewById(R.id.digit_2), 2)
        val key_3 = DigitButton(findViewById(R.id.digit_3), 3)
        val key_4 = DigitButton(findViewById(R.id.digit_4), 4)
        val key_5 = DigitButton(findViewById(R.id.digit_5), 5)
        val key_6 = DigitButton(findViewById(R.id.digit_6), 6)
        val key_7 = DigitButton(findViewById(R.id.digit_7), 7)
        val key_8 = DigitButton(findViewById(R.id.digit_8), 8)
        val key_9 = DigitButton(findViewById(R.id.digit_9), 9)
        val key_0 = DigitButton(findViewById(R.id.digit_0), 0)

        val opBack = OperationButton(findViewById(R.id.op_back), Operation.BACK)
        val opClear = OperationButton(findViewById(R.id.op_clear), Operation.CLEAR)
        val opMult = OperationButton(findViewById(R.id.op_mult), Operation.MULT)
        val opDiv = OperationButton(findViewById(R.id.op_div), Operation.DIV)
        val opPlus = OperationButton(findViewById(R.id.op_plus), Operation.PLUS)
        val opMinus = OperationButton(findViewById(R.id.op_minus), Operation.MINUS)
        val opDot = OperationButton(findViewById(R.id.op_dot), Operation.DOT)
        val opEval = OperationButton(findViewById(R.id.op_eval), Operation.EVAL)

//        val prefs = applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
//        var currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
//        val editor = prefs.edit()

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
//            lengthAdapter.notifyDataSetChanged()
//            bottomPanel.updateUnitText()
//            topPanel.updateUnitText()
//        }

    }

    override fun attachBaseContext(newBase: Context) {

        val prefs = newBase.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
        newLocale = Locale(currentLang)

        val context = ImperialContextWrapper.wrap(newBase, newLocale!!)
        super.attachBaseContext(context)
    }

}

