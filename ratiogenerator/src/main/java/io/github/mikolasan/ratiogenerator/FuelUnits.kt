package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object FuelUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.KM_PER_LITER, mapOf(
                    ImperialUnitName.KM_PER_LITER to 1.0,
                    ImperialUnitName.MILE_PER_GALLON to 0.4251436773057455,
                    ImperialUnitName.LITER_ON_100KM to 100.0)),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.MILE_PER_GALLON, mapOf(
                    ImperialUnitName.KM_PER_LITER to 2.352146,
                    ImperialUnitName.MILE_PER_GALLON to 1.0,
                    ImperialUnitName.LITER_ON_100KM to 235.2146)),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.LITER_ON_100KM, mapOf(
                    ImperialUnitName.KM_PER_LITER to 0.01,
                    ImperialUnitName.MILE_PER_GALLON to 0.004251436773057455,
                    ImperialUnitName.LITER_ON_100KM to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
