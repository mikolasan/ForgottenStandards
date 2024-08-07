package xyz.neupokoev.forgottenstandards

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.Menu
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.noties.markwon.Markwon
import java.io.IOException
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private val languageSetting = "language"
    private var newLocale: Locale? = null

    private var converterFragment: ConverterFragment? = null
    private var unitListFragment: UnitListFragment? = null
    private var switchFragment: SwitchFragment? = null
    private var keyboardFragment: KeyboardFragment? = null
    private var searchFragment: SearchFragment? = null
    private val unitObserver = ImperialUnitObserver(null)

    private lateinit var settings: ImperialSettings
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the options menu from XML.
        val inflater = menuInflater
        inflater.inflate(R.menu.options_menu, menu)

        // Get the SearchView and set the searchable configuration.
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.action_search).actionView as SearchView).apply {
            // Assumes current activity is the searchable activity.
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
            setIconifiedByDefault(false) // Don't iconify the widget. Expand it by default.
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
                        listAdapter.filter.filter(query)
                    }
                    return true
                }
            }
            setOnQueryTextListener(listener)
        }

        return true
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
        val navController = navHostFragment?.navController
        navController?.run {
            val globalUnits = workingUnits
            addOnDestinationChangedListener { controller, destination, arguments ->
                if (destination.id == R.id.unitListFragment
                    || destination.id == R.id.converterFragment
                    || destination.id == R.id.nutBoltFragment
                ) {
                    supportActionBar?.title = globalUnits.selectedCategory?.name
                } else {
                    supportActionBar?.title = destination.label
                }
            }
        }

        val toolbar = findViewById<Toolbar>(R.id.my_toolbar)
        if (toolbar != null && navController != null) {
            setSupportActionBar(toolbar)
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)
        }

