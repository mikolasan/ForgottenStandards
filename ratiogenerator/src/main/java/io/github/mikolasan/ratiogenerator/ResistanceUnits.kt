package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object ResistanceUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.RESISTANCE, ImperialUnitName.OHM, mapOf(
                    ImperialUnitName.OHM to 1.0,
                    ImperialUnitName.KILOOHM to 1000.0)),
            ImperialUnit(ImperialUnitType.RESISTANCE, ImperialUnitName.KILOOHM, mapOf(
                    ImperialUnitName.OHM to 0.001,
                    ImperialUnitName.KILOOHM to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
