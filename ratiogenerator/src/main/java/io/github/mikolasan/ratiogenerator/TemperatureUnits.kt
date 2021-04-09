package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object TemperatureUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.CELSIUS, mapOf(
                    ImperialUnitName.CELSIUS to 1.0,
                    ImperialUnitName.FAHRENHEIT to 1.0,
                    ImperialUnitName.KELVIN to 1.0,
                    ImperialUnitName.RANKINE to 1.0,
                    ImperialUnitName.REAUMUR to 0.8)),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.FAHRENHEIT, mapOf(
                    ImperialUnitName.CELSIUS to 1.0,
                    ImperialUnitName.FAHRENHEIT to 1.0,
                    ImperialUnitName.KELVIN to 1.0,
                    ImperialUnitName.RANKINE to 1.0,
                    ImperialUnitName.REAUMUR to 0.8)),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.KELVIN, mapOf(
                    ImperialUnitName.CELSIUS to 1.0,
                    ImperialUnitName.FAHRENHEIT to 1.0,
                    ImperialUnitName.KELVIN to 1.0,
                    ImperialUnitName.RANKINE to 1.0,
                    ImperialUnitName.REAUMUR to 0.8)),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.RANKINE, mapOf(
                    ImperialUnitName.CELSIUS to 1.0,
                    ImperialUnitName.FAHRENHEIT to 1.0,
                    ImperialUnitName.KELVIN to 1.0,
                    ImperialUnitName.RANKINE to 1.0,
                    ImperialUnitName.REAUMUR to 0.8)),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.REAUMUR, mapOf(
                    ImperialUnitName.CELSIUS to 1.25,
                    ImperialUnitName.FAHRENHEIT to 1.25,
                    ImperialUnitName.KELVIN to 1.25,
                    ImperialUnitName.RANKINE to 1.25,
                    ImperialUnitName.REAUMUR to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
