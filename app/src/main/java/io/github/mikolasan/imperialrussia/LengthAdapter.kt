package io.github.mikolasan.imperialrussia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class LengthAdapter(context: Context, private val units: Array<ImperialUnit>) : BaseAdapter() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private lateinit var masterUnit: ImperialUnit
    private var masterValue: Double = 0.0
    private var fromUnit: ImperialUnit = units[0]
    private var toUnit: ImperialUnit = units[1]

    fun setCurrentValue(unit: ImperialUnit, value: Double) {
        masterUnit = unit
        masterValue = value
        for (listUnit in units) {
            listUnit.value = convertValue(unit, listUnit, value)
            if (listUnit == masterUnit)
                fromUnit = masterUnit
        }
        notifyDataSetChanged()
    }

    fun selectFromUnit(unit: ImperialUnit) {
        fromUnit = unit
    }

    fun selectToUnit(unit: ImperialUnit) {
        toUnit = unit
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        val view = when (units[position]) {
            fromUnit -> inflater.inflate(R.layout.listview_from_item, parent, false)
            toUnit -> inflater.inflate(R.layout.listview_to_item, parent, false)
            else -> inflater.inflate(R.layout.listview_item, parent, false)
        }
        val nameTextView: TextView = view.findViewById(R.id.unit_name)
        nameTextView.text = inflater.context.resources.getString(units[position].resourceId)
        val valueTextView: TextView = view.findViewById(R.id.unit_value)
        valueTextView.text = valueForDisplay(units[position].value)
        return view
    }

    override fun getItem(position: Int): Any {
        return units[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return units.size
    }

}