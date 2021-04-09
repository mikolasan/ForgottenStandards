package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object FuelUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.KM_L, mapOf(
                    ImperialUnitName.KM_L to 1.0,
                    ImperialUnitName.MI_GAL to 0.4251436773057455,
                    ImperialUnitName.L_100KM to 100.0)),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.MI_GAL, mapOf(
                    ImperialUnitName.KM_L to 2.352146,
                    ImperialUnitName.MI_GAL to 1.0,
                    ImperialUnitName.L_100KM to 235.2146)),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.L_100KM, mapOf(
                    ImperialUnitName.KM_L to 0.01,
                    ImperialUnitName.MI_GAL to 0.004251436773057455,
                    ImperialUnitName.L_100KM to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