//        try {
//            val label = findViewById<TextView>(R.id.description_text)
//            val unit = workingUnits.topUnit
//            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
//        } catch (e: Exception) {
//            // layout without view pager
//        }

    }

    override fun onStart() {
        super.onStart()

//        val listVisible = unitListFragment != null
//        val converterVisible = converterFragment != null
//        if (!converterVisible && listVisible) {
//            // only list - show button
//            unitListFragment?.keyboardButtonFragment?.view?.visibility = View.VISIBLE
//            unitListFragment?.keyboardButtonView?.visibility = View.VISIBLE
//        } else if (converterVisible && !listVisible) {
//            // only converter - show keyboard
//            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
//            converterFragment?.keyboardView?.visibility = View.VISIBLE
//        } else {
//            // show keyboard only in converter
//            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
//            converterFragment?.keyboardView?.visibility = View.VISIBLE
//        }
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

    private fun applyLanguageSettings() {
//        val prefs = getPreferences()
//        var currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
//        val editor = prefs.edit()
//
//        val languageButton = findViewById<Button>(R.id.language)
//
//        languageButton.text = currentLang
//        languageButton.setOnClickListener { view ->
//            val btn = view as Button
//            if (currentLang == "ru") {
//                currentLang = "en"
//            } else if (currentLang == "en") {
//                currentLang = "ru"
//            }
//
//            newLocale = Locale(currentLang)
//            Locale.setDefault(newLocale!!)
//            btn.text = currentLang
//            editor.putString(languageSetting, currentLang)
//            editor.apply()
//
//            val config = resources.configuration
//            config.locale = newLocale
//            resources.updateConfiguration(config, resources.displayMetrics)
//
//
//            listAdapter.notifyDataSetChanged()
//            bottomPanel.updateUnitText()
//            topPanel.updateUnitText()
//        }
    }

    private fun restoreMainUnits() {
        workingUnits = settings.restoreWorkingUnits()

        val topString = settings.restoreTopString()
        workingUnits.topUnit.restoreValue(topString, BasicCalculator(topString).eval())
        val bottomString = settings.restoreBottomString()
        workingUnits.bottomUnit.restoreValue(bottomString, BasicCalculator(bottomString).eval())

    }

    fun swapWorkingUnits() {
        val tmp = workingUnits.topUnit
        workingUnits.topUnit = workingUnits.bottomUnit
        workingUnits.bottomUnit = tmp
    }

    private fun swapPanels() {
        swapWorkingUnits()
        converterFragment?.swapPanels()
    }

    fun onPanelSelected(panel: ImperialUnitPanel) {
        val selectedUnit = panel.unit!!
        if (workingUnits.topUnit == selectedUnit) return

        val tmp = workingUnits.topUnit
        workingUnits.topUnit = selectedUnit
        workingUnits.bottomUnit = tmp

        unitObserver.setUnitAndUpdateValue(selectedUnit) // change keyboard focus

        unitListFragment?.run {
            workingUnits.listAdapter.notifyDataSetChanged()
        }

    }

    fun onPanelsSwapped() {
        swapWorkingUnits()
        unitListFragment?.onPanelsSwapped()
        // update description
        try {
            val label = findViewById<TextView>(R.id.description_text)
            val unit = workingUnits.topUnit
            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
        } catch (e: Exception) {
            // layout without view pager
        }
        converterFragment?.run {
            keyboardFragment?.selectedPanel = selectedPanel
        }
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {
        //unitListFragment?.onPanelTextChanged(panel, s)
        workingUnits.listAdapter.updateAllValues(panel.unit, panel.unit?.value ?: 0.0)

        converterFragment?.let {
            val oppositePanel = if (it.bottomPanel == panel) it.topPanel else it.bottomPanel
            if (oppositePanel.hasUnitAssigned()) {
                oppositePanel.updateDisplayValue()
            }
            settings.saveTopString(makeSerializedString(it.topPanel.input.text))
            settings.saveBottomString(makeSerializedString(it.bottomPanel.input.text))
        }
    }

    fun onUnitSelected(unit: ImperialUnit) {
        var topChanged = true
        converterFragment?.let {
            val selectedUnit = it.selectedPanel.unit ?: return@let
            val topUnit = it.topPanel.unit ?: return@let
            topChanged = (topUnit == selectedUnit)
        }

        if (workingUnits.topUnit == unit) {
            return
        }
        val swapped = workingUnits.bottomUnit == unit
        if (swapped) {
            val tmp = workingUnits.topUnit
            workingUnits.topUnit = unit
            workingUnits.bottomUnit = tmp
        } else if (topChanged) {
            workingUnits.topUnit = unit
            // keep the bottom
        } else {
            val tmp = workingUnits.topUnit
            workingUnits.topUnit = unit
            workingUnits.bottomUnit = tmp
        }

        unitObserver.setUnitAndUpdateValue(unit) // change keyboard focus

        unitListFragment?.run {
            workingUnits.listAdapter.notifyDataSetChanged()
        }

        converterFragment?.let {
            if (swapped) {
//                it.swapPanels()
                val selectedUnit = it.selectedPanel.unit ?: return@let
                val topUnit = it.topPanel.unit ?: return@let
                it.selectedPanel = if (topUnit == selectedUnit) {
                    it.bottomPanel
                } else {
                    it.topPanel
                }
                val otherPanel = if (topUnit == selectedUnit) {
                    it.topPanel
                } else {
                    it.bottomPanel
                }
                it.selectPanel(it.selectedPanel, otherPanel)

            } else {
                val selectedUnit = it.selectedPanel.unit ?: return@let
                val topUnit = it.topPanel.unit ?: return@let
                if (topChanged) {
                    it.restoreTopPanel(unit)
                } else {
                    it.restoreBottomPanel(unit)
                }
                it.displayUnitValues()
            }

        }

        // TODO: only when the second unit is selected as fav
        if (workingUnits.favoritedUnits.size > 1) {
            try {
                val nav = findNavController(R.id.nav_host_fragment)
                val bundle = bundleOf(
                    "category" to unit.category.type.name,
                    "topUnit" to workingUnits.topUnit.unitName.name,
                    "bottomUnit" to workingUnits.bottomUnit.unitName.name
                )
                nav.navigate(R.id.action_select_unit, bundle)
            } catch (e: Exception) {
                // ignore
            }
        }

        settings.saveTopUnit(workingUnits.topUnit, workingUnits.topUnit.formattedString)
        settings.saveBottomUnit(workingUnits.bottomUnit, workingUnits.bottomUnit.formattedString)

//        try {
//            val label = findViewById<TextView>(R.id.description_text)
//            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
//        } catch (e: Exception) {
//            // layout without view pager
//        }
    }

    fun onKeyboardConnected(keyboardFragment: KeyboardFragment) {
        converterFragment?.run {
            keyboardFragment.selectedPanel = selectedPanel
        }
    }

    fun onArrowClicked(unit: ImperialUnit) {
        Toast.makeText(
            applicationContext,
            "'${unit.unitName.name}' has been moved to the top",
            Toast.LENGTH_SHORT
        ).show()
        if (workingUnits.topUnit != unit) {
            swapPanels()
            converterFragment?.selectTopPanel()
        }
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onArrowLongClicked(unit: ImperialUnit) {
        Toast.makeText(
            applicationContext,
            "'${unit.unitName.name}' has been moved to the top",
            Toast.LENGTH_SHORT
        ).show()
        if (workingUnits.topUnit != unit) {
            swapPanels()
            converterFragment?.selectTopPanel()
        }
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onTopPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            workingUnits.topUnit = it.topPanel.unit!!
            unitListFragment?.onUnitSelected(unit, it.bottomPanel.unit)
            settings.saveTopUnit(unit, makeSerializedString(it.topPanel.input.text))
        }
    }

    fun onBottomPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            workingUnits.bottomUnit = it.bottomPanel.unit!!
            unitListFragment?.onUnitSelected(unit, it.topPanel.unit)
            settings.saveBottomUnit(unit, makeSerializedString(it.bottomPanel.input.text))
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
                unitObserver.addObserver(callable)
            }

            is UnitListFragment -> {
                unitListFragment = fragment
                val callable = { unit: ImperialUnit, value: Double ->
                    workingUnits.listAdapter.updateAllValues(unit, value)
                    // notifyDataSetChanged
                }
                unitObserver.addObserver(callable)
            }

            is SwitchFragment -> switchFragment = fragment
            is KeyboardFragment -> {
                fragment.observer = unitObserver
            }

            is SearchFragment -> searchFragment = fragment
        }
    }

    fun showTypeSwitcher() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
