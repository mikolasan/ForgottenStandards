package io.github.mikolasan.imperialrussia

import android.content.Context
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.widget.ViewPager2

import java.util.*


class MainActivity : FragmentActivity() {

    private val languageSetting = "language"

    private var newLocale: Locale? = null

    //private var selectedPanel: ImperialUnitPanel? = null
    private lateinit var converterFragment: ConverterFragment
    private lateinit var unitListFragment: UnitListFragment

    val settings: ImperialSettings by lazy {
        ImperialSettings(applicationContext)
    }

    enum class UISide {
        floating,
        list,
        input
    }
    var uiSide = UISide.list



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

    private fun restoreSelectedUnits() {
        val topPanelUnit = settings.restoreTopUnit()
        val bottomPanelUnit = settings.restoreBottomUnit()
        topPanelUnit?.let {
            converterFragment.restoreTopPanel(it)
            unitListFragment.restoreSelectedUnit(it)
        }
        bottomPanelUnit?.let {
            converterFragment.restoreBottomPanel(it)
            unitListFragment.restoreSecondUnit(it)
        }
    }

    private fun restorePanels() {
        restoreSelectedUnits()

        val preferences = getPreferences()
        val topPanelUnit = settings.restoreTopUnit()
        val bottomPanelUnit = settings.restoreBottomUnit()
        val topPanelValue = BasicCalculator(preferences.getString("topPanelValue", "") ?: "").eval()
        val bottomPanelValue = preferences.getString("bottomPanelValue", "") ?: ""
        topPanel.setUnitValue(topPanelValue)
        topPanel.updateDisplayValue()
        listAdapter.updateAllValues(topPanel.unit, topPanelValue)
        bottomPanelUnit?.let { unit ->
            val bottomValue = convertValue(topPanelUnit, unit, topPanelValue)
            bottomPanel.updateDisplayValue()
            val displayValue = bottomPanel.getString()
            if (bottomPanelValue != displayValue) {
                preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
                preferencesEditor.apply()
            }
        }

        selectPanel(topPanel, bottomPanel)
    }

    private fun restoreInputValues() {
        val topPanelValue = settings.restoreTopString()
        val bottomPanelValue = settings.restoreBottomString()
        converterFragment.restoreInputValues(topPanelValue, bottomPanelValue)
    }



    private fun setTopPanel(unit: ImperialUnit, value: Double?) {
        topPanel.changeUnit(unit)
        if (value == null) {
            val topValue = convertValue(bottomPanel.unit, unit, bottomPanel.getValue()
                    ?: 1.0)
            topPanel.setUnitValue(topValue)
        } else {
            topPanel.setUnitValue(value)
        }
        topPanel.updateDisplayValue()

        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(bottomPanel.unit)
        listAdapter.notifyDataSetChanged()

        preferencesEditor.putString("topPanelUnit", unit.unitName.name)
        preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
        preferencesEditor.apply()
        updateRatioLabel()
    }

    private fun setBottomPanel(unit: ImperialUnit) {
        bottomPanel.changeUnit(unit)
        val bottomValue = convertValue(topPanel.unit, unit, topPanel.getValue()
                ?: 1.0)
        bottomPanel.setUnitValue(bottomValue)
        bottomPanel.updateDisplayValue()

        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(topPanel.unit)
        listAdapter.notifyDataSetChanged()

        preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
        preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
        preferencesEditor.apply()
        updateRatioLabel()
    }

    private fun swapPanels() {
        val topUnit = topPanel.unit ?: return
        val bottomUnit = bottomPanel.unit ?: return
        val topValue = topPanel.getValue() ?: return
        val bottomValue = bottomPanel.getValue() ?: return
        val topString = topPanel.getString()
        val bottomString = bottomPanel.getString()
        topPanel.changeUnit(bottomUnit)
        bottomPanel.changeUnit(topUnit)

        topPanel.setUnitValue(bottomValue)
        bottomPanel.setUnitValue(topValue)

        topPanel.setString(bottomString)
        bottomPanel.setString(topString)

        listAdapter.swapSelection()
        updateRatioLabel()
    }

    private fun setListeners() {}

    private fun createNewActivity() {
        setListeners()
        restorePanels()
        //applyLanguageSettings()
    }

    private fun recreatePreviousActivity(savedInstanceState: Bundle) {
        setListeners()
        restoreSelectedUnits()
        listAdapter.notifyDataSetChanged()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
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
        restoreInputValues()
        selectPanel(topPanel, bottomPanel)
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



    override fun attachBaseContext(newBase: Context) {

        val prefs = newBase.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
        val currentLang = prefs.getString(languageSetting, "en") ?: "en" // just want a safe call
        newLocale = Locale(currentLang)

        val context = ImperialContextWrapper.wrap(newBase, newLocale!!)
        super.attachBaseContext(context)
    }

    fun onTopPanelTextChanged(s: Editable) {
        if (bottomPanel.hasUnitAssigned()) {
            bottomPanel.updateDisplayValue()
            preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
        }
        preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
        preferencesEditor.apply()

        unitListFragment.onTopPanelTextChanged(s)
    }

    fun onBottomPanelTextChanged(s: Editable) {
        if (topPanel.hasUnitAssigned()) {
            topPanel.updateDisplayValue()
            preferencesEditor.putString("topPanelValue", topPanel.makeSerializedString())
        }
        preferencesEditor.putString("bottomPanelValue", bottomPanel.makeSerializedString())
        preferencesEditor.apply()

        unitListFragment.onBottomPanelTextChanged(s)

    }

    fun onUnitSelected(unit: ImperialUnit) {
        converterFragment.onUnitSelected(unit)
    }
}

