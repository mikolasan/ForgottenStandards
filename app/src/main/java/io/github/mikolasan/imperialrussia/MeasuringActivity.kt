package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class MeasuringActivity : Activity() {

    fun convertToArshin(meters: Double): Double {
        return meters / 0.7112
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)
        val input = findViewById<EditText>(R.id.editInput)
        val output = findViewById<EditText>(R.id.editOutput)
        input.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.isNotEmpty()!!) {
                    val meters = s.toString().toDouble()
                    val arshin = convertToArshin(meters)
                    output.setText(arshin.toString())
                }
            }
        })
        //input.setText()
    }
}