//            add(R.id.fragment_container_view, switchFragment)
        }
    }

    fun hideTypeSwitcher() {
//        supportFragmentManager.commit {
//            setReorderingAllowed(true)
//            remove(switchFragment)
//        }
    }

    fun onCategorySelected(category: ImperialUnitCategoryName) {
        val type = categoryNameToType(category)
        // category must be updated before navigating to the list because the title depends on it
        workingUnits.selectedCategory = category

        try {
            val nav = findNavController(R.id.nav_host_fragment)
            val bundle = bundleOf(
                "categoryTitle" to category.name
            )
            if (type == ImperialUnitType.NUT_AND_BOLT) {
                nav.navigate(R.id.action_select_nut_bolt, bundle)
            } else {
                nav.navigate(R.id.action_select_category, bundle)
            }
        } catch (e: Exception) {
            // ignore
        }

//        nav.navigate(R.id.action_select_category)
//        val categoryTitle = category.name
//        val action = SwitchFragmentDirections.actionSelectCategory(categoryTitle)
//        nav.navigate(action)




        if (type == ImperialUnitType.NUT_AND_BOLT) {
            // TODO
            return
        }

        workingUnits.orderedUnits = workingUnits.allUnits.getValue(type)
        workingUnits.topUnit = workingUnits.orderedUnits[0]
        workingUnits.bottomUnit = workingUnits.orderedUnits[1]

        workingUnits.listAdapter.allUnits = workingUnits.orderedUnits
        workingUnits.listAdapter.units = workingUnits.listAdapter.allUnits
        workingUnits.listAdapter.updateAllValues(workingUnits.topUnit, 0.0)

        converterFragment?.run {
            topPanel.changeUnit(workingUnits.topUnit)
            bottomPanel.changeUnit(workingUnits.bottomUnit)
            displayUnitValues()
            selectTopPanel()
        }
        unitListFragment?.run {
            restoreSelectedUnit(workingUnits.topUnit)
            restoreSecondUnit(workingUnits.bottomUnit)
//            setTitle(category.name)
        }

        unitObserver.setUnitAndUpdateValue(workingUnits.topUnit)
//        hideTypeSwitcher()

        settings.saveCategory(category.name)


    }

    fun showUnitList() {
        converterFragment?.let { f ->
            f.view?.visibility = if (f.isVisible) View.GONE else View.VISIBLE
        }
    }

    fun updateKeyboard() {
        val listVisible = unitListFragment != null
        val converterVisible = converterFragment != null
        if (!converterVisible && listVisible) {
            // only list - show button
            unitListFragment?.keyboardButtonFragment?.view?.visibility = View.VISIBLE
            unitListFragment?.keyboardButtonView?.visibility = View.VISIBLE
        } else if (converterVisible && !listVisible) {
            // only converter - show keyboard
            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardView?.visibility = View.VISIBLE
        } else {
            // show keyboard only in converter
            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardView?.visibility = View.VISIBLE
        }
    }

    fun showKeyboard() {
        val listVisible = unitListFragment != null
        val converterVisible = converterFragment != null

        if (!converterVisible && listVisible) {
            unitListFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
            unitListFragment?.keyboardView?.visibility = View.VISIBLE
        } else if (converterVisible && !listVisible) {
            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardView?.visibility = View.VISIBLE
        } else {
            converterFragment?.keyboardFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardView?.visibility = View.VISIBLE
        }

        unitListFragment?.keyboardButtonFragment?.view?.visibility = View.INVISIBLE
        converterFragment?.keyboardButtonFragment?.view?.visibility = View.INVISIBLE

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
        val listVisible = unitListFragment != null
        val converterVisible = converterFragment != null

        if (!converterVisible && listVisible) {
            unitListFragment?.keyboardButtonFragment?.view?.visibility = View.VISIBLE
            unitListFragment?.keyboardButtonView?.visibility = View.VISIBLE
        } else if (converterVisible && !listVisible) {
            converterFragment?.keyboardButtonFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardButtonView?.visibility = View.VISIBLE
        } else {
            converterFragment?.keyboardButtonFragment?.view?.visibility = View.VISIBLE
            converterFragment?.keyboardButtonView?.visibility = View.VISIBLE
        }

        unitListFragment?.keyboardFragment?.view?.visibility = View.GONE
        converterFragment?.keyboardFragment?.view?.visibility = View.GONE
    }

    fun showSearch() {

    }
}

