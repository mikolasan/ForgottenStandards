package io.github.mikolasan.imperialrussia

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitCategory
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.MinAngleUnits
import io.github.mikolasan.ratiogenerator.MinAreaUnits
import io.github.mikolasan.ratiogenerator.MinCurrencyUnits
import io.github.mikolasan.ratiogenerator.MinEnergyUnits
import io.github.mikolasan.ratiogenerator.MinForceUnits
import io.github.mikolasan.ratiogenerator.MinFuelUnits
import io.github.mikolasan.ratiogenerator.MinLengthUnits
import io.github.mikolasan.ratiogenerator.MinPowerUnits
import io.github.mikolasan.ratiogenerator.MinPressureUnits
import io.github.mikolasan.ratiogenerator.MinResistanceUnits
import io.github.mikolasan.ratiogenerator.MinSpeedUnits
import io.github.mikolasan.ratiogenerator.MinStorageUnits
import io.github.mikolasan.ratiogenerator.MinTemperatureUnits
import io.github.mikolasan.ratiogenerator.MinTimeUnits
import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import io.github.mikolasan.ratiogenerator.MinWeightUnits
import io.github.mikolasan.ratiogenerator.MinNutBoltUnits

class ImperialSettings(application: Application) : AndroidViewModel(application) {
    private val categoryMap: Map<ImperialUnitType, ImperialUnitCategory> = mapOf(
            ImperialUnitType.ANGLE to MinAngleUnits,
            ImperialUnitType.AREA to MinAreaUnits,
            ImperialUnitType.CURRENCY to MinCurrencyUnits,
            ImperialUnitType.ENERGY to MinEnergyUnits,
            ImperialUnitType.FORCE to MinForceUnits,
            ImperialUnitType.FUEL to MinFuelUnits,
            ImperialUnitType.LENGTH to MinLengthUnits,
            ImperialUnitType.POWER to MinPowerUnits,
            ImperialUnitType.PRESSURE to MinPressureUnits,
            ImperialUnitType.RESISTANCE to MinResistanceUnits,
            ImperialUnitType.SPEED to MinSpeedUnits,
            ImperialUnitType.STORAGE to MinStorageUnits,
            ImperialUnitType.TEMPERATURE to MinTemperatureUnits,
            ImperialUnitType.TIME to MinTimeUnits,
            ImperialUnitType.VOLUME to MinVolumeUnits,
            ImperialUnitType.WEIGHT to MinWeightUnits,
            ImperialUnitType.NUT_AND_BOLT to MinNutBoltUnits,
    )

    private val preferencesFile = "ImperialRussiaPref.9"

    private val preferencesEditor: SharedPreferences.Editor by lazy {
        return@lazy preferences.edit()
    }
    private val preferences: SharedPreferences by lazy {
        application.applicationContext.getSharedPreferences(preferencesFile, Context.MODE_PRIVATE)
    }

    private fun restoreUnit(type: ImperialUnitType, settingName: String, defaultUnit: ImperialUnit): ImperialUnit {
        return if (preferences.contains(settingName)) {
            try {
                val unitName = preferences.getString(settingName, null) ?: defaultUnit.unitName.name
                val imperialUnitName = ImperialUnitName.valueOf(unitName)
                categoryMap.getValue(type).nameMap.getValue(imperialUnitName)
            } catch (e: Exception) {
                System.err.println(e.message)
                defaultUnit
            }
        } else {
            defaultUnit
        }
    }

    fun restoreWorkingUnits(): WorkingUnits {
        val units: Map<ImperialUnitType, Array<ImperialUnit>> =
            ImperialUnitType.values().associateWith { unitType -> loadOrderedUnits(unitType) }
        val type: ImperialUnitType = if (preferences.contains("category")) {
            ImperialUnitType.valueOf(preferences.getString("category", null) ?: "LENGTH")
        } else {
            ImperialUnitType.LENGTH
        }
        val currentUnits = units.getValue(type)

        val topPanelUnit: ImperialUnit = restoreUnit(type, "topPanelUnit", currentUnits[0])
        val bottomPanelUnit: ImperialUnit = restoreUnit(type, "bottomPanelUnit", currentUnits[1])

        return WorkingUnits().apply {
            allUnits = units
            orderedUnits = currentUnits
            topUnit = topPanelUnit
            bottomUnit = bottomPanelUnit
            selectedCategory = topPanelUnit.unitType
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
        val units = categoryMap.getValue(type).units.toTypedArray().copyOf()
        categoryMap.getValue(type).units.forEachIndexed { i, u ->
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
            categoryMap.getValue(type).units.toTypedArray().copyInto(units)
        }
        preferencesEditor.apply()
        return units
    }

    fun saveNewOrder(orderedUnits: Array<ImperialUnit>) {
        println("== saveNewOrder ==")
        orderedUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            println("$settingName - $i")
            preferencesEditor.putInt(settingName, i)
        }
        println("== END ==")
        preferencesEditor.apply()
    }

    fun saveCategory(serializedString: String) {
        preferencesEditor.putString("category", serializedString)
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