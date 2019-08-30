package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class MeasuringActivity : Activity() {

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

    lateinit var fromUnit: ImperialUnit
    lateinit var toUnit: ImperialUnit
    lateinit var selectedPanel: ImperialUnitPanel


    inner class DigitButton(button: Button, digit: Int) {
        private val digitString = digit.toString()
        init {
            button.setOnClickListener {
                val selectedInput = selectedPanel.input
                val value = selectedInput.text.toString()
                selectedInput.setText(value + digitString)
            }
        }
    }

    inner class OperationButton(button: Button, operation: Operation) {
        val operations = setOf('/', '*', '+', '-', '.')

        fun addSymbol(sym: Char) {
            val selectedInput = selectedPanel.input
            var value = selectedInput.text.toString()
            if (operations.contains(value.last()))
                value = value.dropLast(1)
            selectedInput.setText(value + sym)
        }

        init {
            button.setOnClickListener {
                val selectedInput = selectedPanel.input
                when (operation) {
                    Operation.MULT -> addSymbol('*')
                    Operation.DIV -> addSymbol('/')
                    Operation.PLUS -> addSymbol('+')
                    Operation.MINUS -> addSymbol('-')
                    Operation.CLEAR -> selectedInput.setText("")
                    Operation.BACK -> {
                        val value = selectedInput.text.toString()
                        selectedInput.setText(value.dropLast(1))
                    }
                    Operation.DOT -> addSymbol('.')
                    Operation.EVAL -> {
                        val value = selectedInput.text.toString()
                        val calculator = BasicCalculator(value)
                        selectedInput.setText(calculator.eval().toString())
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        fromUnit = arshin
        toUnit = inch
        val convFromInput = findViewById<EditText>(R.id.conv_from_input)
        val convToInput = findViewById<EditText>(R.id.conv_to_input)
        val convFromTitle = findViewById<TextView>(R.id.conv_from_title)
        val convToTitle = findViewById<TextView>(R.id.conv_to_title)

        val convFromPanel = ImperialUnitPanel(convFromTitle, convFromInput, fromUnit)
        val convToPanel = ImperialUnitPanel(convToTitle, convToInput, toUnit)
        selectedPanel = convFromPanel
        convFromTitle.text = fromUnit.name
        convToTitle.text = toUnit.name


        val lengthUnits = arrayOf(point, line, inch, tip, palm, foot, arshin, fathom, turn, mile)
        val lengthAdapter = LengthAdapter(this, R.layout.listview_item, lengthUnits)
        val lengthList = findViewById<ListView>(R.id.units_list)
        lengthList.adapter = lengthAdapter
        lengthList.setOnItemClickListener{ parent, view, position, id ->
            val unit = lengthAdapter.getItem(position) as? ImperialUnit
            unit?.let {
                selectedPanel.changeUnit(unit)
                if (selectedPanel.input == convToInput) {
                    toUnit = unit
                    val fromValue = convFromInput.text.toString().toDouble()
                    val toValue = convertValue(fromUnit, unit, fromValue)
                    convToInput.setText(valueForDisplay(toValue))
                } else if (selectedPanel.input == convFromInput) {
                    fromUnit = unit
                    val toValue = convToInput.text.toString().toDouble()
                    val fromValue = convertValue(toUnit, unit, toValue)
                    convFromInput.setText(valueForDisplay(fromValue))
                    lengthAdapter.setCurrentValue(unit, fromValue)
                }
            }
        }

        fun getColor(resourceId: Int): Int {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                val theme = null
                return resources.getColor(resourceId, theme)
            } else {
                return resources.getColor(resourceId)
            }
        }
        val colorInputSelected = getColor(R.color.inputSelected)
        val colorInputNormal = getColor(R.color.inputNormal)

        convFromInput.setOnFocusChangeListener{ view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    selectedPanel = convFromPanel
                }
                view.setTextColor(if (hasFocus) colorInputSelected else colorInputNormal)
            }
        }
        convFromInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel.input != convFromInput)
                    return

                s?.let {
                    val fromValue = BasicCalculator(s.toString()).eval()
                    val toValue = convertValue(fromUnit, toUnit, fromValue)
                    convToInput.setText(valueForDisplay(toValue))
                    lengthAdapter.setCurrentValue(fromUnit, fromValue)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })

        convToInput.setOnFocusChangeListener{ view, hasFocus ->
            if (view is EditText) {
                if (hasFocus) {
                    selectedPanel = convToPanel
                }
                view.setTextColor(if (hasFocus) colorInputSelected else colorInputNormal)
            }
        }
        convToInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel.input != convToInput)
                    return

                s?.let {
                    val toValue = BasicCalculator(s.toString()).eval()
                    val fromValue = convertValue(toUnit, fromUnit, toValue)
                    convFromInput.setText(valueForDisplay(fromValue))
                    lengthAdapter.setCurrentValue(fromUnit, fromValue)
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


    }
}
