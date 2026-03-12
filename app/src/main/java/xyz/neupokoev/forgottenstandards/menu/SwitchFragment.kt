package xyz.neupokoev.forgottenstandards.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.willowtreeapps.fuzzywuzzy.ToStringFunction
import com.willowtreeapps.fuzzywuzzy.diffutils.FuzzySearch
import com.willowtreeapps.fuzzywuzzy.diffutils.algorithms.WeightedRatio
import io.github.mikolasan.ratiogenerator.ImperialUnit
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.categoryNameToType
import java.util.Locale

class SwitchFragment : Fragment(R.layout.fragment_switch) {

    lateinit var categoryAdapter: ImperialCategoryAdapter
    private var allUnits: List<ImperialUnit> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as MainActivity).setSubscriber(this)
        
        allUnits = ImperialCategory.typeMap.values.flatMap { it.units }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_switch, container, false)
        val categoryGrid = view.findViewById<RecyclerView>(R.id.category_grid)

        val mainActivity = activity as MainActivity
        val items = ImperialCategory.getItemsWithFrequent(mainActivity.settings)

        categoryAdapter = ImperialCategoryAdapter(items, mainActivity)
        categoryGrid.adapter = categoryAdapter
        
        // 4 columns for better wrapping of units in search results
        val totalSpans = 4
        val manager = GridLayoutManager(activity as MainActivity, totalSpans, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                val viewType = categoryAdapter.getItemViewType(position)
                return when (viewType) {
                    ImperialCategoryAdapter.TYPE_HEADER -> totalSpans
                    ImperialCategoryAdapter.TYPE_CONVERSION -> totalSpans
                    ImperialCategoryAdapter.TYPE_ITEM -> {
                        if (categoryAdapter.isSearchMode) totalSpans else totalSpans / 2
                    }
                    ImperialCategoryAdapter.TYPE_UNIT -> 1 // 4 units per line
                    else -> 1
                }
            }
        }
        categoryGrid.layoutManager = manager
        return view
    }

    class UnitToString : ToStringFunction<ImperialUnit> {
        override fun apply(item: ImperialUnit): String {
            return item.unitName.name.lowercase(Locale.ROOT)
        }
    }

    class CategoryToString : ToStringFunction<CategoryMenuItem.Item> {
        override fun apply(item: CategoryMenuItem.Item): String {
            return item.category.name.lowercase(Locale.ROOT)
        }
    }

    fun setFilter(query: String?) {
        val mainActivity = activity as MainActivity
        if (query.isNullOrBlank()) {
            categoryAdapter.isSearchMode = false
            categoryAdapter.updateItems(ImperialCategory.getItemsWithFrequent(mainActivity.settings))
            return
        }

        categoryAdapter.isSearchMode = true
        val queryLower = query.lowercase(Locale.ROOT)

        // 1. Fuzzy match units
        val matchedUnits = FuzzySearch.extractSorted(
            queryLower,
            allUnits,
            UnitToString(),
            WeightedRatio(),
            50)
            .map { it.referent }
            .groupBy { it.unitType }

        // 2. Group categories that match by name
        val matchedCategories = FuzzySearch.extractSorted(
            queryLower,
            ImperialCategory.items.filterIsInstance<CategoryMenuItem.Item>(),
            CategoryToString(),
            WeightedRatio(),
            60)
            .map { it.referent.category }
            .toSet()

        val newItems = mutableListOf<CategoryMenuItem>()
        var lastHeader: CategoryMenuItem.Header? = null

        ImperialCategory.items.forEach { item ->
            when (item) {
                is CategoryMenuItem.Header -> {
                    lastHeader = item
                }
                is CategoryMenuItem.Item -> {
                    val type = categoryNameToType(item.category)
                    val unitsForThisCategory = matchedUnits[type] ?: emptyList()
                    val categoryNameMatches = matchedCategories.contains(item.category)

                    if (categoryNameMatches || unitsForThisCategory.isNotEmpty()) {
                        lastHeader?.let {
                            newItems.add(it)
                            lastHeader = null
                        }
                        newItems.add(item)
                        unitsForThisCategory.forEach { unit ->
                            newItems.add(CategoryMenuItem.UnitItem(unit))
                        }
                    }
                }
                else -> {}
            }
        }

        categoryAdapter.updateItems(newItems)
    }
}