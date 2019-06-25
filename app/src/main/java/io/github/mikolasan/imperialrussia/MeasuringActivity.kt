package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Button

class MeasuringActivity : Activity() {

    enum class Unit {
        POINT,
        LINE,
        INCH,
        TIP, // vershok
        PALM, // piad
        FOOT,
        YARD, // arshin
        FATHOM, // sazhen
        TURN, // versta
        MILE,
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

    val ratio : DoubleArray = doubleArrayOf(
            3.5714285714285714285714285714286e-4,
            0.00357142857142857142857142857143,
            0.03571428571428571428571428571429,
            0.0625,
            0.25,
            0.42857142857142857142857142857143,
            1.0,
            3.0,
            1500.0,
            10500.0
    )

    private fun convertToYard(type: Unit, value: Double): Double {
        return value / ratio[type.ordinal]
    }

    private fun convertYardTo(type: Unit, value: Double): Double {
        return value * ratio[type.ordinal]
    }

    fun convertUnits(type: Unit, value: Double, resultType: Unit): Double {
        if (type == resultType) {
            return value;
        } else if (Unit.YARD == type) {
            return convertYardTo(resultType, value)
        } else if (Unit.YARD == resultType) {
            return convertToYard(type, value)
        }
        return .0
    }

    fun convertToArshin(inches: Double): Double {
        return convertYardTo(Unit.YARD, convertToYard(Unit.INCH, inches))
    }

    var selectedInput: EditText? = null

    inner class DigitButton(button: Button, digit: Int) {
        private val digitString = digit.toString()
        init {
            button.setOnClickListener {
                val value = selectedInput!!.text.toString()
                selectedInput!!.setText(value + digitString)
            }
        }
    }

    inner class OperationButton(button: Button, operation: Operation) {
        init {
            button.setOnClickListener {
                val value = selectedInput!!.text.toString()
                when (operation) {
                    Operation.MULT -> selectedInput!!.setText(value+"*")
                    Operation.DIV -> selectedInput!!.setText(value+"/")
                    Operation.PLUS -> selectedInput!!.setText(value+"+")
                    Operation.MINUS -> selectedInput!!.setText(value+"-")
                    Operation.CLEAR -> selectedInput!!.setText("")
                    Operation.BACK -> selectedInput!!.setText(value.dropLast(1))
                    Operation.DOT -> selectedInput!!.setText(value + ".")
                    Operation.EVAL -> {
                        val calculator = BasicCalculator(value)
                        selectedInput!!.setText(calculator.eval().toString())
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        val convFromInput = findViewById<EditText>(R.id.conv_from_input)
        val convToInput = findViewById<EditText>(R.id.conv_to_input)
        selectedInput = convToInput
        convToInput.setOnFocusChangeListener{
            view, hasFocus -> if (hasFocus && view is EditText) selectedInput = view
        }
        convFromInput.setOnFocusChangeListener{
            view, hasFocus -> if (hasFocus && view is EditText) selectedInput = view
        }
        convToInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    val inches = BasicCalculator(s.toString()).eval()
                    val arshin = convertToArshin(inches)
                    convFromInput.setText(arshin.toString())
                }
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
