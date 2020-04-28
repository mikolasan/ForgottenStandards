package io.github.mikolasan.imperialrussia

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class UnitListFragment : Fragment() {

    private lateinit var unitsList: ListView
    private lateinit var listAdapter: ImperialListAdapter

    private fun setListeners(view: View) {
        unitsList.setOnItemClickListener { _, _, position, _ ->
            val unit = listAdapter.getItem(position) as ImperialUnit
            (activity as MainActivity).onUnitSelected(unit)
            if (!topPanel.hasUnitAssigned()) {
                topPanel.activate()
                setTopPanel(unit, 1.0)
                selectPanel(topPanel, bottomPanel)
            } else if (!bottomPanel.hasUnitAssigned() && topPanel.unit != unit) {
                bottomPanel.activate()
                setBottomPanel(unit)
                selectPanel(bottomPanel, topPanel)
            } else {
                if (selectedPanel == topPanel) {
                    if (bottomPanel.unit != unit) {
                        setTopPanel(unit, null)
                    } else if (topPanel.unit != unit) {
                        selectPanel(bottomPanel, topPanel)
                        listAdapter.swapSelection()
                        listAdapter.notifyDataSetChanged()
                    }
                } else if (selectedPanel == bottomPanel) {
                    if (topPanel.unit != unit) {
                        setBottomPanel(unit)
                    } else if (bottomPanel.unit != unit){
                        selectPanel(topPanel, bottomPanel)
                        listAdapter.swapSelection()
                        listAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        view.run {

        }
    }
    // TODO:
    private fun createListAdapter(context: Context): ImperialListAdapter {
        val orderedUnits = settings.restoreOrderedUnits()
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
        val view = inflater.inflate(R.layout.fragment_unit_list, container, false)

        val listAdapter = createListAdapter(view.context)
        unitsList = view.findViewById(R.id.units_list)
        unitsList.adapter = listAdapter
        setListeners(view)
        return view
    }

    fun restoreSelectedUnit(unit: ImperialUnit) {
        listAdapter.setSelectedUnit(unit)
    }

    fun restoreSecondUnit(unit: ImperialUnit) {
        listAdapter.setSelectedUnit(unit)
    }

    fun onTopPanelTextChanged(s: Editable) {
        listAdapter.updateAllValues(topPanel.unit, topPanel.unit?.value ?: 0.0)
    }

    fun onBottomPanelTextChanged(s: Editable) {
        listAdapter.updateAllValues(topPanel.unit, topPanel.unit?.value ?: 0.0)
    }
}