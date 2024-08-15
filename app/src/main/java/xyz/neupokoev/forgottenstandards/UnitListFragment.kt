package xyz.neupokoev.forgottenstandards

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mikolasan.ratiogenerator.ImperialUnit


class UnitListFragment : Fragment() {

    private var selectedId: Int = 0
    private val listAdapter: ImperialListAdapter = ImperialListAdapter()
    private lateinit var unitsList: RecyclerView
    private lateinit var bottomPanel: ImperialUnitPanel
    private lateinit var topPanel: ImperialUnitPanel
    private lateinit var selectedPanel: ImperialUnitPanel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).let { mainActivity ->
            mainActivity.setSubscriber(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        unitsList = view.findViewById(R.id.units_list)

        val mainActivity = activity as MainActivity
        setUnits(mainActivity.workingUnits.orderedUnits)
        listAdapter.workingUnits = mainActivity.workingUnits

        unitsList.adapter = listAdapter
        unitsList.layoutManager = LinearLayoutManager(activity)
        unitsList.setItemAnimator(null);

        topPanel = view.findViewById(R.id.convert_to)
        topPanel.visibility = View.GONE
        bottomPanel = view.findViewById(R.id.convert_from)
        bottomPanel.visibility = View.GONE
        selectedPanel = topPanel // init before use

        topPanel.setHintText(view.context.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(view.context.resources.getString(R.string.select_unit_2_hint))



        // This is moved to another fragment (main activity for tablets ??)
//        val searchInput = view.findViewById<EditText>(R.id.search_input)
//        searchInput.addTextChangedListener {
//            listAdapter.filter.filter(it.toString())
//        }
//        searchInput.setOnFocusChangeListener { view, hasFocus ->
//            if (hasFocus) {
//                (activity as MainActivity).showKeyboardButton()
//            }
//        }
//        title = view.findViewById(R.id.unit_type_label)
//        title.text = arguments?.getString("categoryTitle")

//        keyboardView = view.findViewById(R.id.keyboard)
//        keyboardButtonView = view.findViewById(R.id.keyboard_button)

        setListeners(view)

        // This will be too late, DestinationChangedListener is applied before that
        // the only quirk: label in the nav graph must be omitted
        // (activity as AppCompatActivity?)!!.supportActionBar!!.title = "your title"
        return view
    }

    override fun onStart() {
        super.onStart()

        (activity as MainActivity).onCategoryOpened()

//        keyboardFragment = keyboardView.getFragment()
//        keyboardButtonFragment = keyboardButtonView.getFragment()

//        (activity as? MainActivity)?.workingUnits?.let { workingUnits ->
//            restoreSelectedUnit(workingUnits.topUnit)
//            restoreSecondUnit(workingUnits.bottomUnit)
//            val unit = workingUnits.topUnit
//            listAdapter.updateAllValues(unit, unit.value)
//        }

        //(activity as? MainActivity)?.updateKeyboard()
//        (activity as? MainActivity)?.showKeyboard()
    }

    override fun onResume() {
        super.onResume()
        //listAdapter.notifyDataSetChanged()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        //listAdapter.notifyDataSetChanged()

    }

    fun setUnits(units: Array<ImperialUnit>) {
        listAdapter.setUnits(units)
    }

    fun updateAllValues(unit: ImperialUnit, value: Double) {
        listAdapter.updateAllValues(unit, value)
    }

//    fun restoreSelectedUnit(unit: ImperialUnit) {
//        (activity as? MainActivity)?.let { mainActivity ->
//            mainActivity.workingUnits.topUnit = unit
//        }
//        //listAdapter.setSelectedUnit(unit)
//    }
//
//    fun restoreSecondUnit(unit: ImperialUnit) {
//        (activity as? MainActivity)?.let { mainActivity ->
//            mainActivity.workingUnits.bottomUnit = unit
//        }
//        //listAdapter.setSelectedUnit(unit)
//    }

    fun onPanelsSwapped() {
        //listAdapter.notifyDataSetChanged()
    }

    fun onPanelTextChanged(unit: ImperialUnit, value: Double) {
        listAdapter.updateAllValues(unit, value)
    }


    private fun attachKeyboardInputToTopPanel() {
        val mainActivity = activity as MainActivity
        val callable = { unit: ImperialUnit, value: Double ->
            val panel = topPanel
            panel.unit = unit
            panel.setUnitValue(value)
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(topPanel, callable)
    }

    private fun attachKeyboardInputToBottomPanel() {
        val mainActivity = activity as MainActivity
        val callable = { unit: ImperialUnit, value: Double ->
            val panel = bottomPanel
            panel.unit = unit
            panel.setUnitValue(value)
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(bottomPanel, callable)
    }

    private fun listenForKeyboardInputAtTopPanel() {
        val mainActivity = activity as MainActivity
        val callable = { unit: ImperialUnit, value: Double ->
            val panel = topPanel
            val v = convertValue(unit, panel.unit, value)
            panel.setUnitValue(v)
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(topPanel, callable)
    }

    private fun listenForKeyboardInputAtBottomPanel() {
        val mainActivity = activity as MainActivity
        val callable = { unit: ImperialUnit, value: Double ->
            val panel = bottomPanel
            val v = convertValue(unit, panel.unit, value)
            panel.setUnitValue(v)
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(bottomPanel, callable)
    }

    fun showBookmark(unit: ImperialUnit) {
        val mainActivity = activity as MainActivity
        val favorites = mainActivity.workingUnits.favoriteUnits
        if (favorites.isEmpty()) {
            topPanel.visibility = View.GONE
            bottomPanel.visibility = View.GONE
            mainActivity.removeKeyboardInputObserver(topPanel)
            mainActivity.removeKeyboardInputObserver(bottomPanel)
            return
        }

        if (favorites.size == 1
            || favorites.size == 2 && bottomPanel.visibility == View.VISIBLE) {
            topPanel.visibility = View.VISIBLE
            topPanel.activate()
            topPanel.changeUnit(unit)
            topPanel.updateDisplayValue()
            topPanel.setHighlight(true)
            mainActivity.removeKeyboardInputObserver(topPanel)
            attachKeyboardInputToTopPanel()
            mainActivity.onPanelSelected(topPanel)

            mainActivity.removeKeyboardInputObserver(bottomPanel)
            if (bottomPanel.visibility == View.VISIBLE) {
                bottomPanel.setHighlight(false)
                listenForKeyboardInputAtBottomPanel()
            }
        } else {

            bottomPanel.visibility = View.VISIBLE
            bottomPanel.activate()
            bottomPanel.changeUnit(unit)
            bottomPanel.updateDisplayValue()
            bottomPanel.setHighlight(true)
            mainActivity.removeKeyboardInputObserver(bottomPanel)
            attachKeyboardInputToBottomPanel()
            mainActivity.onPanelSelected(bottomPanel)

            mainActivity.removeKeyboardInputObserver(topPanel)
            if (topPanel.visibility == View.VISIBLE) {
                topPanel.setHighlight(false)
                listenForKeyboardInputAtTopPanel()
            }
        }
        listAdapter.notifyItemChanged(selectedId)
    }

    fun removeBookmark(panel: ImperialUnitPanel, unit: ImperialUnit) {
        val mainActivity = activity as MainActivity

        val favorites = mainActivity.workingUnits.favoriteUnits
        if (favorites.isEmpty()) {
            return
        }

        favorites.minusAssign(unit)
        unit.bookmarked = false

        mainActivity.onUnitSelectedInList(unit)

        listAdapter.notifyItemChanged(selectedId)
        listAdapter.restoreUnit(unit)

        panel.visibility = View.GONE
        mainActivity.removeKeyboardInputObserver(panel)

        unitsList.scrollToPosition(0)
        selectedId = 0

    }

    // TODO: remove '?'
//    fun onUnitSelected(selectedUnit: ImperialUnit) {
//        (activity as? MainActivity)?.let { mainActivity ->
//            mainActivity.workingUnits.mainUnit = selectedUnit
//        }
////        listAdapter.setSelectedUnit(selectedUnit)
////        listAdapter.setSecondUnit(secondUnit)
//        //listAdapter.notifyDataSetChanged()
//
//    }

    fun setFilter(query: String?) {
        listAdapter.filter.filter(query)
    }

    private fun setListeners(view: View) {

        val mainActivity = activity as MainActivity
        listAdapter.setOnUnitSelectedListener { i, _, unit ->
            mainActivity.onUnitSelectedInList(unit)
            listAdapter.notifyItemChanged(selectedId)
            listAdapter.notifyItemChanged(i)
            selectedId = i
            mainActivity.removeKeyboardInputObserver(topPanel)
            mainActivity.removeKeyboardInputObserver(bottomPanel)
            topPanel.setHighlight(false)
            bottomPanel.setHighlight(false)
            listenForKeyboardInputAtTopPanel()
            listenForKeyboardInputAtBottomPanel()
        }
        listAdapter.let { listAdapter ->
            listAdapter.setOnArrowClickListener { _: Int, arrow: View, unit: ImperialUnit ->
                arrow.visibility = View.INVISIBLE // hide the arrow
                mainActivity.onArrowClicked(unit)
            }
            listAdapter.setOnArrowLongClickListener { _: Int, arrow: View, unit: ImperialUnit ->
                arrow.visibility = View.INVISIBLE // hide the arrow
                mainActivity.onArrowLongClicked(unit)
                //unitsList.setSelectionAfterHeaderView()
            }
            listAdapter.setOnBookmarkClickListener { _: Int, arrow: View, unit: ImperialUnit ->
                if (mainActivity.workingUnits.favoriteUnits.size == 2) {
                    return@setOnBookmarkClickListener
                }
                if (unit.bookmarked) {
                    mainActivity.workingUnits.favoriteUnits.plusAssign(unit)
                    //mainActivity.onUnitSelected(unit)
                    showBookmark(unit)
                    listAdapter.excludeUnit(unit)
                }
            }
        }

        topPanel.setOnClickListener {
            val mainActivity = activity as MainActivity

            topPanel.setHighlight(true)
            bottomPanel.setHighlight(false)

            mainActivity.removeKeyboardInputObserver(topPanel)
            attachKeyboardInputToTopPanel()
            mainActivity.removeKeyboardInputObserver(bottomPanel)
            listenForKeyboardInputAtBottomPanel()

            mainActivity.onPanelSelected(topPanel)
            listAdapter.notifyItemChanged(selectedId)
        }
        topPanel.bookmark.setOnClickListener {
            removeBookmark(topPanel, topPanel.unit!!)
        }

        bottomPanel.setOnClickListener {
            val mainActivity = activity as MainActivity

            bottomPanel.setHighlight(true)
            topPanel.setHighlight(false)

            mainActivity.removeKeyboardInputObserver(bottomPanel)
            attachKeyboardInputToBottomPanel()
            mainActivity.removeKeyboardInputObserver(topPanel)
            listenForKeyboardInputAtTopPanel()

            mainActivity.onPanelSelected(topPanel)
            listAdapter.notifyItemChanged(selectedId)
        }
        bottomPanel.bookmark.setOnClickListener {
            removeBookmark(bottomPanel, bottomPanel.unit!!)
        }
        // This is replaced by the toolbar
//        view.run {
//            val typeSwitcher: ConstraintLayout = view.findViewById(R.id.unit_type)
//            typeSwitcher.setOnClickListener { v ->
//                (activity as MainActivity).showTypeSwitcher()
//            }
//        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
//                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    // do whatever
//                    val layout = view.findViewById<FragmentContainerView>(R.id.keyboard)
//                    print(layout.visibility)
                }
            }
        }

    }
}