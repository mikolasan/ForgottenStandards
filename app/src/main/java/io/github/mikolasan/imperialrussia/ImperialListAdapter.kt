package io.github.mikolasan.imperialrussia

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import io.github.mikolasan.ratiogenerator.ImperialUnit
import java.util.Locale

class ImperialListAdapter(private val workingUnits: WorkingUnits) : BaseAdapter(), Filterable {
    var units: Array<ImperialUnit> = workingUnits.orderedUnits
    private var arrowClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowClickListener $position")
    }
    private var arrowLongClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowLongClickListener $position")
    }
    private var bookmarkClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("bookmarkClickListener $position")
    }

    fun setOnArrowClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowClickListener = listener
    }

    fun setOnArrowLongClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        arrowLongClickListener = listener
    }

    fun setOnBookmarkClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        bookmarkClickListener = listener
    }

    fun updateAllValues(unit: ImperialUnit?, value: Double) {
        units.forEach { u ->
            if (u != unit) {
                val v = convertValue(unit, u, value)
                u.value = v
                u.formattedString = makeSerializedString(valueForDisplay(v))
            }
        }
        notifyDataSetChanged()
    }

    override fun getView(position: Int, contentView: View?, parent: ViewGroup?): View {
        if (contentView != null) {
            if (contentView is ConstraintLayout) {
                updateViewData(contentView, position)
                updateViewColors(contentView, position)
                updateControlListeners(contentView, position)
                return contentView
            }
            return contentView
        } else {
            val context = parent?.context
            val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view = inflater.inflate(R.layout.unit_space, parent, false) as ConstraintLayout
            updateViewData(view, position)
            updateViewColors(view, position)
            updateControlListeners(view, position)
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
            ViewState.SECOND to R.color.backgroundAccent,
            ViewState.SELECTED to R.color.backgroundDark,
            ViewState.NORMAL to R.color.backgroundAccent
    )
    private val nameColors = mapOf(
            ViewState.SECOND to R.color.fontPrimary,
            ViewState.SELECTED to R.color.fontPrimary,
            ViewState.NORMAL to R.color.fontPrimary
    )
    private val valueColors = mapOf(
            ViewState.SECOND to R.color.fontPrimary,
            ViewState.SELECTED to R.color.fontPrimary,
            ViewState.NORMAL to R.color.fontPrimary
    )

    private fun updateViewColors(layout: ConstraintLayout, dataPosition: Int) {
        val name: TextView = layout.findViewById(R.id.unit_name)
        val value: TextView = layout.findViewById(R.id.unit_value)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        val bookmark: ImageView = layout.findViewById(R.id.bookmark)
        val bookmarkColor = if (units[dataPosition].bookmarked) R.color.bookmark else R.color.action
        val color = bookmark.context.resources.getColor(bookmarkColor)
        bookmark.drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
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
//                bookmark.setColorFilter(if (units[dataPosition].bookmarked) R.color.colorPrimaryDark else R.color.action, PorterDuff.Mode.SRC_IN)
                arrowUp.visibility = View.INVISIBLE
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
        value.text = data.formattedString //valueForDisplay(data.value)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        symbol.text = ImperialSymbol.symbols[data.unitName] ?: ""
    }

    private fun updateControlListeners(layout: ConstraintLayout, dataPosition: Int) {
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

        val bookmark: ImageView = layout.findViewById(R.id.bookmark)
        bookmark.setOnClickListener {
            unit.bookmarked = !unit.bookmarked
//            bookmarkClickListener(dataPosition, it, unit)
            notifyDataSetChanged()
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