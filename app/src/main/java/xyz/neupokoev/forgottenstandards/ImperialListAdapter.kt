package xyz.neupokoev.forgottenstandards

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
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import io.github.mikolasan.ratiogenerator.ImperialUnit
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale


class ImperialListAdapter(private val workingUnits: WorkingUnits,
                          private val publishSubject: MainActivity)
    : RecyclerView.Adapter<ImperialListAdapter.ViewHolder>(), Filterable
{

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        lateinit var data: ImperialUnit
        lateinit var publishSubject: MainActivity
        val layout: ConstraintLayout = view as ConstraintLayout
        val name: TextView = layout.findViewById(R.id.unit_name)
        val value: TextView = layout.findViewById(R.id.unit_value)
        val symbol: TextView = layout.findViewById(R.id.unit_symbol)
        val arrowUp: ImageView = layout.findViewById(R.id.arrow_up)
        val bookmark: ImageView = layout.findViewById(R.id.bookmark)
        init {
            layout.setOnClickListener {
                publishSubject.onUnitSelected(data)
            }
        }
    }

    private val scope = CoroutineScope(Job() + Dispatchers.Main)

    var allUnits: Array<ImperialUnit> = workingUnits.orderedUnits
    var units: Array<ImperialUnit> = workingUnits.orderedUnits
    var names: List<String> = allUnits.map { u -> u.unitName.name.lowercase(Locale.ROOT) }

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
        names = allUnits.map { u -> u.unitName.name.lowercase(Locale.ROOT) }
        allUnits.forEachIndexed { i, u ->
            if (u != unit) {
                scope.launch {
                    withContext(Dispatchers.IO) {
                        val v = convertValue(unit, u, value)
                        u.value = v
                        u.formattedString = makeSerializedString(valueForDisplay(v))
                    }
                    notifyItemChanged(i)
                }
            } else {
                notifyItemChanged(i)
            }
        }
    }

    private fun getItem(position: Int): ImperialUnit {
        return units[position]
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
        return units.size
    }

    enum class ViewState {
        SECOND,
        SELECTED,
        NORMAL
    }

    private val backgrounds = mapOf(
            ViewState.SECOND to R.color.backgroundAccent,
            ViewState.SELECTED to R.color.colorPrimaryDark,
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
    private val valueBackgrounds = mapOf(
        ViewState.SECOND to R.color.backgroundAccent,
        ViewState.SELECTED to R.color.backgroundDark,
        ViewState.NORMAL to R.color.inputNormal
    )

    private fun updateViewColors(holder: ViewHolder, dataPosition: Int) {
        val bookmarkColor = if (units[dataPosition].bookmarked) R.color.bookmark else R.color.action
        val color = holder.bookmark.context.resources.getColor(bookmarkColor)
        holder.bookmark.drawable.mutate().setColorFilter(color, PorterDuff.Mode.SRC_IN)
        when (getItem(dataPosition) as ImperialUnit) {
            workingUnits.topUnit -> {
                holder.layout.setBackgroundResource(backgrounds.getValue(ViewState.SELECTED))
                holder.name.setTextColorId(nameColors.getValue(ViewState.SELECTED))
                holder.value.setTextColorId(valueColors.getValue(ViewState.SELECTED))
                holder.symbol.setTextColorId(valueColors.getValue(ViewState.SELECTED))

                holder.arrowUp.visibility = if (dataPosition == 0) View.INVISIBLE else View.VISIBLE
            }
//            workingUnits.bottomUnit -> {
//                layout.setBackgroundResource(backgrounds.getValue(ViewState.SECOND))
//                name.setTextColorId(nameColors.getValue(ViewState.SECOND))
//                value.setTextColorId(valueColors.getValue(ViewState.SECOND))
//                symbol.setTextColorId(valueColors.getValue(ViewState.SECOND))
//                arrowUp.visibility = View.INVISIBLE
//            }
            else -> {
                holder.layout.setBackgroundResource(backgrounds.getValue(ViewState.NORMAL))
                holder.name.setTextColorId(nameColors.getValue(ViewState.NORMAL))
                holder.value.setTextColorId(valueColors.getValue(ViewState.NORMAL))
                holder.symbol.setTextColorId(valueColors.getValue(ViewState.NORMAL))
//                bookmark.setColorFilter(if (units[dataPosition].bookmarked) R.color.colorPrimaryDark else R.color.action, PorterDuff.Mode.SRC_IN)
                holder.arrowUp.visibility = View.INVISIBLE
            }
        }
    }

    private fun updateViewData(holder: ViewHolder, dataPosition: Int) {
        val data: ImperialUnit = getItem(dataPosition)
        holder.publishSubject = publishSubject
        holder.data = data
        holder.name.text = data.unitName.name.lowercase(Locale.getDefault()).replace('_', ' ')
            .replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
        holder.value.text = data.formattedString //valueForDisplay(data.value)
        holder.symbol.text = ImperialSymbol.symbols[data.unitName] ?: ""
    }

    private fun updateControlListeners(holder: ViewHolder, dataPosition: Int) {
        val unit = getItem(dataPosition)
        holder.arrowUp.setOnClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                arrowClickListener(dataPosition, it, unit)
                notifyItemMoved(dataPosition, 0)
            }
        }
        holder.arrowUp.setOnLongClickListener {
            if (dataPosition != 0) {
                units.moveToFrontFrom(dataPosition)
                notifyItemMoved(dataPosition, 0)
                arrowLongClickListener(dataPosition, it, unit)
            }
            dataPosition != 0
        }
        holder.bookmark.setOnClickListener {
            unit.bookmarked = !unit.bookmarked
            bookmarkClickListener(dataPosition, it, unit)
            notifyItemChanged(dataPosition)
        }
    }

    override fun getFilter(): Filter {
        return customFilter
    }

    private val customFilter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val results = FilterResults()
            if (constraint.isNullOrEmpty()) {
                results.values = allUnits
                return results
            } else {
                val filteredUnits = mutableListOf<ImperialUnit>()
                val query = constraint.toString().lowercase(Locale.ROOT)
                val filteredUNames = FuzzySearch.extractSorted(query, names, 50)
                for (n in filteredUNames) {
                    allUnits
                        .find { u -> u.unitName.name.lowercase(Locale.ROOT) == n.string }
                        ?.let {u -> filteredUnits.add(u) }
                }
                results.values = filteredUnits.toTypedArray()
                return results
            }

        }

        override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {
            units = filterResults?.values as Array<ImperialUnit>
            notifyDataSetChanged()
            // TODO: use submitList from AsyncListDiffer (used in ListAdapter)
            //submitList(filterResults?.values as MutableList<String>)
        }

    }


}