package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object EnergyUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.JOULE, mapOf(
                    ImperialUnitName.JOULE to 1.0,
                    ImperialUnitName.KILOJOULE to 1000.0,
                    ImperialUnitName.KILOWATT_HOUR to 3600000.0,
                    ImperialUnitName.CALORIE to 4.184,
                    ImperialUnitName.KILOCALORIE to 4184.0)),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOJOULE, mapOf(
                    ImperialUnitName.JOULE to 0.001,
                    ImperialUnitName.KILOJOULE to 1.0,
                    ImperialUnitName.KILOWATT_HOUR to 3600.0,
                    ImperialUnitName.CALORIE to 0.004184,
                    ImperialUnitName.KILOCALORIE to 4.184)),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOWATT_HOUR, mapOf(
                    ImperialUnitName.JOULE to 2.7777777777777776E-7,
                    ImperialUnitName.KILOJOULE to 2.777777777777778E-4,
                    ImperialUnitName.KILOWATT_HOUR to 1.0,
                    ImperialUnitName.CALORIE to 1.1622222222222223E-6,
                    ImperialUnitName.KILOCALORIE to 0.0011622222222222223)),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.CALORIE, mapOf(
                    ImperialUnitName.JOULE to 0.2390057361376673,
                    ImperialUnitName.KILOJOULE to 239.0057361376673,
                    ImperialUnitName.KILOWATT_HOUR to 860420.6500956023,
                    ImperialUnitName.CALORIE to 1.0,
                    ImperialUnitName.KILOCALORIE to 1000.0)),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOCALORIE, mapOf(
                    ImperialUnitName.JOULE to 2.390057361376673E-4,
                    ImperialUnitName.KILOJOULE to 0.2390057361376673,
                    ImperialUnitName.KILOWATT_HOUR to 860.4206500956022,
                    ImperialUnitName.CALORIE to 0.001,
                    ImperialUnitName.KILOCALORIE to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
