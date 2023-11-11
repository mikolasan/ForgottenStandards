package io.github.mikolasan.imperialrussia

import android.content.Context
import android.view.*
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.mikolasan.ratiogenerator.ImperialUnit
import java.util.*

class ImperialListAdapter(private val workingUnits: WorkingUnits) : BaseAdapter(), Filterable {
    var units: Array<ImperialUnit> = workingUnits.orderedUnits
    private var arrowClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowClickListener $position")
    }
    private var arrowLongClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowLongClickListener $position")
    }

    fun setOnArrowClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowClickListener = listener
    }

    fun setOnArrowLongClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowLongClickListener = listener
    }

    fun resetAllValues() {
        units.forEach { u -> u.value = 0.0 }
        notifyDataSetChanged()
    }

    fun updateAllValues(unit: ImperialUnit?, value: Double) {
        units.forEach { u -> u.value = convertValue(unit, u, value) }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        if (contentView != null) {
            if (contentView is ConstraintLayout) {
                updateViewData(contentView, position)
                updateViewColors(contentView, position)
                updateArrowListener(contentView, position)
                return contentView
            }
            return contentView
        } else {
            val context = parent?.context
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.unit_space, parent, false) as ConstraintLayout
            updateViewData(view, position)
            updateViewColors(view, position)
            updateArrowListener(view, position)
            return view
        }

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

    enum class ViewState {
        SECOND,
        SELECTED,
        NORMAL
    }

    private val backgrounds = mapOf(
            ViewState.SECOND to R.drawable.ic_side_from_back,
            ViewState.SELECTED to R.drawable.ic_side_to_back,
            ViewState.NORMAL to R.drawable.ic_side_panel_back
    )
    private val nameColors = mapOf(
            ViewState.SECOND to R.color.inputNormal,
            ViewState.SELECTED to R.color.inputSelected,
            ViewState.NORMAL to R.color.colorPrimary
    )
    private val valueColors = mapOf(
            ViewState.SECOND to R.color.keyboardDigit,
            ViewState.SELECTED to R.color.colorPrimaryDark,
            ViewState.NORMAL to R.color.keyboardDigit
    )

    private fun updateViewColors(layout: ConstraintLayout, dataPosition: Int) {
        val name: TextView = layout.findViewById(R.id.unit_name)
        val value: TextView = layout.findViewById(R.id.unit_value)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        when (getItem(dataPosition) as ImperialUnit) {
            workingUnits.topUnit -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.SELECTED))
                name.setTextColorId(nameColors.getValue(ViewState.SELECTED))
                value.setTextColorId(valueColors.getValue(ViewState.SELECTED))
                symbol.setTextColorId(valueColors.getValue(ViewState.SELECTED))
                arrowUp.visibility = if (dataPosition == 0) View.INVISIBLE else View.VISIBLE
            }
//            workingUnits.bottomUnit -> {
//                layout.setBackgroundResource(backgrounds.getValue(ViewState.SECOND))
//                name.setTextColorId(nameColors.getValue(ViewState.SECOND))
//                value.setTextColorId(valueColors.getValue(ViewState.SECOND))
//                symbol.setTextColorId(valueColors.getValue(ViewState.SECOND))
//                arrowUp.visibility = View.INVISIBLE
//            }
            else -> {
                layout.setBackgroundResource(backgrounds.getValue(ViewState.NORMAL))
                name.setTextColorId(nameColors.getValue(ViewState.NORMAL))
                value.setTextColorId(valueColors.getValue(ViewState.NORMAL))
                symbol.setTextColorId(valueColors.getValue(ViewState.NORMAL))
                arrowUp.visibility = View.VISIBLE
            }
        }
    }

    private fun updateViewData(layout: ConstraintLayout, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition) as ImperialUnit
        val name: TextView = layout.findViewById(R.id.unit_name)
        name.text = data.unitName.name.lowercase(Locale.getDefault()).replace('_', ' ')
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        val value: TextView = layout.findViewById(R.id.unit_value)
        value.text = valueForDisplay(data.value)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        symbol.text = ImperialSymbol.symbols.getOrDefault(data.unitName, "")
    }

    private fun updateArrowListener(layout: ConstraintLayout, dataPosition: Int) {
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        val unit = units[dataPosition]
        arrowUp.setOnClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                arrowClickListener(dataPosition, it, unit)
                notifyDataSetChanged()
            }
        }
        arrowUp.setOnLongClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                notifyDataSetChanged()
                arrowLongClickListener(dataPosition, it, unit)
            }
            dataPosition != 0
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredUnits = mutableListOf<ImperialUnit>()
            if (constraint.isNullOrEmpty()) {
                filteredUnits.addAll(units)
            } else {
                for (item in units) {
                    if (item.unitName.name
                            .toLowerCase()
                            .startsWith(constraint.toString().toLowerCase())) {
                        filteredUnits.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredUnits
            return results
        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            notifyDataSetChanged()
            // TODO: use submitList from AsyncListDiffer (used in ListAdapter)
            //submitList(filterResults?.values as MutableList<String>)
        }

    }


}