package xyz.neupokoev.forgottenstandards.advanced

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.MinCyrillicNumeralsUnits
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.converter.ImperialTextWatcher
import xyz.neupokoev.forgottenstandards.converter.ImperialUnitPanel

class CyrillicNumeralsFragment : Fragment() {

    private lateinit var arabicInput: ImperialUnitPanel
    private lateinit var cyrillicOutput: TextView
    private lateinit var descriptionText: TextView
    
    private val units = mapOf(1 to 'А', 2 to 'В', 3 to 'Г', 4 to 'Д', 5 to 'Е', 6 to 'Ѕ', 7 to 'З', 8 to 'И', 9 to 'Ѳ')
    private val tens = mapOf(1 to 'І', 2 to 'К', 3 to 'Л', 4 to 'М', 5 to 'Н', 6 to 'Ѯ', 7 to 'О', 8 to 'П', 9 to 'Ч')
    private val hundreds = mapOf(1 to 'Р', 2 to 'С', 3 to 'Т', 4 to 'У', 5 to 'Ф', 6 to 'Х', 7 to 'Ѱ', 8 to 'Ѡ', 9 to 'Ц')

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as? MainActivity)?.setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_cyrillic_numerals, container, false)
        
        arabicInput = view.findViewById(R.id.arabic_input)
        cyrillicOutput = view.findViewById(R.id.cyrillic_output)
        descriptionText = view.findViewById(R.id.description_text)
        
        val mainActivity = activity as MainActivity
        val dummyUnit = mainActivity.workingUnits.mainUnit
        arabicInput.changeUnit(dummyUnit)
        arabicInput.setHighlight(true)
        arabicInput.activate()
        
        arabicInput.input.addTextChangedListener(object : ImperialTextWatcher(arabicInput, null as xyz.neupokoev.forgottenstandards.converter.ConverterFragment?, mainActivity) {
            override fun afterTextChanged(s: android.text.Editable?) {
                super.afterTextChanged(s)
                updateCyrillic()
            }
        })
        
        descriptionText.text = getString(R.string.cyrillic_numerals_description)

        return view
    }

    override fun onStart() {
        super.onStart()
        (activity as? MainActivity)?.onPanelSelected(arabicInput)
        updateCyrillic()
    }

    fun updateArabicPanel(unit: ImperialUnit, value: Double) {
        arabicInput.unit = unit
        arabicInput.setUnitValue(value)
        arabicInput.updateDisplayValue()
        updateCyrillic()
    }

    private fun updateCyrillic() {
        val value = arabicInput.getValue()?.toInt() ?: 0
        if (value <= 0) {
            cyrillicOutput.text = getString(R.string.placeholder_three_dashes)
            return
        }
        cyrillicOutput.text = convertToCyrillic(value)
    }

    private fun convertToCyrillic(n: Int): String {
        var num = n
        val result = StringBuilder()

        // Thousands
        if (num >= 1000) {
            val thousands = num / 1000
            result.append('҂')
            result.append(renderBase(thousands))
            num %= 1000
        }

        // Units, tens, hundreds
        val basePart = renderBase(num)
        
        if (basePart.isNotEmpty()) {
            val titloChar = '\u0483'
            val modifiedBase = StringBuilder(basePart)
            val targetPos = if (basePart.length > 1) {
                basePart.length - 2
            } else {
                0
            }
            modifiedBase.insert(targetPos + 1, titloChar)
            result.append(modifiedBase)
        } else if (result.isNotEmpty()) {
            // Case for exact thousands like 1000, 2000...
            result.append('\u0483')
        }

        return result.toString()
    }

    private fun renderBase(n: Int): String {
        var num = n
        val res = StringBuilder()
        // Hundreds
        if (num >= 100) {
            hundreds[num / 100]?.let { res.append(it) }
            num %= 100
        }

        // Tens and Units
        if (num in 11..19) {
            units[num % 10]?.let { res.append(it) }
            tens[1]?.let { res.append(it) }
        } else {
            if (num >= 10) {
                tens[num / 10]?.let { res.append(it) }
                num %= 10
            }
            if (num > 0) {
                units[num]?.let { res.append(it) }
            }
        }
        return res.toString()
    }
}
