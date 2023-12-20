package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SwitchFragment : Fragment(R.layout.fragment_switch) {

    lateinit var categoryAdapter: ImperialCategoryAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_switch, container, false)
        val categoryGrid = view.findViewById<RecyclerView>(R.id.category_grid)

        categoryAdapter = ImperialCategoryAdapter(ImperialCategory.names, activity as MainActivity)
        categoryGrid.adapter = categoryAdapter
        val manager = GridLayoutManager(activity as MainActivity, 2, GridLayoutManager.VERTICAL, false)
        categoryGrid.layoutManager = manager
//        view.findViewById<ConstraintLayout>(R.id.switch_layout).setOnClickListener{
//            (activity as MainActivity).hideTypeSwitcher()
//        }
        return view
    }
}