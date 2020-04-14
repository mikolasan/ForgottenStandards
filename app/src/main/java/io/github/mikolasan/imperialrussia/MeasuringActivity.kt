package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*


class MeasuringActivity : Activity() {

    private val languageSetting = "language"
    private val preferencesFile = "ImperialRussiaPreferences"
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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        val preferences = applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val preferencesEditor = preferences.edit()

        val convFromLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)
        val convToLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        val convFromPanel = ImperialUnitPanel(convFromLayout)
        val convToPanel = ImperialUnitPanel(convToLayout)
        val convFromInput = convFromPanel.input
        val convToInput = convToPanel.input
        val lengthUnits = LengthUnits.lengthUnits
        val lengthAdapter = LengthAdapter(this, lengthUnits)
        convToPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_hint))
        convFromPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_2_hint))

        fun setCursor(editText: EditText?) {
            editText?.isCursorVisible = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                editText?.textCursorDrawable = resources.getDrawable(R.drawable.ic_cursor)
            }
            editText?.setSelection(editText?.text.length)
        }

        fun selectPanel(new: ImperialUnitPanel?, old: ImperialUnitPanel?) {
            selectedPanel = new
            new?.setHighlight(true)
            new?.input?.requestFocus()
            //new?.input?.isCursorVisible = true
            setCursor(new?.input)
            old?.setHighlight(false)
            old?.input?.isCursorVisible = false
        }

        fun setTopPanel(unit: ImperialUnit, value: Double?) {
            convToPanel.changeUnit(unit)
            val currentValue = value ?: 1.0
            if (value == null) {
                convFromPanel.setValue(currentValue)
            } else {
                val topValue = LengthUnits.convertValue(convFromPanel.unit, unit, currentValue)
                convToPanel.setValue(topValue)
            }
            lengthAdapter.selectToUnit(unit)
            lengthAdapter.notifyDataSetChanged()
            preferencesEditor.putString("topPanelUnit", unit.unitName.name)
            preferencesEditor.putFloat("topPanelValue", currentValue.toFloat())
            preferencesEditor.apply()
        }

        fun setBottomPanel(unit: ImperialUnit, value: Double? = null) {
            convFromPanel.changeUnit(unit)
            val currentValue = value ?: 1.0
            if (value == null) {
                convFromPanel.setValue(currentValue)
            } else {
                val topValue = LengthUnits.convertValue(unit, convToPanel.unit, value)
                convToPanel.setValue(topValue)
            }
            lengthAdapter.setCurrentValue(unit, currentValue)
            preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
            preferencesEditor.putFloat("bottomPanelValue", currentValue.toFloat())
            preferencesEditor.apply()
        }

        fun unsetTopPanel() {
            preferencesEditor.putString("topPanelUnit", "")
            preferencesEditor.apply()
            convToPanel.unit = null
            convToPanel.deactivate()
            lengthAdapter.selectToUnit(null)
            if (!convFromPanel.isActivated()) {
                lengthAdapter.resetValues()
            } else {
                lengthAdapter.notifyDataSetChanged()
            }
        }

        fun unsetBottomPanel() {
            preferencesEditor.putString("bottomPanelUnit", "")
            preferencesEditor.apply()
            convFromPanel.unit = null
            convFromPanel.deactivate()
            lengthAdapter.selectFromUnit(null)
            if (!convToPanel.isActivated()) {
                lengthAdapter.resetValues()
            } else {
                lengthAdapter.notifyDataSetChanged()
            }
        }

        // restore
        val topPanelUnit = preferences.getString("topPanelUnit", "") ?: ""
        if (topPanelUnit != "") {
            val unitName = ImperialUnitName.valueOf(topPanelUnit)
            val unit = LengthUnits.imperialUnits[unitName]
            unit?.let {
                val topPanelValue = preferences.getFloat("topPanelValue", 0.0f)
                setTopPanel(unit, topPanelValue.toDouble())
            }
        }
        val bottomPanelUnit = preferences.getString("bottomPanelUnit", "") ?: ""
        if (bottomPanelUnit != "") {
            val unitName = ImperialUnitName.valueOf(bottomPanelUnit)
            val unit = LengthUnits.imperialUnits[unitName]
            unit?.let {
                val bottomPanelValue = preferences.getFloat("bottomPanelValue", 0.0f)
                setBottomPanel(unit, bottomPanelValue.toDouble())
            }
        }

        val unitsList: ListView = findViewById<ListView>(R.id.units_list)
        unitsList.adapter = lengthAdapter
        unitsList.setOnItemClickListener{ _, _, position, id ->
            val unit = lengthAdapter.getItem(position) as? ImperialUnit
            unit?.let {
                if (!convFromPanel.isActivated()) {
                    if (convToPanel.unit != unit) {
                        selectPanel(convFromPanel, convToPanel)
                        setBottomPanel(unit, convFromPanel.getValue())
                    } else {
                        selectPanel(null, convToPanel)
                        unsetTopPanel()
                    }
                } else if (!convToPanel.isActivated()) {
                    if (convFromPanel.unit != unit) {
                        selectPanel(convToPanel, convFromPanel)
                        setTopPanel(unit, convFromPanel.getValue())
                    } else {
                        selectPanel(null, convFromPanel)
                        unsetBottomPanel()
                    }
                } else {
                    if (selectedPanel == convToPanel) {
                        if (convToPanel.unit == unit) {
                            selectPanel(convFromPanel, convToPanel)
                            unsetTopPanel()
                        } else if (convFromPanel.unit != unit) {
                            setTopPanel(unit, convFromPanel.getValue())
                        } else {
                            selectPanel(convFromPanel, convToPanel)
                            lengthAdapter.swapSelection()
                            lengthAdapter.notifyDataSetChanged()
                        }
                    } else if (selectedPanel == convFromPanel) {
                        if (convFromPanel.unit == unit) {
                            selectPanel(convToPanel, convFromPanel)
                            unsetBottomPanel()
                        } else if (convToPanel.unit != unit) {
                            setBottomPanel(unit, convFromPanel.getValue())
                        } else {
                            selectPanel(convToPanel, convFromPanel)
                            lengthAdapter.swapSelection()
                            lengthAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        convFromLayout.setOnClickListener { view ->
            convFromInput.requestFocus()
        }

        convToLayout.setOnClickListener{ view ->
            convToInput.requestFocus()
        }

        convFromInput.setOnFocusChangeListener { view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    if (selectedPanel != convFromPanel) {
                        selectPanel(convFromPanel, convToPanel)
                        lengthAdapter.swapSelection()
                        lengthAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        convFromInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!convFromInput.isFocused)
                    return

                if (selectedPanel?.input != convFromInput)
                    return

                s?.let {
                    val fromValue = BasicCalculator(s.toString()).eval()
                    val toValue = LengthUnits.convertValue(convFromPanel.unit, convToPanel.unit, fromValue)
                    convToInput.text = valueForDisplay(toValue)
                    lengthAdapter.setCurrentValue(convFromPanel.unit, fromValue)
                    convFromInput.setSelection(convFromInput.text.length)
                    preferencesEditor.putFloat("topPanelValue", toValue.toFloat())
                    preferencesEditor.putFloat("bottomPanelValue", fromValue.toFloat())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        convToInput.setOnFocusChangeListener { view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    if (selectedPanel != convToPanel) {
                        selectPanel(convToPanel, convFromPanel)
                        lengthAdapter.swapSelection()
                        lengthAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)
        convToInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (!convToInput.isFocused)
                    return

                if (selectedPanel?.input != convToInput)
                    return

                if (mainLayout is MotionLayout && mainLayout.progress.equals(1.0))
                    return

                s?.let {
                    val toValue = BasicCalculator(s.toString()).eval()
                    val fromValue = LengthUnits.convertValue(convToPanel.unit, convFromPanel.unit, toValue)
                    convFromInput.text = valueForDisplay(fromValue)
                    lengthAdapter.setCurrentValue(convFromPanel.unit, fromValue)
                    convToInput.setSelection(convToInput.text.length)
                    preferencesEditor.putFloat("topPanelValue", toValue.toFloat())
                    preferencesEditor.putFloat("bottomPanelValue", fromValue.toFloat())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

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
//            convFromPanel.updateUnitText()
//            convToPanel.updateUnitText()
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

