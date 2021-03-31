package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.text.Editable
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager2.widget.ViewPager2
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.noties.markwon.Markwon
import java.io.IOException
import java.util.*


class MainActivity : FragmentActivity() {

    private val languageSetting = "language"
    private var newLocale: Locale? = null

    private var converterFragment: ConverterFragment? = null
    private var unitListFragment: UnitListFragment? = null
    private var switchFragment: SwitchFragment = SwitchFragment()

    private lateinit var settings: ImperialSettings
    lateinit var workingUnits: WorkingUnits
    lateinit var pagerAdapter: ImperialPagerAdapter
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
        try {
            val viewPager = findViewById<ViewPager2>(R.id.pager)
            viewPager.adapter = pagerAdapter
        } catch (e: Exception) {
            // layout without view pager
        }

        try {
            val label = findViewById<TextView>(R.id.description_text)
            val unit = workingUnits.selectedUnit
            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
        } catch (e: Exception) {
            // layout without view pager
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

        pagerAdapter = ImperialPagerAdapter(this)
        val topString = settings.restoreTopString()
        workingUnits.selectedUnit.restoreValue(topString, BasicCalculator(topString).eval())
        val bottomString = settings.restoreBottomString()
        workingUnits.secondUnit.restoreValue(bottomString, BasicCalculator(bottomString).eval())

        workingUnits.topUnit.restoreValue(topString, BasicCalculator(topString).eval())
        workingUnits.bottomUnit.restoreValue(bottomString, BasicCalculator(bottomString).eval())
    }

    private fun swapPanels() {
        val savedUnit = workingUnits.topUnit
        workingUnits.topUnit = workingUnits.bottomUnit
        workingUnits.bottomUnit = savedUnit
        converterFragment?.swapPanels()
        unitListFragment?.swapPanels()
    }

    fun onPanelsSwapped() {
        unitListFragment?.onPanelsSwapped()
        pagerAdapter.notifyItemChanged(UNIT_LIST_PAGE_ID)
        //pagerAdapter.notifyDataSetChanged()

        try {
            val label = findViewById<TextView>(R.id.description_text)
            val unit = workingUnits.selectedUnit
            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
        } catch (e: Exception) {
            // layout without view pager
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
        converterFragment?.let {
            it.onUnitSelected(workingUnits.selectedUnit, unit)
        }
        try {
            val label = findViewById<TextView>(R.id.description_text)
            markwon.setMarkdown(label, descriptions[unit.unitName.ordinal])
        } catch (e: Exception) {
            // layout without view pager
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

    fun onCategorySelected(category: ImperialUnitCategory) {
        println("Category: ${category.name}")
    }
}

