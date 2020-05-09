package io.github.mikolasan.imperialrussia

import android.os.Bundle
import android.text.Editable
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2
import io.github.mikolasan.ratiogenerator.ImperialUnit

import java.util.*


class MainActivity : FragmentActivity() {

    private val languageSetting = "language"
    private var newLocale: Locale? = null

    //private var selectedPanel: ImperialUnitPanel? = null
    private var converterFragment: ConverterFragment? = null
    private var unitListFragment: UnitListFragment? = null

    private lateinit var settings: ImperialSettings
    lateinit var workingUnits: WorkingUnits

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

        workingUnits.selectedUnit.inputString = settings.restoreTopString()
        workingUnits.selectedUnit.value = BasicCalculator(workingUnits.selectedUnit.inputString).eval()
        workingUnits.secondUnit.inputString = settings.restoreBottomString()
        workingUnits.secondUnit.value = BasicCalculator(workingUnits.secondUnit.inputString).eval()
    }

    fun restoreAllValues(fragment: Fragment) {
        when (fragment) {
            is ConverterFragment -> {
                converterFragment = fragment
                converterFragment?.let {
                    it.restoreTopPanel(workingUnits.selectedUnit)
                    it.restoreBottomPanel(workingUnits.secondUnit)
                    it.selectPanel(it.topPanel, it.bottomPanel)
                    it.displayUnitValues()
                }
            }
            is UnitListFragment -> {
                unitListFragment = fragment
                unitListFragment?.restoreSelectedUnit(workingUnits.selectedUnit)
                unitListFragment?.restoreSecondUnit(workingUnits.secondUnit)
                unitListFragment?.updateAllValues(workingUnits.selectedUnit)
            }
        }

//        bottomPanelUnit?.let { unit ->
//            val bottomValue = convertValue(topPanelUnit, unit, topPanelValue)
//            bottomPanel.updateDisplayValue()
//            val displayValue = bottomPanel.getString()
//            if (bottomPanelValue != displayValue) {
//                preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
//                preferencesEditor.apply()
//            }
//        }
//        selectPanel(topPanel, bottomPanel)
    }

    private fun swapPanels() {
        converterFragment?.swapPanels()
        unitListFragment?.swapPanels()
    }

    private fun createNewActivity() {
        restoreMainUnits()
//        restoreAllValues()
        //applyLanguageSettings()
    }

    private fun recreatePreviousActivity(savedInstanceState: Bundle) {
        restoreMainUnits()
//        restoreAllValues()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        settings = ImperialSettings(applicationContext)

        val viewPager = findViewById<ViewPager2>(R.id.pager)
        viewPager.adapter = ImperialPagerAdapter(this)

        if (savedInstanceState == null) {
            createNewActivity()
        } else {
            recreatePreviousActivity(savedInstanceState)
        }
    }

//    override fun onResume() {
//        super.onResume()
//    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // onRestoreInstanceState works against me. I include layouts and included layouts do not have
        // unique ids. And it calls TextChangedListener on my inputs with wrong values.
        //super.onRestoreInstanceState(savedInstanceState)

//        restoreInputValues()
//        selectPanel(topPanel, bottomPanel)
    }

    override fun onStart() {
        super.onStart()
//
//        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)
//        if (mainLayout is MotionLayout && topPanel.hasUnitAssigned() && bottomPanel.hasUnitAssigned()
//                && mainLayout.startState == R.id.show_list_constraint) {
//            mainLayout.progress = 1.0f
//            uiSide = UISide.input
//        }
    }
//
//    // --- Running ---
//
//    // foreground -> visible process
//    override fun onPause() {
//        super.onPause()
//    }
//
//    // visible -> cached
//    override fun onStop() {
//        super.onStop()
//    }



//    override fun attachBaseContext(newBase: Context) {
//
//        val prefs = newBase.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
//        val currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
//        newLocale = Locale(currentLang)
//
//        val context = ImperialContextWrapper.wrap(newBase, newLocale!!)
//        super.attachBaseContext(context)
//    }

    fun onPanelsSwapped() {
        unitListFragment?.onPanelsSwapped()
    }

    fun onPanelTextChanged(panel: ImperialUnitPanel, s: Editable) {
        if (panel.hasUnitAssigned()) {
            panel.updateDisplayValue()
        }
        converterFragment?.let {
            settings.saveTopString(it.topPanel.makeSerializedString())
            settings.saveBottomString(it.bottomPanel.makeSerializedString())
        }
        unitListFragment?.onPanelTextChanged(panel, s)
    }

//    fun onTopPanelTextChanged(s: Editable) {
//        if (bottomPanel.hasUnitAssigned()) {
//            bottomPanel.updateDisplayValue()
//            settings.saveBottomString()
//            preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
//        }
//        preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
//        preferencesEditor.apply()
//
//        unitListFragment.onTopPanelTextChanged(s)
//    }
//
//    fun onBottomPanelTextChanged(s: Editable) {
//        if (topPanel.hasUnitAssigned()) {
//            topPanel.updateDisplayValue()
//            preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
//        }
//        preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
//        preferencesEditor.apply()
//
//        unitListFragment.onBottomPanelTextChanged(s)
//
//    }

    fun onUnitSelected(unit: ImperialUnit) {
        converterFragment?.onUnitSelected(unit)
    }

    fun onArrowClicked(unit: ImperialUnit) {
        Toast.makeText(applicationContext, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
        if (workingUnits.selectedUnit != unit) {
            swapPanels()
        }
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onArrowLongClicked(unit: ImperialUnit) {
        Toast.makeText(applicationContext, "'${unit.unitName.name}' has been moved to the top", Toast.LENGTH_SHORT).show()
        if (workingUnits.selectedUnit != unit) {
            swapPanels()
        }
        settings.saveNewOrder(workingUnits.orderedUnits)
    }

    fun onTopPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            unitListFragment?.onUnitSelected(unit, it.bottomPanel.unit)
            settings.saveTopUnit(unit, it.topPanel.makeSerializedString())
        }
    }

    fun onBottomPanelUnitChanged(unit: ImperialUnit) {
        converterFragment?.let {
            unitListFragment?.onUnitSelected(unit, it.topPanel.unit)
            settings.saveBottomUnit(unit, it.bottomPanel.makeSerializedString())
        }
    }
}

