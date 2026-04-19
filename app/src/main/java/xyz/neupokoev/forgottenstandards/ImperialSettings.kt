package xyz.neupokoev.forgottenstandards

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import xyz.neupokoev.forgottenstandards.menu.ImperialCategory
import xyz.neupokoev.forgottenstandards.menu.ImperialUnitCategoryName

class ImperialSettings(application: Application) : AndroidViewModel(application) {
    private val preferencesFile = "ForgStPref.12"

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
                ImperialCategory.typeMap.getValue(type).nameMap.getValue(imperialUnitName)
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
            ImperialUnitType.entries.associateWith { unitType -> loadOrderedUnits(unitType) }
        val category: ImperialUnitCategoryName = if (preferences.contains("category")) {
            val categoryName = preferences.getString("category", null) ?: "Length"
            ImperialCategory.names.find { n -> n.name == categoryName } ?: ImperialCategory.names.first()
        } else {
            ImperialCategory.names.first()
        }
        val type: ImperialUnitType = categoryNameToType(category)
        val currentUnits = units.getValue(type)

        val topPanelUnit: ImperialUnit = if (currentUnits.isNotEmpty()) {
            restoreUnit(type, "topPanelUnit", currentUnits[0])
        } else {
            // Fallback for empty categories
            ImperialUnit(ImperialCategory.typeMap.getValue(type), type, ImperialUnitName.NO_UNIT)
        }

        return WorkingUnits().apply {
            allUnits = units
            orderedUnits = currentUnits
            mainUnit = topPanelUnit
            selectedCategory = category
        }
    }

    fun restoreTopString(): String {
        return preferences.getString("topPanelValue", "") ?: ""
    }

    fun restoreBottomString(): String {
        return preferences.getString("bottomPanelValue", "") ?: ""
    }

    private fun loadOrderedUnits(type: ImperialUnitType): Array<ImperialUnit> {
        val category = ImperialCategory.typeMap.getValue(type)
        val units = category.units.toTypedArray().copyOf()
        if (units.isEmpty()) return units
        
        category.units.forEachIndexed { i, u ->
            if (u.unitName == ImperialUnitName.NO_UNIT) {
                return@forEachIndexed
            }
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            val p = preferences.getInt(settingName, i)
            if (p < 0) {
                return@forEachIndexed
            }
            if (!preferences.contains(settingName)) {
                preferencesEditor.putInt(settingName, i)
            }
            if (p < units.size) {
                units[p] = u
            }
        }
        if (units.distinct().size != units.size) {
            category.units.toTypedArray().copyInto(units)
        }
        preferencesEditor.apply()
        return units
    }

    fun saveNewOrder(orderedUnits: Array<ImperialUnit>) {
        orderedUnits.forEachIndexed { i, u ->
            val unitName = u.unitName.name
            val settingName = "unit${unitName}Position"
            preferencesEditor.putInt(settingName, i)
        }
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

    fun incrementConversionCount(unit1: ImperialUnit, unit2: ImperialUnit) {
        val name1 = unit1.unitName.name
        val name2 = unit2.unitName.name
        val pairKey = if (name1 < name2) "pair_${name1}_${name2}" else "pair_${name2}_${name1}"
        val count = preferences.getInt(pairKey, 0)
        preferencesEditor.putInt(pairKey, count + 1)
        preferencesEditor.apply()
    }

    fun getFrequentConversions(): List<Pair<ImperialUnitName, ImperialUnitName>> {
        val all = preferences.all
        return all.filterKeys { it.startsWith("pair_") }
            .map { (key, value) -> 
                try {
                    key to (value as Int)
                } catch (e: Exception) {
                    key to 0
                }
            }
            .sortedByDescending { it.second }
            .take(5)
            .mapNotNull { (key, _) ->
                try {
                    val parts = key.removePrefix("pair_").split("_")
                    ImperialUnitName.valueOf(parts[0]) to ImperialUnitName.valueOf(parts[1])
                } catch (e: Exception) {
                    null
                }
            }
    }

    fun isFirstRun(): Boolean {
        return preferences.getBoolean("isFirstRun", true)
    }

    fun setFirstRunDone() {
        preferencesEditor.putBoolean("isFirstRun", false)
        preferencesEditor.apply()
    }
}