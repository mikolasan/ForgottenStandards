package io.github.mikolasan.imperialrussia

import android.content.Context
import android.content.SharedPreferences
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.LengthUnits

class ImperialSettings(private val context: Context) {
    private val preferencesFile = "ImperialRussiaPref.4"

    private val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy preferences.edit()
    }

    private val preferences: SharedPreferences by lazy {
        context.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
    }

    private fun restoreUnit(storedName: String): ImperialUnit? {
        if (storedName != "") {
            val unitName = ImperialUnitName.valueOf(storedName)
            return LengthUnits.imperialUnits[unitName]
        }
        return null
    }

    fun restoreWorkingUnits(): WorkingUnits {
        val orderedUnits = LengthUnits.lengthUnits.toMutableList()
        LengthUnits.lengthUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            var p = preferences.getInt(settingName, i)
            if (p < 0) {
                System.err.println("Shit: pos ${i}, unit ${unitName} got ${p}")
                p = i
            }
            if (!preferences.contains(settingName)) {
                preferencesEditor.putInt(settingName, i)
            }
            orderedUnits[p] = u
        }
        preferencesEditor.apply()

        val topPanelUnit = restoreTopUnit(orderedUnits[0])
        val bottomPanelUnit = restoreBottomUnit(orderedUnits[1])
        if (orderedUnits[1] != bottomPanelUnit) {
            orderedUnits.moveToFront(bottomPanelUnit)
        }
        if (orderedUnits[0] != topPanelUnit) {
            orderedUnits.moveToFront(topPanelUnit)
        }

        return WorkingUnits(orderedUnits, topPanelUnit, bottomPanelUnit)
    }

    fun restoreTopUnit(defaultUnit: ImperialUnit): ImperialUnit {
        return restoreUnit(preferences.getString("topPanelUnit", "") ?: "") ?: defaultUnit
    }

    fun restoreBottomUnit(defaultUnit: ImperialUnit): ImperialUnit {
        return restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "") ?: defaultUnit
    }

    fun restoreTopString(): String {
        return preferences.getString("topPanelValue", "") ?: ""
    }

    fun restoreBottomString(): String {
        return preferences.getString("bottomPanelValue", "") ?: ""
    }

    fun saveNewOrder(orderedUnits: MutableList<ImperialUnit>) {
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