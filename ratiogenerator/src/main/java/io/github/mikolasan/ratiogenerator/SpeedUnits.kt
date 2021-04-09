package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object SpeedUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.METER_PER_SECOND, mapOf(
                    ImperialUnitName.METER_PER_SECOND to 1.0,
                    ImperialUnitName.MILE_PER_HOUR to 0.44704,
                    ImperialUnitName.KNOT to 0.514444,
                    ImperialUnitName.MACH to 343.2)),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.MILE_PER_HOUR, mapOf(
                    ImperialUnitName.METER_PER_SECOND to 2.2369362920544025,
                    ImperialUnitName.MILE_PER_HOUR to 1.0,
                    ImperialUnitName.KNOT to 1.150778453829635,
                    ImperialUnitName.MACH to 767.7165354330708)),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.KNOT, mapOf(
                    ImperialUnitName.METER_PER_SECOND to 1.9438461717893492,
                    ImperialUnitName.MILE_PER_HOUR to 0.8689769926367106,
                    ImperialUnitName.KNOT to 1.0,
                    ImperialUnitName.MACH to 667.1280061581045)),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.MACH, mapOf(
                    ImperialUnitName.METER_PER_SECOND to 0.002913752913752914,
                    ImperialUnitName.MILE_PER_HOUR to 0.0013025641025641026,
                    ImperialUnitName.KNOT to 0.0014989627039627043,
                    ImperialUnitName.MACH to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
