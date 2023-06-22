package io.github.mikolasan.imperialrussia

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class SwitchFragment : Fragment(R.layout.fragment_switch) {

    private lateinit var viewModel: SwitchViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_switch, container, false)
        val categoryGrid = view.findViewById<RecyclerView>(R.id.category_grid)
        val categories: Array<ImperialUnitCategoryName> = arrayOf(
                ImperialUnitCategoryName("Length"),
                ImperialUnitCategoryName("Area"),
                ImperialUnitCategoryName("Volume"),
                ImperialUnitCategoryName("Temperature"),
                ImperialUnitCategoryName("Weight"),
                ImperialUnitCategoryName("Speed"),
                ImperialUnitCategoryName("Time"),
                // Physics
                ImperialUnitCategoryName("Pressure"),
                ImperialUnitCategoryName("Power"),
                ImperialUnitCategoryName("Energy"), // aka Work
                ImperialUnitCategoryName("Force"),
                ImperialUnitCategoryName("Resistance"), // ?

                ImperialUnitCategoryName("Currency"),

                ImperialUnitCategoryName("Storage"),
                ImperialUnitCategoryName("Fuel"),
                ImperialUnitCategoryName("Angle")
        )
        categoryGrid.adapter = ImperialCategoryAdapter(categories, activity as MainActivity);
        val manager = GridLayoutManager(activity as MainActivity, 3, GridLayoutManager.VERTICAL, false)
        categoryGrid.layoutManager = manager
        view.findViewById<ConstraintLayout>(R.id.switch_layout).setOnClickListener{
            (activity as MainActivity).hideTypeSwitcher()
        }
        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(SwitchViewModel::class.java)
        // TODO: Use the ViewModel
    }

}