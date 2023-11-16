package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ListView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import io.github.mikolasan.ratiogenerator.ImperialUnit

class UnitListFragment : Fragment() {

    private lateinit var unitsList: ListView
    lateinit var listAdapter: ImperialListAdapter
    private var switchFragment: SwitchFragment? = null
    private lateinit var title: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as? MainActivity)?.setSubscriber(this)

        listAdapter = (activity as MainActivity).workingUnits.listAdapter
        listAdapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowClicked(unit)
        }
        listAdapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            (activity as MainActivity).onArrowLongClicked(unit)
            unitsList.setSelectionAfterHeaderView()
        }
        listAdapter.setOnBookmarkClickListener { _: Int, arrow: View, unit: ImperialUnit ->

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        unitsList = view.findViewById(R.id.units_list)
        unitsList.adapter = listAdapter

        title = view.findViewById(R.id.unit_type_label)
        title.text = arguments?.getString("categoryTitle")

        setListeners(view)
        return view
    }

    override fun onStart() {
        super.onStart()

        (activity as? MainActivity)?.workingUnits?.let { workingUnits ->
            restoreSelectedUnit(workingUnits.topUnit)
            restoreSecondUnit(workingUnits.bottomUnit)
            val unit = workingUnits.topUnit
            listAdapter.updateAllValues(unit, unit.value)
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
            mainActivity.workingUnits.topUnit = unit
        }
        //listAdapter.setSelectedUnit(unit)
    }

    fun restoreSecondUnit(unit: ImperialUnit) {
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.workingUnits.bottomUnit = unit
        }
        //listAdapter.setSelectedUnit(unit)
    }

    fun onPanelsSwapped() {
        listAdapter.notifyDataSetChanged()
    }

    // TODO: remove '?'
    fun onUnitSelected(selectedUnit: ImperialUnit, secondUnit: ImperialUnit?) {
        (activity as? MainActivity)?.let { mainActivity ->
            mainActivity.workingUnits.topUnit = selectedUnit
            mainActivity.workingUnits.bottomUnit = secondUnit!!
        }
//        listAdapter.setSelectedUnit(selectedUnit)
//        listAdapter.setSecondUnit(secondUnit)
        listAdapter.notifyDataSetChanged()

    }

    fun setTitle(text: String) {
        title.text = text
    }

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

        view.viewTreeObserver.addOnGlobalLayoutListener {
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
//                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // do whatever
                    val layout = view.findViewById<FragmentContainerView>(R.id.keyboard)
                    print(layout.visibility)
                }
            }
        }

    }
}