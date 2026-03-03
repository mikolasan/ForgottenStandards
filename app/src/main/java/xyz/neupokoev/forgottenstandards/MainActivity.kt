package xyz.neupokoev.forgottenstandards

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.noties.markwon.Markwon
import xyz.neupokoev.forgottenstandards.converter.ConverterFragment
import xyz.neupokoev.forgottenstandards.converter.ImperialUnitObserver
import xyz.neupokoev.forgottenstandards.converter.ImperialUnitPanel
import xyz.neupokoev.forgottenstandards.converter.UnitListFragment
import xyz.neupokoev.forgottenstandards.menu.ImperialCategory
import xyz.neupokoev.forgottenstandards.menu.ImperialUnitCategoryName
import xyz.neupokoev.forgottenstandards.menu.SwitchFragment
import xyz.neupokoev.forgottenstandards.settings.SettingsFragment
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val languageSetting = "language"
    private var newLocale: Locale? = null

    private var converterFragment: ConverterFragment? = null
    private var unitListFragment: UnitListFragment? = null
    private var switchFragment: SwitchFragment? = null
    private var keyboardView: FragmentContainerView? = null
    private var keyboardFragment: KeyboardFragment? = null
    private var keyboardButtonView: FragmentContainerView? = null
    private var keyboardButtonFragment: KeyboardButtonFragment? = null
    private var searchFragment: SearchFragment? = null
    private var settingsFragment: SettingsFragment? = null
    private var navController: NavController? = null
    private val unitObserver = ImperialUnitObserver(null)

    lateinit var settings: ImperialSettings
    lateinit var workingUnits: WorkingUnits
    lateinit var markwon: Markwon
    private val descriptions by lazy {
        ImperialUnitName.values().map {
            try {
                val inputReader = applicationContext.assets.open(it.name + ".txt")
                return@map inputReader.bufferedReader().readLines().joinToString("\n")
            } catch (e: IOException) {
                return@map ""
            }
        }
    }

    private var isTrackingConversion = false

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML.
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        // Get the SearchView and set the searchable configuration.
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity.
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(true)

            val listener = object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
