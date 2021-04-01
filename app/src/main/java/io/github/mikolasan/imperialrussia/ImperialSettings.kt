package io.github.mikolasan.imperialrussia

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import io.github.mikolasan.ratiogenerator.*
import java.lang.ClassCastException

class ImperialSettings(application: Application) : AndroidViewModel(application) {
    val allTypes: Array<ImperialUnitType> = arrayOf(
            ImperialUnitType.LENGTH,
            ImperialUnitType.VOLUME
    )
    private val unitObjects: Map<ImperialUnitType, ImperialUnits> = mapOf(
            ImperialUnitType.LENGTH to LengthUnits,
            ImperialUnitType.VOLUME to VolumeUnits
    )

    private val preferencesFile = "ImperialRussiaPref.7"

    private val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy preferences.edit()
    }
    private val preferences: SharedPreferences by lazy {
        application.applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
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
        val units: Map<ImperialUnitType, Array<ImperialUnit>> = allTypes.associate { unitType ->
            Pair(unitType, loadOrderedUnits(unitType))
        }
        val currentUnits = loadOrderedUnits()

        val topPanelUnit: ImperialUnit = restoreUnit("topPanelUnit", units[0])
        val bottomPanelUnit: ImperialUnit = restoreUnit("bottomPanelUnit", units[1])

        return WorkingUnits().apply {
            allUnits = units
            orderedUnits = currentUnits
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

    private fun loadOrderedUnits(type: ImperialUnitType): Array<ImperialUnit> {
        val units = LengthUnits.units.copyOf()
        LengthUnits.units.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            val p = preferences.getInt(settingName, i)
            if (p < 0) {
                throw ImperialInsistentException("Shit: pos ${i}, unit ${unitName} got ${p}")
            }
            if (!preferences.contains(settingName)) {
                System.err.println("First time loading ${settingName}")
                preferencesEditor.putInt(settingName, i)
            }
            units[p] = u
        }
        if (units.distinct().size != units.size) {
            LengthUnits.units.copyInto(units)
        }
        preferencesEditor.apply()
        return units
    }

    fun saveNewOrder(orderedUnits: Array<ImperialUnit>) {
        println("== saveNewOrder ==")
        orderedUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            println("${settingName} - ${i}")
            preferencesEditor.putInt(settingName, i)
        }
        println("== END ==")
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