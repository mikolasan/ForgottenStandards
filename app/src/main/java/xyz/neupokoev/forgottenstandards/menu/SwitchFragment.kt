package xyz.neupokoev.forgottenstandards.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R

class SwitchFragment : Fragment(R.layout.fragment_switch) {

    lateinit var categoryAdapter: ImperialCategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_switch, container, false)
        val categoryGrid = view.findViewById<RecyclerView>(R.id.category_grid)

        categoryAdapter = ImperialCategoryAdapter(ImperialCategory.items, activity as MainActivity)
        categoryGrid.adapter = categoryAdapter
        val manager = GridLayoutManager(activity as MainActivity, 2, GridLayoutManager.VERTICAL, false)
        manager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (categoryAdapter.getItemViewType(position)) {
                    0 -> 2 // TYPE_HEADER
                    1 -> 1 // TYPE_ITEM
                    else -> 1
                }
            }
        }
        categoryGrid.layoutManager = manager
//        view.findViewById<ConstraintLayout>(R.id.switch_layout).setOnClickListener{
//            (activity as MainActivity).hideTypeSwitcher()
//        }
        return view
    }
}