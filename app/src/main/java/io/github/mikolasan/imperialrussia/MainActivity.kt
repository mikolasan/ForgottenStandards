package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.viewpager2.widget.ViewPager2
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.noties.markwon.Markwon
import java.io.IOException
import java.util.*


class MainActivity : FragmentActivity() {

    private val languageSetting = "language"
    private var newLocale: Locale? = null

    private var converterFragment: ConverterFragment? = null
    private var unitListFragment: UnitListFragment? = null
    private var switchFragment: SwitchFragment = SwitchFragment()
    private var keyboardFragment: KeyboardFragment? = null
    private var searchFragment: SearchFragment? = null

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

//        try {
//            val label = findViewById<TextView>(R.id.description_text)
//            val unit = workingUnits.topUnit
//            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
//        } catch (e: Exception) {
//            // layout without view pager
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
            settings.saveTopString(it.topPanel.makeSerializedString())
            settings.saveBottomString(it.bottomPanel.makeSerializedString())
        }
    }

    fun onUnitSelected(unit: ImperialUnit) {
        val nav = findNavController(R.id.nav_host_fragment)
        val bundle = bundleOf(
            "category" to unit.category.type.name,
            "topUnit" to workingUnits.topUnit.unitName.name,
            "bottomUnit" to workingUnits.bottomUnit.unitName.name
        )
        nav.navigate(R.id.action_select_unit, bundle)

//        converterFragment?.let {
//            it.onUnitSelected(workingUnits.topUnit, unit)
//        }

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
        Toast.makeText(applicationContext, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
        if (workingUnits.topUnit != unit) {
            swapPanels()
            converterFragment?.selectTopPanel()
        }
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onArrowLongClicked(unit: ImperialUnit) {
        Toast.makeText(applicationContext, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
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
            settings.saveTopUnit(unit, it.topPanel.makeSerializedString())
        }
    }

    fun onBottomPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            workingUnits.bottomUnit = it.bottomPanel.unit!!
            unitListFragment?.onUnitSelected(unit, it.topPanel.unit)
            settings.saveBottomUnit(unit, it.bottomPanel.makeSerializedString())
        }
    }

    fun setSubscriber(fragment: Fragment) {
        when (fragment) {
            is ConverterFragment -> converterFragment = fragment
            is UnitListFragment -> unitListFragment = fragment
            is SwitchFragment -> switchFragment = fragment
            is KeyboardFragment -> keyboardFragment = fragment
            is SearchFragment -> searchFragment = fragment
        }
    }

    fun showTypeSwitcher() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view, switchFragment)
        }
    }

    fun hideTypeSwitcher() {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            remove(switchFragment)
        }
    }

    fun onCategorySelected(category: ImperialUnitCategoryName) {
        val nav = findNavController(R.id.nav_host_fragment)
        val bundle = bundleOf(
            "categoryTitle" to category.name

        )
        nav.navigate(R.id.action_select_category, bundle)
//        nav.navigate(R.id.action_select_category)
//        val categoryTitle = category.name
//        val action = SwitchFragmentDirections.actionSelectCategory(categoryTitle)
//        nav.navigate(action)

        workingUnits.listAdapter.resetAllValues()
        workingUnits.orderedUnits = workingUnits.allUnits.getValue(ImperialUnitType.valueOf(category.name.toUpperCase()))
        workingUnits.listAdapter.units = workingUnits.orderedUnits
        workingUnits.listAdapter.resetAllValues() // why?
        workingUnits.topUnit = workingUnits.orderedUnits[0]
        workingUnits.bottomUnit = workingUnits.orderedUnits[1]
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

//        hideTypeSwitcher()

    }

    fun showUnitList() {
        converterFragment?.let { f ->
            f.view?.visibility = if (f.isVisible) View.GONE else View.VISIBLE
        }
    }

    fun showKeyboard() {
        keyboardFragment?.view?.visibility = View.VISIBLE
        findViewById<FragmentContainerView>(R.id.keyboard_button)?.visibility = View.GONE
    }

    fun showKeyboardButton() {
        keyboardFragment?.view?.visibility = View.GONE
        findViewById<FragmentContainerView>(R.id.keyboard_button)?.visibility = View.VISIBLE
    }

    fun showSearch() {
        
    }
}

