package io.github.mikolasan.imperialrussia

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
    lateinit var listAdapter: ImperialListAdapter

    private fun setListeners(view: View) {
        unitsList.setOnItemClickListener { _, _, position, _ ->
            val unit = listAdapter.getItem(position) as ImperialUnit
            (activity as MainActivity).onUnitSelected(unit)
        }

        view.run {

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_unit_list, container, false)
        unitsList = view.findViewById(R.id.units_list)
        setListeners(view)
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        listAdapter = (activity as MainActivity).workingUnits.listAdapter
        unitsList.adapter = listAdapter
        listAdapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowClicked(unit)
        }
        listAdapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowLongClicked(unit)
            unitsList.setSelectionAfterHeaderView()
        }
        (activity as MainActivity).restoreAllValues(this)
    }

    override fun onResume() {
        super.onResume()
        listAdapter.notifyDataSetChanged()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
            listAdapter.notifyDataSetChanged()

    }

    override fun onStart() {
        super.onStart()
    }

    fun restoreSelectedUnit(unit: ImperialUnit) {
        (activity as MainActivity).workingUnits.selectedUnit = unit
        //listAdapter.setSelectedUnit(unit)
    }

    fun restoreSecondUnit(unit: ImperialUnit) {
        (activity as MainActivity).workingUnits.secondUnit = unit
        //listAdapter.setSelectedUnit(unit)
    }

    fun onPanelsSwapped() {
        val mainActivity = activity as MainActivity
        val temp = mainActivity.workingUnits.selectedUnit
        mainActivity.workingUnits.selectedUnit = mainActivity.workingUnits.secondUnit
        mainActivity.workingUnits.secondUnit = temp
        listAdapter.notifyDataSetChanged()
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {

    }

    fun updateAllValues(masterUnit: ImperialUnit) {
        listAdapter.updateAllValues(masterUnit, masterUnit.value)
    }

    // TODO: remove '?'
    fun onUnitSelected(selectedUnit: ImperialUnit, secondUnit: ImperialUnit?) {
        (activity as MainActivity).workingUnits.selectedUnit = selectedUnit
        (activity as MainActivity).workingUnits.secondUnit = secondUnit!!
//        listAdapter.setSelectedUnit(selectedUnit)
//        listAdapter.setSecondUnit(secondUnit)
        listAdapter.notifyDataSetChanged()

    }

    fun swapPanels() {
//        listAdapter.swapSelection()
    }
}