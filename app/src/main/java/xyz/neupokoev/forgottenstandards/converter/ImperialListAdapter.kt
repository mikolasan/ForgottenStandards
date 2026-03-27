package xyz.neupokoev.forgottenstandards.converter

import android.content.Context
import android.graphics.PorterDuff
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.willowtreeapps.fuzzywuzzy.ToStringFunction
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import com.willowtreeapps.fuzzywuzzy.diffutils.algorithms.WeightedRatio
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.WorkingUnits
import xyz.neupokoev.forgottenstandards.convertValueWrapper
import xyz.neupokoev.forgottenstandards.moveToFrontFrom
import xyz.neupokoev.forgottenstandards.setTextColorId
import xyz.neupokoev.forgottenstandards.valueForDisplay
import java.util.Locale


class ImperialListAdapter
    : RecyclerView.Adapter<ImperialListAdapter.ViewHolder>(), Filterable
{

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var data: ImperialUnit
        lateinit var unitSelectedListener: (Int, View, ImperialUnit) -> Unit
        val layout: ConstraintLayout = view as ConstraintLayout
        val name: TextView = layout.findViewById(R.id.unit_name)
        val value: TextView = layout.findViewById(R.id.unit_value)
        val rangeContainer: View = layout.findViewById(R.id.range_container)
        val rangeMinValue: TextView = layout.findViewById(R.id.range_min_value)
        val rangeMaxValue: TextView = layout.findViewById(R.id.range_max_value)
        val rangeSeparator: TextView = layout.findViewById(R.id.range_separator)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        val bookmark: ImageView = layout.findViewById(R.id.bookmark)
        val infoButton: ImageView = layout.findViewById(R.id.info_button)
        val description: TextView = layout.findViewById(R.id.unit_description)
        val controls: View = layout.findViewById(R.id.unit_controls)
        val buttonMinus: View = layout.findViewById(R.id.button_minus)
        val buttonPlus: View = layout.findViewById(R.id.button_plus)

        init {
            layout.setOnClickListener {
                unitSelectedListener(this.absoluteAdapterPosition, view, data)
            }
        }
    }

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    lateinit var workingUnits: WorkingUnits
    private lateinit var allUnits: ArrayList<ImperialUnit>
    private lateinit var listUnits: ArrayList<ImperialUnit>
    private lateinit var noPinnedUnits: ArrayList<ImperialUnit>

    private var unitSelectedListener: (Int, View, ImperialUnit) -> Unit = { _, _, _ ->

    }

    private var arrowClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowClickListener $position")
    }
    private var arrowLongClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("arrowLongClickListener $position")
    }
    private var bookmarkClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("bookmarkClickListener $position")
    }
    private var infoClickListener: (Int, View, ImperialUnit) -> Unit = { position, _, _ ->
        println("infoClickListener $position")
    }

    fun setOnUnitSelectedListener(listener: (Int, View, ImperialUnit) -> Unit) {
        unitSelectedListener = listener
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

    fun setOnInfoClickListener(listener: (Int, View, ImperialUnit) -> Unit) {
        infoClickListener = listener
    }

    fun setUnits(units: Array<ImperialUnit>) {
        allUnits = ArrayList(units.toList())
        noPinnedUnits = ArrayList(units.toList())
        listUnits = noPinnedUnits
    }

    fun excludeUnit(unit: ImperialUnit) {
        val id = listUnits.indexOfFirst { it.unitName == unit.unitName }
        if (id != -1) {
            noPinnedUnits.remove(unit)
            listUnits = noPinnedUnits
            notifyItemRemoved(id)
        }
    }

    fun restoreUnit(unit: ImperialUnit) {
        noPinnedUnits.add(0, unit)
        listUnits = noPinnedUnits
        notifyItemInserted(0)
    }

    fun updateAllValues(unit: ImperialUnit, value: Double) {
        allUnits.forEachIndexed { i, u ->
            if (u != unit) {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        convertValueWrapper(unit, value, u)
                    }
                    notifyItemChanged(i)
                }
            } else {
                unit.value = value
                notifyItemChanged(i)
            }
        }
    }

    private fun getItem(position: Int): ImperialUnit {
        return listUnits[position]
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val context = parent.context
        val inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.unit_space, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        updateViewData(holder, position)
        updateViewColors(holder, position)
        updateControlListeners(holder, position)
    }

    override fun getItemCount(): Int {
        return listUnits.size
    }

    enum class ViewState {
        SECOND,
        SELECTED,
        NORMAL
    }

    private val backgrounds = mapOf(
            ViewState.SECOND to R.color.panel_back,
            ViewState.SELECTED to R.color.panel_selected_back,
            ViewState.NORMAL to R.color.panel_back
    )
    private val nameColors = mapOf(
            ViewState.SECOND to R.color.panel_font,
            ViewState.SELECTED to R.color.panel_selected_font,
            ViewState.NORMAL to R.color.panel_font
    )
    private val valueColors = mapOf(
            ViewState.SECOND to R.color.panel_font,
            ViewState.SELECTED to R.color.panel_selected_font,
            ViewState.NORMAL to R.color.panel_font
    )
    private val valueBackgrounds = mapOf(
        ViewState.SECOND to R.color.panel_back,
        ViewState.SELECTED to R.color.panel_selected_back,
        ViewState.NORMAL to R.color.panel_back
    )

    private fun updateViewColors(holder: ViewHolder, dataPosition: Int) {
        val unit = getItem(dataPosition)
        val mainActivity = holder.itemView.context as? MainActivity
        
        var bookmarkColorRes = if (unit.bookmarked) R.color.bookmark else R.color.action
        
        // Highlight first item on first run
        if (dataPosition == 0 && mainActivity?.settings?.isFirstRun() == true) {
            bookmarkColorRes = R.color.panel_selected_back // Use a darker/more visible color to highlight
        }

        val color = holder.bookmark.context.resources.getColor(bookmarkColorRes)
        holder.bookmark.drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        
        val state = if (unit == workingUnits.mainUnit) ViewState.SELECTED else ViewState.NORMAL
        
        holder.layout.setBackgroundResource(backgrounds.getValue(state))
        holder.name.setTextColorId(nameColors.getValue(state))
        holder.value.setTextColorId(valueColors.getValue(state))
        holder.rangeMinValue.setTextColorId(valueColors.getValue(state))
        holder.rangeMaxValue.setTextColorId(valueColors.getValue(state))
        holder.rangeSeparator.setTextColorId(valueColors.getValue(state))
        holder.symbol.setTextColorId(valueColors.getValue(state))
        
        val valueBg = valueBackgrounds.getValue(state)
        holder.value.setBackgroundResource(valueBg)
        holder.rangeContainer.setBackgroundResource(valueBg)
    }

    private fun updateViewData(holder: ViewHolder, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition)
        holder.unitSelectedListener = unitSelectedListener
        holder.data = data
        holder.name.text = data.unitName.name.lowercase(Locale.getDefault()).replace('_', ' ')
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        if (data.range.isPresent) {
            holder.rangeMinValue.text = valueForDisplay(data.range.get().first)
            holder.rangeMaxValue.text = valueForDisplay(data.range.get().second)
            holder.rangeContainer.visibility = View.VISIBLE
            holder.value.visibility = View.INVISIBLE
            holder.controls.visibility = View.GONE
            holder.description.visibility = View.GONE
        } else {
            holder.value.text = valueForDisplay(data.value)
            holder.value.visibility = View.VISIBLE
            holder.rangeContainer.visibility = View.INVISIBLE
            
            if (data.unitName == ImperialUnitName.BEAUFORT) {
                holder.controls.visibility = View.VISIBLE
                val v = data.value
                holder.description.text = data.rangeValueNames[v.toInt().toDouble()] ?: ""
                holder.description.visibility = View.VISIBLE
            } else {
                holder.controls.visibility = View.GONE
                holder.description.visibility = View.GONE
            }
        }
        holder.symbol.text = ImperialSymbol.symbols[data.unitName] ?: ""
    }

    private fun updateControlListeners(holder: ViewHolder, dataPosition: Int) {
        val unit = getItem(dataPosition)
        holder.bookmark.setOnClickListener {
            unit.bookmarked = !unit.bookmarked
            bookmarkClickListener(dataPosition, it, unit)
            notifyItemChanged(dataPosition)
        }
        holder.infoButton.setOnClickListener {
            infoClickListener(holder.absoluteAdapterPosition, it, unit)
        }
        holder.buttonMinus.setOnClickListener {
            unit.value = (unit.value - 1.0).coerceAtLeast(0.0)
            updateAllValues(unit, unit.value)
        }
        holder.buttonPlus.setOnClickListener {
            unit.value = (unit.value + 1.0).coerceAtMost(12.0)
            updateAllValues(unit, unit.value)
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    class UnitToString : ToStringFunction<ImperialUnit> {
        override fun apply(item: ImperialUnit): String {
            return item.unitName.name.lowercase(Locale.ROOT)
        }
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) {
                results.values = noPinnedUnits
                return results
            } else {
                val query = constraint.toString().lowercase(Locale.ROOT)
                val filtered = FuzzySearch.extractSorted(
                    query,
                    noPinnedUnits,
                    UnitToString(),
                    WeightedRatio(),
                    50)
                    .map { it.referent }
                listUnits = ArrayList(filtered)
                results.values = listUnits
                return results
            }

        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            listUnits = filterResults?.values as ArrayList<ImperialUnit>
            notifyDataSetChanged()
        }

    }


}