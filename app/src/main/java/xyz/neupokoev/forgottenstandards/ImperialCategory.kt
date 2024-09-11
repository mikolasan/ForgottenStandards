package xyz.neupokoev.forgottenstandards

import io.github.mikolasan.ratiogenerator.ImperialUnitCategory
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.MinAngleUnits
import io.github.mikolasan.ratiogenerator.MinAreaUnits
import io.github.mikolasan.ratiogenerator.MinCurrencyUnits
import io.github.mikolasan.ratiogenerator.MinEnergyUnits
import io.github.mikolasan.ratiogenerator.MinForceUnits
import io.github.mikolasan.ratiogenerator.MinFuelUnits
import io.github.mikolasan.ratiogenerator.MinLengthUnits
import io.github.mikolasan.ratiogenerator.MinNutBoltUnits
import io.github.mikolasan.ratiogenerator.MinPowerUnits
import io.github.mikolasan.ratiogenerator.MinPressureUnits
import io.github.mikolasan.ratiogenerator.MinResistanceUnits
import io.github.mikolasan.ratiogenerator.MinSpeedUnits
import io.github.mikolasan.ratiogenerator.MinStorageUnits
import io.github.mikolasan.ratiogenerator.MinTemperatureUnits
import io.github.mikolasan.ratiogenerator.MinTimeUnits
import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import io.github.mikolasan.ratiogenerator.MinWeightUnits

object ImperialCategory {
    val names: Array<ImperialUnitCategoryName> = arrayOf(
        ImperialUnitCategoryName("Length"),
        ImperialUnitCategoryName("Area"),
        ImperialUnitCategoryName("Volume"),
        ImperialUnitCategoryName("Temperature"),
        ImperialUnitCategoryName("Weight"),
        ImperialUnitCategoryName("Speed"),
        ImperialUnitCategoryName("Time"),
        // Physics
        ImperialUnitCategoryName("Pressure"),
        ImperialUnitCategoryName("Power"),
        ImperialUnitCategoryName("Energy"), // aka Work
        ImperialUnitCategoryName("Force"),
        ImperialUnitCategoryName("Resistance"), // ?

        ImperialUnitCategoryName("Currency"),

        ImperialUnitCategoryName("Storage"),
        ImperialUnitCategoryName("Fuel"),
        ImperialUnitCategoryName("Angle"),
        // New
        //ImperialUnitCategoryName("Nut & Bolt")
    )

    val typeMap: Map<ImperialUnitType, ImperialUnitCategory> = mapOf(
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
        //ImperialUnitType.NUT_AND_BOLT to MinNutBoltUnits,
    )
}