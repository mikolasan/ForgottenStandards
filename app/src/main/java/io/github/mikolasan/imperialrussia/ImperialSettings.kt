package io.github.mikolasan.imperialrussia

import android.content.Context
import android.content.SharedPreferences
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.LengthUnits
import java.lang.ClassCastException

class ImperialSettings(private val context: Context) {
    private val preferencesFile = "ImperialRussiaPref.7"

    private val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy preferences.edit()
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
    }

    private fun restoreUnit(settingName: String, defaultUnit: ImperialUnit): ImperialUnit {
        return if (preferences.contains(settingName)) {
            try {
                val unitName = preferences.getString(settingName, defaultUnit.unitName.name)
                val imperialUnitName = ImperialUnitName.valueOf(unitName!!)
                LengthUnits.imperialUnits.getValue(imperialUnitName)
            } catch (e: ClassCastException) {
                System.err.println(e.message)
                defaultUnit
            }
        } else {
            defaultUnit
        }
    }

    fun restoreWorkingUnits(): WorkingUnits {
        val units = LengthUnits.lengthUnits
        LengthUnits.lengthUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            var p = preferences.getInt(settingName, i)
            if (p < 0) {
                System.err.println("Shit: pos ${i}, unit ${unitName} got ${p}")
                p = i
            }
            if (!preferences.contains(settingName)) {
                System.err.println("First time loading ${settingName}")
                preferencesEditor.putInt(settingName, i)
            }
            units[p] = u
        }
        preferencesEditor.apply()

        val topPanelUnit: ImperialUnit = restoreUnit("topPanelUnit", units[0])
        val bottomPanelUnit: ImperialUnit = restoreUnit("bottomPanelUnit", units[1])

        return WorkingUnits().apply {
            orderedUnits = units
            selectedUnit = topPanelUnit
            secondUnit = bottomPanelUnit
            topUnit = topPanelUnit
            bottomUnit = bottomPanelUnit
            listAdapter = ImperialListAdapter(this)
        }
    }

    fun restoreTopString(): String {
        return preferences.getString("topPanelValue", "") ?: ""
    }

    fun restoreBottomString(): String {
        return preferences.getString("bottomPanelValue", "") ?: ""
    }

    fun saveNewOrder(orderedUnits: Array<ImperialUnit>) {
        LengthUnits.lengthUnits.forEachIndexed { _, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            preferencesEditor.putInt(settingName, orderedUnits.indexOf(u))
        }
        preferencesEditor.apply()
    }

    fun saveTopString(serializedString: String) {
        preferencesEditor.putString("topPanelValue", serializedString)
        preferencesEditor.apply()
    }

    fun saveBottomString(serializedString: String) {
        preferencesEditor.putString("bottomPanelValue", serializedString)
        preferencesEditor.apply()
    }

    fun saveTopUnit(unit: ImperialUnit, serializedString: String) {
        preferencesEditor.putString("topPanelUnit", unit.unitName.name)
        preferencesEditor.putString("topPanelValue", serializedString)
        preferencesEditor.apply()
    }

    fun saveBottomUnit(unit: ImperialUnit, serializedString: String) {
        preferencesEditor.putString("bottomPanelUnit", unit.unitName.name)
        preferencesEditor.putString("bottomPanelValue", serializedString)
        preferencesEditor.apply()
    }
}