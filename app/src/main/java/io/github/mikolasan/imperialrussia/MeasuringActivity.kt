package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*







class MeasuringActivity : Activity() {

    private val languageSetting = "language"
    private val preferencesFile = "ImperialRussiaPrefs"
    private var newLocale: Locale? = null
    private var selectedPanel: ImperialUnitPanel? = null

    inner class DigitButton(button: Button, digit: Int) {
        private val digitSym: Char = digit.toString()[0]
        init {
            button.setOnClickListener {
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
        private val operations = setOf('/', '*', '+', '-', '.')

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

        val convFromLayout = findViewById<ConstraintLayout>(R.id.convert_from)
        val convToLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        val convFromPanel = ImperialUnitPanel(convFromLayout)
        val convToPanel = ImperialUnitPanel(convToLayout)
        val convFromInput = convFromPanel.input
        val convToInput = convToPanel.input

        val lengthUnits = LengthUnits.lengthUnits
        val lengthAdapter = LengthAdapter(this, lengthUnits)
        val lengthList = findViewById<ListView>(R.id.units_list)
        lengthList.adapter = lengthAdapter
        lengthList.setOnItemClickListener{ parent, view, position, id ->
            val unit = lengthAdapter.getItem(position) as? ImperialUnit
            unit?.let {
                if (!convFromPanel.isActivated()) {
                    convFromPanel.changeUnit(unit)
                    convFromPanel.setHighlight(true)
                    selectedPanel = convFromPanel
                    val startValue = 1.0
                    convFromPanel.setValue(startValue)
                    lengthAdapter.selectFromUnit(unit)
                    lengthAdapter.setCurrentValue(unit, startValue)
                } else if (!convToPanel.isActivated()) {
                    if (convFromPanel.unit != unit) {
                        convToPanel.changeUnit(unit)
                        convToPanel.setHighlight(true)
                        convFromPanel.setHighlight(false)
                        selectedPanel = convToPanel
                        val fromValue = convFromPanel.getValue()
                        val toValue = LengthUnits.convertValue(convFromPanel.unit, unit, fromValue)
                        convToPanel.setValue(toValue)
                        lengthAdapter.selectToUnit(unit)
                    } else {
                        selectedPanel = null
                        convFromPanel.setHighlight(false)
                        convFromPanel.deactivate()
                        lengthAdapter.setCurrentValue(unit, 0.0)
                    }
                } else {
                    if (selectedPanel == convToPanel) {
                        if (convToPanel.unit != unit) {
                            convToPanel.changeUnit(unit)
                            val fromValue = convFromPanel.getValue()
                            val toValue = LengthUnits.convertValue(convFromPanel.unit, unit, fromValue)
                            convToPanel.setValue(toValue)
                            lengthAdapter.selectToUnit(unit)
                            lengthAdapter.notifyDataSetChanged()
                        } // else {}
                    } else if (selectedPanel == convFromPanel) {
                        if (convFromPanel.unit != unit) {
                            convFromPanel.changeUnit(unit)
                            val fromValue = convFromPanel.getValue()
                            val toValue = LengthUnits.convertValue(convFromPanel.unit, convToPanel.unit, fromValue)
                            convToPanel.setValue(toValue)
                            lengthAdapter.setCurrentValue(unit, fromValue)
                            lengthAdapter.selectFromUnit(unit)
                        } // else {}
                    }
                }
            }
        }

        fun getColor(resourceId: Int): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // >= (API 23) Android 6.0 Marshmallow
                val theme = null
                resources.getColor(resourceId, theme)
            } else {
                @Suppress("DEPRECATION")
                resources.getColor(resourceId)
            }
        }

        val colorInputSelected = getColor(R.color.inputSelected)
        val colorInputNormal = getColor(R.color.inputNormal)

        fun selectPanel(new: ImperialUnitPanel, old: ImperialUnitPanel) {
            selectedPanel = new
            new.setHighlight(true)
            old.setHighlight(false)
        }

        convFromInput.setOnFocusChangeListener { view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    selectPanel(convFromPanel, convToPanel)
                }
                //view.setTextColor(if (hasFocus) colorInputSelected else colorInputNormal)
            }
        }
        convFromLayout.setOnClickListener { view ->
            selectPanel(convFromPanel, convToPanel)
        }

//        convFromInput.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                if (selectedPanel.input != convFromInput)
//                    return
//
//                s?.let {
//                    val fromValue = BasicCalculator(s.toString()).eval()
//                    val toValue = LengthUnits.convertValue(convFromPanel.unit, convToPanel.unit, fromValue)
//                    convToInput.setText(valueForDisplay(toValue))
//                    lengthAdapter.setCurrentValue(convFromPanel.unit, fromValue)
//                    convFromInput.setSelection(convFromInput.text.length)
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })

//        convToInput.setOnFocusChangeListener { view, hasFocus ->
//            if (view is EditText) {
//                if (hasFocus) {
//                    selectPanel(convToPanel, convFromPanel)
//                }
//                //view.setTextColor(if (hasFocus) colorInputSelected else colorInputNormal)
//
//            }
//        }


        fun setCursor(editText: EditText) {
            editText.isCursorVisible = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                editText.textCursorDrawable = resources.getDrawable(R.drawable.ic_cursor)
            } else {

            }
        }


        //setCursor(convToInput)

//        // X^3 + X^2
//        val cs = SpannableStringBuilder("X3 + X2")
//        cs.setSpan(SuperscriptSpan(), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        cs.setSpan(RelativeSizeSpan(0.75f), 1, 2, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        cs.setSpan(SuperscriptSpan(), 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        cs.setSpan(RelativeSizeSpan(0.75f), 6, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
//        convToInput.setText(cs)

        convToLayout.setOnClickListener{ view ->
            selectPanel(convToPanel, convFromPanel)
        }

        val motionLayout = findViewById<MotionLayout>(R.id.motion_layout)
        convToInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {

                if (selectedPanel?.input != convToInput)
                    return

                if (motionLayout.progress.equals(1.0))
                    return

                s?.let {
                    val toValue = BasicCalculator(s.toString()).eval()
                    val fromValue = LengthUnits.convertValue(convToPanel.unit, convFromPanel.unit, toValue)
                    convFromInput.setText(valueForDisplay(fromValue))
                    lengthAdapter.setCurrentValue(convFromPanel.unit, fromValue)
                    convToInput.setSelection(convToInput.text.length)
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

        convFromInput.setText(valueForDisplay(0.0))
        convFromInput.isCursorVisible = true
        convFromInput.requestFocus()


        val prefs = applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        var currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
        val editor = prefs.edit()

        val languageButton = findViewById<Button>(R.id.language)

        languageButton.text = currentLang
        languageButton.setOnClickListener { view ->
            val btn = view as Button
            if (currentLang == "ru") {
                currentLang = "en"
            } else if (currentLang == "en") {
                currentLang = "ru"
            }

            newLocale = Locale(currentLang)
            Locale.setDefault(newLocale!!)
            btn.text = currentLang
            editor.putString(languageSetting, currentLang)
            editor.apply()

            val config = resources.configuration
            config.locale = newLocale
            resources.updateConfiguration(config, resources.displayMetrics)


            lengthAdapter.notifyDataSetChanged()
            convFromPanel.changeUnit(convFromPanel.unit)
            convToPanel.changeUnit(convToPanel.unit)
        }

    }

    override fun attachBaseContext(newBase: Context) {

        val prefs = newBase.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
        newLocale = Locale(currentLang)

        val context = ImperialContextWrapper.wrap(newBase, newLocale!!)
        super.attachBaseContext(context)
    }

}

