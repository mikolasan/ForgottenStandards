package xyz.neupokoev.forgottenstandards.menu

import io.github.mikolasan.ratiogenerator.ImperialUnitCategory
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.MinAngleUnits
import io.github.mikolasan.ratiogenerator.MinAreaUnits
import io.github.mikolasan.ratiogenerator.MinCookingUnits
import io.github.mikolasan.ratiogenerator.MinCurrencyUnits
import io.github.mikolasan.ratiogenerator.MinEnergyUnits
import io.github.mikolasan.ratiogenerator.MinForceUnits
import io.github.mikolasan.ratiogenerator.MinFuelUnits
import io.github.mikolasan.ratiogenerator.MinLengthUnits
import io.github.mikolasan.ratiogenerator.MinNutBoltUnits
import io.github.mikolasan.ratiogenerator.MinPowerUnits
import io.github.mikolasan.ratiogenerator.MinPressureUnits
import io.github.mikolasan.ratiogenerator.MinResistanceUnits
import io.github.mikolasan.ratiogenerator.MinSlavicCalendarUnits
import io.github.mikolasan.ratiogenerator.MinSpeedUnits
import io.github.mikolasan.ratiogenerator.MinStorageUnits
import io.github.mikolasan.ratiogenerator.MinTemperatureUnits
import io.github.mikolasan.ratiogenerator.MinTimeUnits
import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import io.github.mikolasan.ratiogenerator.MinWeightUnits
import xyz.neupokoev.forgottenstandards.ImperialSettings
import xyz.neupokoev.forgottenstandards.R

object ImperialCategory {
    val items: List<CategoryMenuItem> = listOf(
        CategoryMenuItem.Header("Common"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Length", R.drawable.ic_length)),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Area")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Volume")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Temperature", R.drawable.ic_temperature)),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Weight", R.drawable.ic_weight)),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Speed")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Time")),
        CategoryMenuItem.Header("Cooking"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Cooking", R.drawable.ic_chef_hat)),
        CategoryMenuItem.Header("Physics"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Pressure")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Power")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Energy")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Force")),
        CategoryMenuItem.Header("Electronics"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Resistance")),
        CategoryMenuItem.Header("Misc"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Currency")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Storage")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Fuel")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Angle")),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Nut and Bolt size")),
        CategoryMenuItem.Header("Calendars"),
        CategoryMenuItem.Item(ImperialUnitCategoryName("Slavic Calendar"))
    )

    fun getItemsWithFrequent(settings: ImperialSettings): List<CategoryMenuItem> {
        val frequent = settings.getFrequentConversions()
        if (frequent.isEmpty()) return items
        
        val list = mutableListOf<CategoryMenuItem>()
        list.add(CategoryMenuItem.Header("Frequent"))
        frequent.forEach { (u1, u2) ->
            list.add(CategoryMenuItem.ConversionPair(u1, u2))
        }
        list.addAll(items)
        return list
    }

    val names: Array<ImperialUnitCategoryName> = items
        .filterIsInstance<CategoryMenuItem.Item>()
        .map { it.category }
        .toTypedArray()

    val typeMap: Map<ImperialUnitType, ImperialUnitCategory> = mapOf(
        ImperialUnitType.ANGLE to MinAngleUnits,
        ImperialUnitType.AREA to MinAreaUnits,
        ImperialUnitType.COOKING to MinCookingUnits,
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
        ImperialUnitType.NUT_AND_BOLT_SIZE to MinNutBoltUnits,
        ImperialUnitType.SLAVIC_CALENDAR to MinSlavicCalendarUnits,
    )
}