package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object PowerUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.WATT, mapOf(
                    ImperialUnitName.WATT to 1.0,
                    ImperialUnitName.KILOWATT to 1000.0,
                    ImperialUnitName.HORSEPOWER to 745.7)),
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.KILOWATT, mapOf(
                    ImperialUnitName.WATT to 0.001,
                    ImperialUnitName.KILOWATT to 1.0,
                    ImperialUnitName.HORSEPOWER to 0.7457)),
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.HORSEPOWER, mapOf(
                    ImperialUnitName.WATT to 0.001341021858656296,
                    ImperialUnitName.KILOWATT to 1.3410218586562959,
                    ImperialUnitName.HORSEPOWER to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