//                    unitListFragment?.run {
//                        listAdapter.filter.filter(query)
//                    }
//                    return true
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    unitListFragment?.run {
                        setFilter(newText)
                    }
                    return true
                }
            }
            setOnQueryTextListener(listener)
        }

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                navController?.navigate(R.id.action_global_settings)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        settings = ViewModelProviders.of(this).get(ImperialSettings::class.java)
        markwon = Markwon.create(applicationContext)

        if (savedInstanceState == null) {
            createNewActivity()
        } else {
            recreatePreviousActivity(savedInstanceState)
        }

        setContentView(R.layout.activity_main)


        // val navController = findNavController(R.id.nav_host_fragment) // doesn't work because of some stupid shit about lifecycle, see https://issuetracker.google.com/issues/142847973?pli=1
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as? NavHostFragment
        navController = navHostFragment?.navController
        navController?.run {
            // update title
            addOnDestinationChangedListener { controller, destination, arguments ->
                updateFragment(destination.id, destination.label ?: "")
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        if (toolbar != null) {
            setSupportActionBar(toolbar)

            if (navController != null) {
                val appBarConfiguration = AppBarConfiguration(navController!!.graph)
                toolbar.setupWithNavController(navController!!, appBarConfiguration)
            }
        }

        keyboardView = findViewById(R.id.keyboard)
        keyboardButtonView = findViewById(R.id.keyboard_button)

        unitObserver.onErase = {
            isTrackingConversion = false
        }
        unitObserver.addObserver(this) { unit, value ->
            trackConversion(unit, unitObserver.getEditable())
        }
    }

    override fun onStart() {
        super.onStart()

        keyboardFragment = keyboardView?.getFragment()
        keyboardButtonFragment = keyboardButtonView?.getFragment()

        // first time `addOnDestinationChangedListener` is not called
        navController?.currentDestination?.id?.let { destinationId ->
            updateFragment(destinationId, navController?.currentDestination?.label ?: "")
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // onRestoreInstanceState works against me. I include layouts and included layouts do not have
        // unique ids. And it calls TextChangedListener on my inputs with wrong values.
    }

    private fun createNewActivity() {
        restoreMainUnits()
        //applyLanguageSettings()
    }

    private fun recreatePreviousActivity(savedInstanceState: Bundle) {
        restoreMainUnits()
    }

    fun updateFragment(destinationId: Int, title: CharSequence) {
        val setTitleAsCategory = { supportActionBar?.title = workingUnits.selectedCategory?.name }
        val setTitleToDefaultName = { supportActionBar?.title = title }
        when (destinationId) {
            R.id.switchFragment -> {
                setTitleToDefaultName()
                hideKeyboardCompletely()
            }
            R.id.unitListFragment -> {
                setTitleAsCategory()
                showKeyboardButton()
            }
            R.id.converterFragment -> {
                setTitleAsCategory()
                showKeyboard()
            }
            R.id.nutBoltFragment -> {
                setTitleAsCategory()
                hideKeyboardCompletely()
            }
            R.id.settingsFragment -> {
                setTitleToDefaultName()
                hideKeyboardCompletely()
            }
        }
    }

    private fun restoreMainUnits() {

        workingUnits = settings.restoreWorkingUnits()

        val topString = settings.restoreTopString()
        workingUnits.mainUnit.restoreValue(topString, BasicCalculator(topString).eval())
    }

    fun onPanelSelected(panel: ImperialUnitPanel) {
        val selectedUnit = panel.unit!!
        workingUnits.mainUnit = selectedUnit
        unitObserver.setUnitAndUpdateValue(selectedUnit) // change keyboard focus
    }

    private fun trackConversion(unit: ImperialUnit, s: CharSequence) {
        if (s.isEmpty() || s.toString() == "0") {
            isTrackingConversion = false
        } else if (!isTrackingConversion) {
            val favorites = workingUnits.favoriteUnits
            if (favorites.size == 2 && favorites.contains(unit)) {
                val other = favorites.find { it != unit }!!
                settings.incrementConversionCount(unit, other)
                isTrackingConversion = true
            }
        }
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {
        unitListFragment?.onPanelTextChanged(panel.unit!!, panel.unit?.value ?: 0.0)

        trackConversion(panel.unit!!, s)

        converterFragment?.let {
            val oppositePanel = if (it.bottomPanel == panel) it.topPanel else it.bottomPanel
            if (oppositePanel.hasUnitAssigned()) {
                oppositePanel.updateDisplayValue()
            }
            settings.saveTopString(makeSerializedString(it.topPanel.input.editableText))
            settings.saveBottomString(makeSerializedString(it.bottomPanel.input.editableText))
        }
    }

    fun onUnitSelectedInList(unit: ImperialUnit) {
        workingUnits.mainUnit = unit
        unitObserver.setUnitAndUpdateValue(unit) // change keyboard focus
    }

    fun onArrowClicked(unit: ImperialUnit) {
        Toast.makeText(
            applicationContext,
            "'${unit.unitName.name}' has been moved to the top",
            Toast.LENGTH_SHORT
        ).show()
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onArrowLongClicked(unit: ImperialUnit) {
        Toast.makeText(
            applicationContext,
            "'${unit.unitName.name}' has been moved to the top",
            Toast.LENGTH_SHORT
        ).show()
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onTopPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            workingUnits.mainUnit = unit
            settings.saveTopUnit(unit, makeSerializedString(it.topPanel.input.editableText))
        }
    }

    fun onBottomPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            workingUnits.mainUnit = unit
            settings.saveBottomUnit(unit, makeSerializedString(it.bottomPanel.input.editableText))
        }
    }

    fun setSubscriber(fragment: Fragment) {
        when (fragment) {
            is ConverterFragment -> {
                converterFragment = fragment
                val callable = { unit: ImperialUnit, value: Double ->
                    val panel = fragment.selectedPanel
                    panel.unit = unit
                    panel.setUnitValue(value)
                    panel.updateDisplayValue()
                }
                unitObserver.addObserver(fragment, callable)
            }

            is UnitListFragment -> {
                unitListFragment = fragment
                val callable = { unit: ImperialUnit, value: Double ->
                    unitListFragment?.updateAllValues(unit, value) ?: Unit
                }
                unitObserver.addObserver(fragment, callable)
            }

            is SwitchFragment -> switchFragment = fragment
            is KeyboardFragment -> {
                fragment.observer = unitObserver
            }

            is SearchFragment -> searchFragment = fragment
            is SettingsFragment -> settingsFragment = fragment
        }
    }

    fun showTypeSwitcher() {
    }

    fun hideTypeSwitcher() {
    }

    fun onCategorySelected(category: ImperialUnitCategoryName) {
        val type = categoryNameToType(category)
        // category must be updated before navigating to the list because the title depends on it
        workingUnits.selectedCategory = category
        workingUnits.orderedUnits = workingUnits.allUnits.getValue(type)

        settings.saveCategory(category.name)

        // navigation will happen on the next frame anyway
        workingUnits.mainUnit = workingUnits.orderedUnits[0]
        workingUnits.favoriteUnits.forEach { it.bookmarked = false }
        workingUnits.favoriteUnits = mutableListOf()

        if (navController != null) {
            val bundle = bundleOf(
                "categoryTitle" to category.name
            )
            when (type) {
                ImperialUnitType.SLAVIC_CALENDAR -> {
                    navController?.navigate(R.id.slavicCalendarFragment, bundle)
                }
                ImperialUnitType.NUT_AND_BOLT_SIZE -> {
                    navController?.navigate(R.id.action_select_nut_bolt, bundle)
                }
                else -> {
                    navController?.navigate(R.id.action_select_category, bundle)
                }
            }
        } else {
            onCategoryOpened()
        }
    }

    fun onConversionPairSelected(name1: ImperialUnitName, name2: ImperialUnitName) {
        // Find category
        var foundType: ImperialUnitType? = null
        for ((type, category) in ImperialCategory.typeMap) {
            if (category.nameMap.containsKey(name1) && category.nameMap.containsKey(name2)) {
                foundType = type
                break
            }
        }
        val type = foundType ?: return
        val categoryName = ImperialCategory.names.find { categoryNameToType(it) == type } ?: return

        workingUnits.selectedCategory = categoryName
        workingUnits.orderedUnits = workingUnits.allUnits.getValue(type)
        settings.saveCategory(categoryName.name)

        workingUnits.favoriteUnits.forEach { it.bookmarked = false }
        val unit1 = workingUnits.allUnits.getValue(type).find { it.unitName == name1 } ?: return
        val unit2 = workingUnits.allUnits.getValue(type).find { it.unitName == name2 } ?: return
        unit1.bookmarked = true
        unit2.bookmarked = true
        workingUnits.favoriteUnits = mutableListOf(unit1, unit2)
        workingUnits.mainUnit = unit1

        if (navController != null) {
            val bundle = bundleOf(
                "categoryTitle" to categoryName.name
            )
            navController?.navigate(R.id.action_select_category, bundle)
        }
    }

    fun onCategoryOpened() {
        unitObserver.setUnitAndUpdateValue(workingUnits.mainUnit)

        unitListFragment?.run {
            setUnits(workingUnits.orderedUnits)
            updateAllValues(workingUnits.mainUnit, 0.0)
            hidePanels()
            // restore bookmarks if we came from ConversionPair
            val favorites = workingUnits.favoriteUnits
            if (favorites.size == 2) {
                // units in orderedUnits are same objects as in favorites?
                // Probably not if we just re-loaded. But here they should be.
                showBookmark(favorites[0])
                showBookmark(favorites[1])
            }
            selectFirstInList()
        }
    }

    fun showUnitList() {
        converterFragment?.let { f ->
            f.view?.visibility = if (f.isVisible) View.GONE else View.VISIBLE
        }
    }

    fun addKeyboardInputObserver(observer: Any, callable: ObserverCallable) {
        unitObserver.addObserver(observer, callable)
    }

    fun removeKeyboardInputObserver(observer: Any) {
        unitObserver.removeObserver(observer)
    }

    fun showKeyboard() {
        keyboardFragment?.view?.visibility = View.VISIBLE
        keyboardView?.visibility = View.VISIBLE
        keyboardButtonFragment?.view?.visibility = View.GONE
        keyboardButtonView?.visibility = View.GONE

        // hide soft Android keyboard
        // Only runs if there is a view that is currently focused
        this.currentFocus?.let { view ->
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(view.windowToken, 0)
        }

        unitListFragment
            ?.view
            ?.findViewById<EditText>(R.id.search_input)
            ?.clearFocus()
    }

    fun showKeyboardButton() {
        keyboardFragment?.view?.visibility = View.GONE
        keyboardView?.visibility = View.GONE
        keyboardButtonFragment?.view?.visibility = View.VISIBLE
        keyboardButtonView?.visibility = View.VISIBLE
    }

    fun hideKeyboardCompletely() {
        keyboardFragment?.view?.visibility = View.GONE
        keyboardView?.visibility = View.GONE
        keyboardButtonFragment?.view?.visibility = View.GONE
        keyboardButtonView?.visibility = View.GONE
    }
}