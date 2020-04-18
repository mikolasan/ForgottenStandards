package io.github.mikolasan.imperialrussia

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.Button
import android.widget.ListView
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import java.util.*



class MeasuringActivity : Activity() {

    private val languageSetting = "language"
    private val preferencesFile = "ImperialRussiaPref.1"
    private var newLocale: Locale? = null

    private var selectedPanel: ImperialUnitPanel? = null
    private lateinit var unitsList: ListView

    val bottomPanel by lazy {
        val bottomLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)
        ImperialUnitPanel(bottomLayout)
    }
    val topPanel by lazy {
        val topLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        ImperialUnitPanel(topLayout)
    }
    val listAdapter by lazy {
        createListAdapter(applicationContext)
    }
    val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy getPreferences().edit()
    }

    private fun getPreferences(): SharedPreferences {
        return applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
    }

    // TODO:
    private fun createListAdapter(context: Context): ImperialListAdapter {
        val orderedUnits = restoreOrderedUnits()
        val adapter = ImperialListAdapter(context, orderedUnits)
        adapter.setOnArrowClickListener { position: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { i, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
        }
        adapter.setOnArrowLongClickListener { position: Int, arrow: View, unit: ImperialUnit ->
            arrow.visibility = View.INVISIBLE // hide the arrow
            if (topPanel.unit != unit) {
                swapPanels()
            }
            LengthUnits.lengthUnits.forEachIndexed { i, u ->
                val unitName = u.unitName.name
                val settingName = "unit${unitName}Position"
                preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
            }
            preferencesEditor.apply()
            unitsList.setSelectionAfterHeaderView()
        }
        return adapter
    }

    private fun restoreUnit(storedName: String): ImperialUnit? {
        if (storedName != "") {
            val unitName = ImperialUnitName.valueOf(storedName)
            return LengthUnits.imperialUnits[unitName]
        }
        return null
    }

    private fun createNewActivity() {
        setListeners()
        restorePanels()
        //applyLanguageSettings()
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

    private fun restoreOrderedUnits(): MutableList<ImperialUnit> {
        val preferences = getPreferences()

        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")

        val orderedUnits = LengthUnits.lengthUnits.toMutableList()
        LengthUnits.lengthUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            val p = preferences.getInt(settingName, i)
            if (preferences.contains(settingName)) {
                preferencesEditor.putInt(settingName, i)
            }
            orderedUnits[p] = u
        }
        preferencesEditor.apply()
        bottomPanelUnit?.let {
            if (orderedUnits[1] != it) {
                orderedUnits.moveToFront(it)
            }
        }
        topPanelUnit?.let {
            if (orderedUnits[1] != it) {
                orderedUnits.moveToFront(it)
            }
        }

        return orderedUnits
    }

    private fun restorePanels() {
        val preferences = getPreferences()
        val topPanelValue = BasicCalculator(preferences.getString("topPanelValue", "") ?: "").eval()
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")
        topPanelUnit?.let {
            setTopPanel(it, topPanelValue)
        }
        bottomPanelUnit?.let {
            setBottomPanel(it, null)
        }
        selectPanel(topPanel, bottomPanel)
        listAdapter.swapSelection()

//        listAdapter.setCurrentValue(topPanel.unit, topPanelValue)
    }

    private fun restorePanelSelection() {
        val preferences = getPreferences()
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")
        topPanelUnit?.let {
            topPanel.changeUnit(it)
            listAdapter.setSelectedUnit(it)
        }
        bottomPanelUnit?.let {
            bottomPanel.changeUnit(it)
            listAdapter.setSecondUnit(it)
        }
        listAdapter.notifyDataSetChanged()
        selectPanel(topPanel, bottomPanel)
    }

    private fun restorePanelValues() {
        val preferences = getPreferences()
        val topPanelValue = preferences.getString("topPanelValue", "") ?: ""
        val bottomPanelValue = preferences.getString("bottomPanelValue", "") ?: ""
        topPanel.setString(topPanelValue)
        bottomPanel.setString(bottomPanelValue)
    }

    private fun setTopPanel(unit: ImperialUnit, value: Double?) {
        topPanel.changeUnit(unit)
        if (value == null) {
            val topValue = convertValue(bottomPanel.unit, unit, bottomPanel.getValue()
                    ?: 1.0)
            topPanel.setValue(topValue)
        } else {
            topPanel.setValue(value)
        }
        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(bottomPanel.unit)
        listAdapter.notifyDataSetChanged()
        preferencesEditor.putString("topPanelUnit", unit.unitName.name)
        preferencesEditor.putString("topPanelValue", topPanel.getString())
        preferencesEditor.apply()
    }

    private fun setBottomPanel(unit: ImperialUnit, value: Double? = null) {
        bottomPanel.changeUnit(unit)
        if (value == null) {
            val bottomValue = convertValue(topPanel.unit, unit, topPanel.getValue()
                    ?: 1.0)
            bottomPanel.setValue(bottomValue)
        } else {
            val topValue = convertValue(unit, topPanel.unit, value)
            topPanel.setValue(topValue)
        }
        listAdapter.setSelectedUnit(unit)
        listAdapter.setSecondUnit(topPanel.unit)
        listAdapter.notifyDataSetChanged()
        preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
        preferencesEditor.putString("bottomPanelValue", bottomPanel.getString())
        preferencesEditor.apply()
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

        topPanel.setValue(bottomValue)
        bottomPanel.setValue(topValue)

        topPanel.setString(bottomString)
        bottomPanel.setString(topString)

        listAdapter.swapSelection()
    }

    fun setCursor(editText: EditText?) {
        editText?.isCursorVisible = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            editText?.textCursorDrawable = resources.getDrawable(R.drawable.ic_cursor)
        }
        editText?.setSelection(editText.text.length)
    }

    private fun selectPanel(new: ImperialUnitPanel?, old: ImperialUnitPanel?) {
        selectedPanel = new
        new?.let {
            if (new.unit?.value?.compareTo(0.0) == 0) {
                new.setString("")
            }
            new.setHighlight(true)
            setCursor(new.input)
        }
        old?.let {
            if (old.getString() == "") {
                old.setValue(0.0)
            }
            old.setHighlight(false)
            old.input.isCursorVisible = false
        }
    }

    private fun setListeners() {
        unitsList.setOnItemClickListener { _, _, position, id ->
            val unit = listAdapter.getItem(position) as? ImperialUnit
            unit?.let {
                if (!topPanel.isActivated()) {
                    if (bottomPanel.unit != unit) {
                        selectPanel(topPanel, bottomPanel)
                        setTopPanel(unit, 1.0)
                    }
                } else if (!bottomPanel.isActivated()) {
                    if (topPanel.unit != unit) {
                        selectPanel(bottomPanel, topPanel)
                        setBottomPanel(unit, null)
                    }
                } else {
                    if (selectedPanel == topPanel) {
                        if (topPanel.unit != unit){
                            selectPanel(bottomPanel, topPanel)
                            listAdapter.swapSelection()
                            listAdapter.notifyDataSetChanged()
                        }
                    } else if (selectedPanel == bottomPanel) {
                        if (topPanel.unit != unit) {
                            setBottomPanel(unit, null)
                        } else if (bottomPanel.unit != unit){
                            selectPanel(topPanel, bottomPanel)
                            listAdapter.swapSelection()
                            listAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }

        val topPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != topPanel) {
                selectPanel(topPanel, bottomPanel)
                listAdapter.swapSelection()
                listAdapter.notifyDataSetChanged()
            }
        }
        val bottomPanelOnClickListener: (View) -> Unit = {
            if (selectedPanel != bottomPanel) {
                selectPanel(bottomPanel, topPanel)
                listAdapter.swapSelection()
                listAdapter.notifyDataSetChanged()
            }
        }
        val mainLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.motion_layout)
        val topLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_to)
        val bottomLayout: ConstraintLayout = findViewById<ConstraintLayout>(R.id.convert_from)

        val topInput = topPanel.input
        val bottomInput = bottomPanel.input
        topLayout.setOnClickListener(topPanelOnClickListener)
        bottomLayout.setOnClickListener(bottomPanelOnClickListener)
        topInput.setOnClickListener(topPanelOnClickListener)
        bottomInput.setOnClickListener(bottomPanelOnClickListener)
        topInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel?.input != topInput)
                    return

                if (mainLayout is MotionLayout && mainLayout.progress.equals(1.0))
                    return

                s?.let {
                    val inputValue = BasicCalculator(s.toString()).eval()
                    val convertedValue = convertValue(topPanel.unit, bottomPanel.unit, inputValue)
                    bottomInput.text = valueForDisplay(convertedValue)
                    listAdapter.setCurrentValue(topPanel.unit, inputValue)
                    topInput.setSelection(topInput.text.length)
                    preferencesEditor.putString("topPanelValue", topPanel.getString())
                    preferencesEditor.putString("bottomPanelValue", bottomPanel.getString())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        bottomInput.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (selectedPanel?.input != bottomInput)
                    return

                if (mainLayout is MotionLayout && mainLayout.progress.equals(1.0))
                    return

                s?.let {
                    val inputValue = BasicCalculator(s.toString()).eval()
                    val convertedValue = convertValue(bottomPanel.unit, topPanel.unit, inputValue)
                    topInput.text = valueForDisplay(convertedValue)
                    listAdapter.setCurrentValue(bottomPanel.unit, inputValue)
                    bottomInput.setSelection(bottomInput.text.length)
                    preferencesEditor.putString("topPanelValue", topPanel.getString())
                    preferencesEditor.putString("bottomPanelValue", bottomPanel.getString())
                    preferencesEditor.apply()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        val digitButtonOnClickListener: (View) -> Unit = { view ->
            val button = view as Button
            val text = selectedPanel?.getString() ?: ""
            if (text.length <= maxDisplayLength)
                selectedPanel?.appendString(button.text[0])
        }
        findViewById<DigitButton>(R.id.digit_1).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_2).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_3).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_4).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_5).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_6).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_7).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_8).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_9).setOnClickListener(digitButtonOnClickListener)
        findViewById<DigitButton>(R.id.digit_0).setOnClickListener(digitButtonOnClickListener)

        val operations = setOf('÷', '×', '+', '-')
        fun addSymbol(sym: Char) {
            selectedPanel?.appendString(sym, operations)
        }
        findViewById<OperationButton>(R.id.op_back).setOnClickListener { selectedPanel?.dropLastChar() }
        findViewById<OperationButton>(R.id.op_clear).setOnClickListener { selectedPanel?.setString("") }
        findViewById<OperationButton>(R.id.op_mult).setOnClickListener { addSymbol('×') }
        findViewById<OperationButton>(R.id.op_div).setOnClickListener { addSymbol('÷') }
        findViewById<OperationButton>(R.id.op_plus).setOnClickListener { addSymbol('+') }
        findViewById<OperationButton>(R.id.op_minus).setOnClickListener { addSymbol('-') }
        findViewById<OperationButton>(R.id.op_dot).setOnClickListener { addSymbol('.') }
        findViewById<OperationButton>(R.id.op_eval).setOnClickListener { selectedPanel?.evaluateString() }
    }

    private fun recreatePreviousActivity(savedInstanceState: Bundle) {
        setListeners()
        restorePanelSelection()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_measuring)
        topPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_hint))
        bottomPanel.setHintText(applicationContext.resources.getString(R.string.select_unit_2_hint))

        unitsList = findViewById(R.id.units_list)
        unitsList.adapter = listAdapter

        if (savedInstanceState == null) {
            createNewActivity()
        } else {
            recreatePreviousActivity(savedInstanceState)
        }
    }

    override fun onResume() {
        super.onResume()

        restorePanelValues()

        //        if (mainLayout is MotionLayout && topPanel.isActivated() && bottomPanel.isActivated()) {
//            mainLayout.transitionToEnd()
//        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        // onRestoreInstanceState works against me. I include layouts and included layouts do not have
        // unique ids. And it calls TextChangedListener on my inputs with wrong values.
        //super.onRestoreInstanceState(savedInstanceState)
        restorePanelValues()
    }

//    override fun onStart() {
//        super.onStart()
//    }
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

}

