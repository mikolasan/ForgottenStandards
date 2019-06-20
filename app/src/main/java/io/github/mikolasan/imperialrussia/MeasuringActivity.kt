package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

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
        return .0;
    }

    fun convertToArshin(inches: Double): Double {
        return convertYardTo(Unit.YARD, convertToYard(Unit.INCH, inches))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)

        val convFromInput = findViewById<EditText>(R.id.conv_from_input)
        val convToInput = findViewById<EditText>(R.id.conv_to_input)
        convToInput.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty()!!) {
                    val inches = s.toString().toDouble()
                    val arshin = convertToArshin(inches)
                    convFromInput.setText(arshin.toString())
                }
            }
        })
    }
}
