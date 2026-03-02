package xyz.neupokoev.forgottenstandards.menu

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import java.util.Locale

class ImperialCategoryAdapter(private val items: List<CategoryMenuItem>,
                              private val publishSubject: MainActivity
)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>()
{
    private var selectedViewHolder: ItemViewHolder? = null

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TYPE_CONVERSION = 2
    }

    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val headerTitle: TextView = view.findViewById(R.id.header_title)
    }

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var category: ImperialUnitCategoryName? = null
        val categoryTitle: TextView = view.findViewById(R.id.category_title)
        val space: ConstraintLayout = view.findViewById(R.id.category_space)
    }

    class ConversionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val conversionTitle: TextView = view.findViewById(R.id.conversion_title)
        val space: ConstraintLayout = view.findViewById(R.id.conversion_space)
        var unit1: ImperialUnitName? = null
        var unit2: ImperialUnitName? = null
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is CategoryMenuItem.Header -> TYPE_HEADER
            is CategoryMenuItem.Item -> TYPE_ITEM
            is CategoryMenuItem.ConversionPair -> TYPE_CONVERSION
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
        when (val item = items[position]) {
            is CategoryMenuItem.Header -> {
                (holder as HeaderViewHolder).headerTitle.text = item.title
            }
            is CategoryMenuItem.Item -> {
                val itemHolder = holder as ItemViewHolder
                itemHolder.categoryTitle.text = item.category.name
                val viewCategory = item.category
                itemHolder.category = viewCategory
                val selectedCategory = publishSubject.workingUnits.selectedCategory
                if (selectedCategory == viewCategory) {
                    selectedViewHolder = itemHolder
                    itemHolder.space.setBackgroundResource(R.drawable.bg_rect_selected)
                } else {
                    itemHolder.space.setBackgroundResource(0)
                }
                itemHolder.space.setOnClickListener {
                    // unselect previous holder
                    selectedViewHolder?.space?.setBackgroundResource(0)
                    selectedViewHolder = itemHolder
                    publishSubject.onCategorySelected(holder.category!!)
                    itemHolder.space.setBackgroundResource(R.drawable.bg_rect_selected)
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
        }
    }
}