package xyz.neupokoev.forgottenstandards.menu

import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexboxLayoutManager
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.converter.ImperialSymbol
import java.util.Locale

class ImperialCategoryAdapter(private var items: List<CategoryMenuItem>,
                              private val publishSubject: MainActivity
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var selectedViewHolder: ItemViewHolder? = null
    var isSearchMode: Boolean = false

    companion object {
        const val TYPE_HEADER = 0
        const val TYPE_ITEM = 1
        const val TYPE_CONVERSION = 2
        const val TYPE_UNIT = 3
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.findViewById(R.id.header_title)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var category: ImperialUnitCategoryName? = null
        val categoryTitle: TextView = view.findViewById(R.id.category_title)
        val space: ConstraintLayout = view.findViewById(R.id.category_space)
        val divider: View = view.findViewById(R.id.category_divider)
    }

    class ConversionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conversionTitle: TextView = view.findViewById(R.id.conversion_title)
        val space: ConstraintLayout = view.findViewById(R.id.conversion_space)
        var unit1: ImperialUnitName? = null
        var unit2: ImperialUnitName? = null
    }

    class UnitViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val unitTitle: TextView = view.findViewById(R.id.category_title)
        val space: ConstraintLayout = view.findViewById(R.id.category_space)
    }

    fun updateItems(newItems: List<CategoryMenuItem>) {
        items = newItems
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CategoryMenuItem.Header -> TYPE_HEADER
            is CategoryMenuItem.Item -> TYPE_ITEM
            is CategoryMenuItem.ConversionPair -> TYPE_CONVERSION
            is CategoryMenuItem.UnitItem -> TYPE_UNIT
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            TYPE_HEADER -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_header, parent, false)
                HeaderViewHolder(view)
            }
            TYPE_CONVERSION -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.conversion_item, parent, false)
                ConversionViewHolder(view)
            }
            TYPE_UNIT -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.unit_menu_item, parent, false)
                UnitViewHolder(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.category_space, parent, false)
                ItemViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    private fun formatUnitName(name: String): String {
        return name.lowercase(Locale.ROOT).replace('_', ' ').replaceFirstChar { it.uppercase() }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val layoutParams = holder.itemView.layoutParams
        if (layoutParams is FlexboxLayoutManager.LayoutParams) {
            when (items[position]) {
                is CategoryMenuItem.UnitItem -> {
                    layoutParams.flexBasisPercent = -1f // wrap_content
                    layoutParams.flexGrow = 0f
                }
                is CategoryMenuItem.Item -> {
                    if (isSearchMode) {
                        layoutParams.flexBasisPercent = 1.0f // full width
                    } else {
                        layoutParams.flexBasisPercent = 0.4f // half width
                    }
                }
                else -> {
                    layoutParams.flexBasisPercent = 1.0f // full width
                }
            }
        }

        when (val item = items[position]) {
            is CategoryMenuItem.Header -> {
                (holder as HeaderViewHolder).headerTitle.text = item.title
            }
            is CategoryMenuItem.Item -> {
                val itemHolder = holder as ItemViewHolder
                itemHolder.categoryTitle.text = item.category.name
                val viewCategory = item.category
                itemHolder.category = viewCategory
                
                if (isSearchMode) {
                    itemHolder.categoryTitle.gravity = Gravity.START
                    itemHolder.categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    itemHolder.categoryTitle.setTextColor(itemHolder.itemView.context.getColor(R.color.menu_search_header_font))
                    itemHolder.divider.visibility = View.GONE
                    // adjust padding for sub-header feel
                    itemHolder.categoryTitle.setPadding(0, 16, 0, 0)
                } else {
                    itemHolder.categoryTitle.gravity = Gravity.CENTER
                    itemHolder.categoryTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
                    itemHolder.categoryTitle.setTextColor(itemHolder.itemView.context.getColor(R.color.menu_header_font))
                    itemHolder.divider.visibility = View.GONE
                    itemHolder.categoryTitle.setPadding(16, 16, 16, 16)
                    
                    val selectedCategory = publishSubject.workingUnits.selectedCategory
                    if (selectedCategory == viewCategory) {
                        selectedViewHolder = itemHolder
                    } else {
                    }
                }
                
                itemHolder.space.setOnClickListener {
                    if (!isSearchMode) {
                        selectedViewHolder?.space?.setBackgroundResource(0)
                        selectedViewHolder = itemHolder
                    }
                    publishSubject.onCategorySelected(item.category)
                }
            }
            is CategoryMenuItem.ConversionPair -> {
                val convHolder = holder as ConversionViewHolder
                convHolder.unit1 = item.unit1
                convHolder.unit2 = item.unit2
                convHolder.conversionTitle.text = "${formatUnitName(item.unit1.name)} \u2194 ${formatUnitName(item.unit2.name)}"
                convHolder.space.setOnClickListener {
                    publishSubject.onConversionPairSelected(item.unit1, item.unit2)
                }
            }
            is CategoryMenuItem.UnitItem -> {
                val unitHolder = holder as UnitViewHolder
                val unit = item.unit
                val short = ImperialSymbol.symbols[unit.unitName]?.let { s -> " ($s)" } ?: ""
                unitHolder.unitTitle.text = "${formatUnitName(unit.unitName.name)}$short"
                unitHolder.space.setOnClickListener {
                    publishSubject.onUnitSelectedFromSearch(unit)
                }
            }
        }
    }
}