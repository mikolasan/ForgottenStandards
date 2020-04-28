package io.github.mikolasan.imperialrussia

import android.content.Context
import android.content.SharedPreferences

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

    private fun restoreOrderedUnits(): MutableList<ImperialUnit> {
        val topPanelUnit = restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
        val bottomPanelUnit = restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")

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

    fun restoreTopUnit(): ImperialUnit? {
        return restoreUnit(preferences.getString("topPanelUnit", "") ?: "")
    }

    fun restoreBottomUnit(): ImperialUnit? {
        return restoreUnit(preferences.getString("bottomPanelUnit", "") ?: "")
    }

    fun restoreTopString(): String {
        return preferences.getString("topPanelValue", "") ?: ""
    }

    fun restoreBottomString(): String {
        return preferences.getString("bottomPanelValue", "") ?: ""
    }
}