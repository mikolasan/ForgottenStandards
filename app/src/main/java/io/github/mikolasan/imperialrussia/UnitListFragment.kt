package io.github.mikolasan.imperialrussia

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class UnitListFragment : Fragment() {

    private lateinit var unitsList: ListView
    val listAdapter by lazy {
        createListAdapter(capplicationContext)
    }

    // TODO:
    private fun createListAdapter(context: Context): ImperialListAdapter {
        val orderedUnits = restoreOrderedUnits()
        val adapter = ImperialListAdapter(context, orderedUnits)
        adapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            Toast.makeText(context, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { _, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
        }
        adapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            Toast.makeText(context, "'${unit.unitName.name}' has been moved to the top + scroll", Toast.LENGTH_SHORT).show()
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { _, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
            unitsList.setSelectionAfterHeaderView()
        }
        return adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.unit_list, container, false)

        unitsList = view.findViewById(R.id.units_list)
        unitsList.adapter = listAdapter

        return view
    }
}