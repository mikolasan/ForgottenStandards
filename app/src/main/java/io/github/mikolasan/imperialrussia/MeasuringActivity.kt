package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity.CENTER_HORIZONTAL
import android.view.Gravity.CENTER_VERTICAL
import android.view.Gravity.RIGHT
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

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

    lateinit var selectedInput: EditText
    var fromUnit: ImperialUnit = arshin
    var toUnit: ImperialUnit = inch



    inner class DigitButton(button: Button, digit: Int) {
        private val digitString = digit.toString()
        init {
            button.setOnClickListener {
                val value = selectedInput?.text.toString()
                selectedInput?.setText(value + digitString)
            }
        }
    }

    inner class OperationButton(button: Button, operation: Operation) {
        val operations = setOf('/', '*', '+', '-', '.')

        fun addSymbol(sym: Char) {
            var value = selectedInput?.text.toString()
            if (operations.contains(value.last()))
                value = value.dropLast(1)
            selectedInput?.setText(value + sym)
        }

        init {
            button.setOnClickListener {
                when (operation) {
                    Operation.MULT -> addSymbol('*')
                    Operation.DIV -> addSymbol('/')
                    Operation.PLUS -> addSymbol('+')
                    Operation.MINUS -> addSymbol('-')
                    Operation.CLEAR -> selectedInput?.setText("")
                    Operation.BACK -> {
                        val value = selectedInput?.text.toString()
                        selectedInput?.setText(value.dropLast(1))
                    }
                    Operation.DOT -> addSymbol('.')
                    Operation.EVAL -> {
                        val value = selectedInput?.text.toString()
                        val calculator = BasicCalculator(value)
                        selectedInput?.setText(calculator.eval().toString())
                    }
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        val lengthUnits = arrayOf(point, line, inch, tip, palm, foot, arshin, fathom, turn, mile)
        val lengthAdapter = LengthAdapter(this, R.layout.listview_item, lengthUnits)
        val lengthList = findViewById<ListView>(R.id.units_list)
        lengthList.adapter = lengthAdapter
//        lengthList.setOnClickListener {
//            if (it.id == R.layout.listview_item)
//                if (it is ConstraintLayout)
//                    fromUnit = it.findViewById<TextView>(R.id.unit_name)
//        }

        val convFromInput = findViewById<EditText>(R.id.conv_from_input)
        val convToInput = findViewById<EditText>(R.id.conv_to_input)
        selectedInput = convFromInput
        val convFromTitle = findViewById<TextView>(R.id.conv_from_title)
        val convToTitle = findViewById<TextView>(R.id.conv_to_title)
        convFromTitle.text = fromUnit.name
        convToTitle.text = toUnit.name

        convFromInput.setOnFocusChangeListener{
            view, hasFocus -> if (view is EditText) {
                if (hasFocus) {
                    selectedInput = view
                    view.gravity = RIGHT or CENTER_VERTICAL
                } else {
                    view.gravity = CENTER_HORIZONTAL or CENTER_VERTICAL
                }
            }
        }
        convFromInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
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

        convToInput.setOnFocusChangeListener{
            view, hasFocus -> if (view is EditText) {
                if (hasFocus) {
                    selectedInput = view
                    view.gravity = RIGHT or CENTER_VERTICAL
                } else {
                    view.gravity = CENTER_HORIZONTAL or CENTER_VERTICAL
                }
            }
        }
//        convToInput.addTextChangedListener(object: TextWatcher {
//            override fun afterTextChanged(s: Editable?) {
//                s?.let {
//                    val toValue = BasicCalculator(s.toString()).eval()
//                    val fromValue = convertValue(toUnit, fromUnit, toValue)
//                    convFromInput.setText(valueForDisplay(fromValue))
//                }
//            }
//
//            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
//            }
//
//            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
//            }
//        })

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
