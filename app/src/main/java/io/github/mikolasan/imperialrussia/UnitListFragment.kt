package io.github.mikolasan.imperialrussia

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.fragment.app.Fragment
import io.github.mikolasan.ratiogenerator.ImperialUnit

class UnitListFragment : Fragment() {

    private lateinit var unitsList: ListView
    private lateinit var listAdapter: ImperialListAdapter

    private fun setListeners(view: View) {
        unitsList.setOnItemClickListener { _, _, position, _ ->
            val unit = listAdapter.getItem(position) as ImperialUnit
            (activity as MainActivity).onUnitSelected(unit)
        }

        view.run {

        }
    }

    private fun createListAdapter(context: Context): ImperialListAdapter {
        val orderedUnits = (activity as MainActivity).workingUnits.orderedUnits
        val adapter = ImperialListAdapter(context, orderedUnits)
        adapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowClicked(unit)
        }
        adapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowLongClicked(unit)
            unitsList.setSelectionAfterHeaderView()
        }
        return adapter
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_unit_list, container, false)

        listAdapter = createListAdapter(view.context)
        unitsList = view.findViewById(R.id.units_list)
        unitsList.adapter = listAdapter
        setListeners(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).restoreAllValues(this)
    }

    fun restoreSelectedUnit(unit: ImperialUnit) {
        listAdapter.setSelectedUnit(unit)
    }

    fun restoreSecondUnit(unit: ImperialUnit) {
        listAdapter.setSelectedUnit(unit)
    }

    fun onPanelsSwapped() {
        listAdapter.swapSelection()
        listAdapter.notifyDataSetChanged()
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {
        listAdapter.updateAllValues(panel.unit, panel.unit?.value ?: 0.0)
    }

//    fun onTopPanelTextChanged(s: Editable) {
//        listAdapter.updateAllValues(topPanel.unit, topPanel.unit?.value ?: 0.0)
//    }
//
//
//    fun onBottomPanelTextChanged(s: Editable) {
//        listAdapter.updateAllValues(topPanel.unit, topPanel.unit?.value ?: 0.0)
//    }

    fun updateAllValues(masterUnit: ImperialUnit) {
        listAdapter.updateAllValues(masterUnit, masterUnit.value)
    }

    // TODO: remove '?'
    fun onUnitSelected(selectedUnit: ImperialUnit, secondUnit: ImperialUnit?) {
        listAdapter.setSelectedUnit(selectedUnit)
        listAdapter.setSecondUnit(secondUnit)
        listAdapter.notifyDataSetChanged()

    }

    fun swapPanels() {
        listAdapter.swapSelection()
    }
}