package io.github.mikolasan.imperialrussia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class LengthAdapter(context: Context, private val layout: Int, private val units: Array<ImperialUnit>) : BaseAdapter() {
    private val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    private lateinit var masterUnit: ImperialUnit
    private var masterValue: Double = 0.0

    fun setCurrentValue(unit: ImperialUnit, value: Double) {
        masterUnit = unit
        masterValue = value
        for (listUnit in units) {
            listUnit.value = convertValue(unit, listUnit, value)
        }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        val view = contentView ?: inflater.inflate(layout, parent, false)
        val nameTextView: TextView = view.findViewById(R.id.unit_name)
        nameTextView.text = units[position].name
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