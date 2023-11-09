package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import io.github.mikolasan.ratiogenerator.ImperialUnit

class UnitListFragment : Fragment() {

    private lateinit var unitsList: ListView
    lateinit var listAdapter: ImperialListAdapter
    private var switchFragment: SwitchFragment? = null
    private lateinit var title: TextView

    private fun setListeners(view: View) {
        unitsList.setOnItemClickListener { _, _, position, _ ->
            val unit = listAdapter.getItem(position) as ImperialUnit
            (activity as MainActivity).onUnitSelected(unit)
        }

        view.run {
            val typeSwitcher: ConstraintLayout = view.findViewById(R.id.unit_type)
            typeSwitcher.setOnClickListener { v ->
                (activity as MainActivity).showTypeSwitcher()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        unitsList = view.findViewById(R.id.units_list)
        title = view.findViewById(R.id.unit_type_label)
        title.text = arguments?.getString("categoryTitle")

        setListeners(view)
        return view
    }

    @Deprecated("Deprecated in Java")
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

        (activity as? MainActivity)?.let { mainActivity ->
            val workingUnits = mainActivity.workingUnits
            restoreSelectedUnit(workingUnits.selectedUnit)
            restoreSecondUnit(workingUnits.secondUnit)
            updateAllValues(workingUnits.selectedUnit)
            mainActivity.setSubscriber(this)
        }
    }

    override fun onResume() {
        super.onResume()
        listAdapter.notifyDataSetChanged()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        listAdapter.notifyDataSetChanged()

    }

    fun restoreSelectedUnit(unit: ImperialUnit) {
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.workingUnits.selectedUnit = unit
        }
        //listAdapter.setSelectedUnit(unit)
    }

    fun restoreSecondUnit(unit: ImperialUnit) {
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.workingUnits.secondUnit = unit
        }
        //listAdapter.setSelectedUnit(unit)
    }

    fun onPanelsSwapped() {
        listAdapter.notifyDataSetChanged()
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {

    }

    fun updateAllValues(masterUnit: ImperialUnit) {
        listAdapter.updateAllValues(masterUnit, masterUnit.value)
    }

    // TODO: remove '?'
    fun onUnitSelected(selectedUnit: ImperialUnit, secondUnit: ImperialUnit?) {
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.workingUnits.selectedUnit = selectedUnit
            mainActivity.workingUnits.secondUnit = secondUnit!!
        }
//        listAdapter.setSelectedUnit(selectedUnit)
//        listAdapter.setSecondUnit(secondUnit)
        listAdapter.notifyDataSetChanged()

    }

    fun setTitle(text: String) {
        title.text = text
    }
}