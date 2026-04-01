package xyz.neupokoev.forgottenstandards.converter

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.TextView
import androidx.appcompat.widget.TooltipCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.MinCookingUnits
import xyz.neupokoev.forgottenstandards.DescriptionFragment
import xyz.neupokoev.forgottenstandards.MainActivity
import xyz.neupokoev.forgottenstandards.R
import xyz.neupokoev.forgottenstandards.convertValueWrapper

class UnitListFragment : Fragment() {

    private var selectedId: Int = 0
    private val listAdapter: ImperialListAdapter = ImperialListAdapter()
    private lateinit var unitsList: RecyclerView
    private lateinit var bottomPanel: ImperialUnitPanel
    private lateinit var topPanel: ImperialUnitPanel
    private lateinit var selectedPanel: ImperialUnitPanel
    private lateinit var favoritesPlaceholder: TextView
    private lateinit var ingredientSelector: View
    private lateinit var ingredientName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as MainActivity).setSubscriber(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list, container, false)
        unitsList = view.findViewById(R.id.units_list)
        favoritesPlaceholder = view.findViewById(R.id.favorites_placeholder)

        val mainActivity = activity as MainActivity
        listAdapter.workingUnits = mainActivity.workingUnits

        unitsList.adapter = listAdapter
        unitsList.layoutManager = LinearLayoutManager(activity)
        unitsList.setItemAnimator(null);

        topPanel = view.findViewById(R.id.convert_to)
        bottomPanel = view.findViewById(R.id.convert_from)
        selectedPanel = topPanel // init before use
        topPanel.setHintText(view.context.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(view.context.resources.getString(R.string.select_unit_2_hint))

        ingredientSelector = view.findViewById(R.id.ingredient_selector)
        ingredientName = view.findViewById(R.id.ingredient_name)

        updateFavoritesUI()
        setListeners(view)
        setIngredientListeners()

        return view
    }

    override fun onStart() {
        super.onStart()

        val mainActivity = activity as MainActivity
        mainActivity.onCategoryOpened()

        updateIngredientSelectorVisibility(mainActivity.workingUnits.selectedCategory?.name)

        if (mainActivity.settings.isFirstRun()) {
            unitsList.postDelayed({
                val viewHolder = unitsList.findViewHolderForAdapterPosition(0) as? ImperialListAdapter.ViewHolder
                viewHolder?.bookmark?.let { bookmarkView ->
                    TooltipCompat.setTooltipText(bookmarkView, getString(R.string.pin_hint))
                    bookmarkView.performLongClick() // Force tooltip display if supported
                    mainActivity.settings.setFirstRunDone()
                }
            }, 500)
        }
    }

    private fun updateIngredientSelectorVisibility(categoryName: String?) {
        if (categoryName == "Cooking") {
            ingredientSelector.visibility = View.VISIBLE
            ingredientName.text = MinCookingUnits.currentIngredient
        } else {
            ingredientSelector.visibility = View.GONE
        }
    }

    private fun setIngredientListeners() {
        ingredientName.setOnClickListener {
            val ingredients = MinCookingUnits.ingredients.keys.toTypedArray()
            AlertDialog.Builder(requireContext())
                .setTitle("Select Ingredient")
                .setItems(ingredients) { _, which ->
                    val selected = ingredients[which]
                    MinCookingUnits.setIngredient(selected)
                    ingredientName.text = selected
                    val mainActivity = activity as MainActivity
                    updateAllValues(mainActivity.workingUnits.mainUnit, mainActivity.workingUnits.mainUnit.value)
                    if (topPanel.visibility == View.VISIBLE) {
                        topPanel.updateDisplayValue()
                    }
                    if (bottomPanel.visibility == View.VISIBLE) {
                        bottomPanel.updateDisplayValue()
                    }
                }
                .show()
        }
    }

    fun setUnits(units: Array<ImperialUnit>) {
        listAdapter.setUnits(units)
    }

    fun updateAllValues(unit: ImperialUnit, value: Double) {
        listAdapter.updateAllValues(unit, value)
    }

    fun selectFirstInList() {
        unitsList.scrollToPosition(0)
        selectedId = 0
    }

    private fun updateFavoritesUI() {
        val mainActivity = activity as MainActivity
        val favorites = mainActivity.workingUnits.favoriteUnits
        if (favorites.isEmpty()) {
            favoritesPlaceholder.visibility = View.VISIBLE
            topPanel.visibility = View.GONE
            bottomPanel.visibility = View.GONE
        } else {
            favoritesPlaceholder.visibility = View.GONE
        }
    }

    fun hidePanels() {
        val mainActivity = activity as MainActivity
        topPanel.visibility = View.GONE
        bottomPanel.visibility = View.GONE
        mainActivity.removeKeyboardInputObserver(topPanel)
        mainActivity.removeKeyboardInputObserver(bottomPanel)
        updateFavoritesUI()
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
            panel.unit?.apply {
                convertValueWrapper(unit, value, this)
            }
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(topPanel, callable)
    }

    private fun listenForKeyboardInputAtBottomPanel() {
        val mainActivity = activity as MainActivity
        val callable = { unit: ImperialUnit, value: Double ->
            val panel = bottomPanel
            panel.unit?.apply {
                convertValueWrapper(unit, value, this)
            }
            panel.updateDisplayValue()
        }
        mainActivity.addKeyboardInputObserver(bottomPanel, callable)
    }

    fun showBookmark(unit: ImperialUnit) {
        val mainActivity = activity as MainActivity
        val favorites = mainActivity.workingUnits.favoriteUnits
        
        updateFavoritesUI()

        if (favorites.size == 1
            || favorites.size == 2 && bottomPanel.visibility == View.VISIBLE
        ) {
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

        updateFavoritesUI()
        selectFirstInList()
    }

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
            }
            listAdapter.setOnBookmarkClickListener { _: Int, arrow: View, unit: ImperialUnit ->
                if (mainActivity.workingUnits.favoriteUnits.size == 2) {
                    return@setOnBookmarkClickListener
                }
                if (unit.bookmarked) {
                    mainActivity.workingUnits.favoriteUnits.plusAssign(unit)
                    showBookmark(unit)
                    listAdapter.excludeUnit(unit)
                }
            }
            listAdapter.setOnInfoClickListener { _: Int, _: View, unit: ImperialUnit ->
                val descriptionFragment = DescriptionFragment.newInstance(unit.unitName)
                descriptionFragment.show(parentFragmentManager, "unit_description")
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

            mainActivity.onPanelSelected(bottomPanel)
            listAdapter.notifyItemChanged(selectedId)
        }
        bottomPanel.bookmark.setOnClickListener {
            removeBookmark(bottomPanel, bottomPanel.unit!!)
        }

        view.viewTreeObserver.addOnGlobalLayoutListener {
            object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                }
            }
        }
    }
}