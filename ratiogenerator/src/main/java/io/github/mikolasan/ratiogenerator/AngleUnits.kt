package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object AngleUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.RADIAN, mapOf(
                    ImperialUnitName.RADIAN to 1.0,
                    ImperialUnitName.DEGREE to 0.017444444444444446,
                    ImperialUnitName.MINUTE_OF_ARC to 2.9074074074074077E-4,
                    ImperialUnitName.SECOND_OF_ARC to 4.845679012345679E-6,
                    ImperialUnitName.GRAD to 0.015700000000000002,
                    ImperialUnitName.CIRCLE to 6.28,
                    ImperialUnitName.SEXTANT to 1.0466666666666666,
                    ImperialUnitName.TURN to 6.28)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.DEGREE, mapOf(
                    ImperialUnitName.RADIAN to 57.324840764331206,
                    ImperialUnitName.DEGREE to 1.0,
                    ImperialUnitName.MINUTE_OF_ARC to 0.016666666666666666,
                    ImperialUnitName.SECOND_OF_ARC to 2.777777777777778E-4,
                    ImperialUnitName.GRAD to 0.9,
                    ImperialUnitName.CIRCLE to 360.0,
                    ImperialUnitName.SEXTANT to 60.0,
                    ImperialUnitName.TURN to 360.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.MINUTE_OF_ARC, mapOf(
                    ImperialUnitName.RADIAN to 3439.4904458598726,
                    ImperialUnitName.DEGREE to 60.0,
                    ImperialUnitName.MINUTE_OF_ARC to 1.0,
                    ImperialUnitName.SECOND_OF_ARC to 0.016666666666666666,
                    ImperialUnitName.GRAD to 54.0,
                    ImperialUnitName.CIRCLE to 21600.0,
                    ImperialUnitName.SEXTANT to 3600.0,
                    ImperialUnitName.TURN to 21600.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.SECOND_OF_ARC, mapOf(
                    ImperialUnitName.RADIAN to 206369.42675159234,
                    ImperialUnitName.DEGREE to 3600.0,
                    ImperialUnitName.MINUTE_OF_ARC to 60.0,
                    ImperialUnitName.SECOND_OF_ARC to 1.0,
                    ImperialUnitName.GRAD to 3240.0,
                    ImperialUnitName.CIRCLE to 1296000.0,
                    ImperialUnitName.SEXTANT to 216000.0,
                    ImperialUnitName.TURN to 1296000.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.GRAD, mapOf(
                    ImperialUnitName.RADIAN to 63.69426751592356,
                    ImperialUnitName.DEGREE to 1.1111111111111112,
                    ImperialUnitName.MINUTE_OF_ARC to 0.018518518518518517,
                    ImperialUnitName.SECOND_OF_ARC to 3.0864197530864197E-4,
                    ImperialUnitName.GRAD to 1.0,
                    ImperialUnitName.CIRCLE to 400.0,
                    ImperialUnitName.SEXTANT to 66.66666666666667,
                    ImperialUnitName.TURN to 400.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.CIRCLE, mapOf(
                    ImperialUnitName.RADIAN to 0.1592356687898089,
                    ImperialUnitName.DEGREE to 0.002777777777777778,
                    ImperialUnitName.MINUTE_OF_ARC to 4.6296296296296294E-5,
                    ImperialUnitName.SECOND_OF_ARC to 7.716049382716049E-7,
                    ImperialUnitName.GRAD to 0.0025,
                    ImperialUnitName.CIRCLE to 1.0,
                    ImperialUnitName.SEXTANT to 0.16666666666666666,
                    ImperialUnitName.TURN to 1.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.SEXTANT, mapOf(
                    ImperialUnitName.RADIAN to 0.9554140127388535,
                    ImperialUnitName.DEGREE to 0.016666666666666666,
                    ImperialUnitName.MINUTE_OF_ARC to 2.777777777777778E-4,
                    ImperialUnitName.SECOND_OF_ARC to 4.6296296296296296E-6,
                    ImperialUnitName.GRAD to 0.015,
                    ImperialUnitName.CIRCLE to 6.0,
                    ImperialUnitName.SEXTANT to 1.0,
                    ImperialUnitName.TURN to 6.0)),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.TURN, mapOf(
                    ImperialUnitName.RADIAN to 0.1592356687898089,
                    ImperialUnitName.DEGREE to 0.002777777777777778,
                    ImperialUnitName.MINUTE_OF_ARC to 4.6296296296296294E-5,
                    ImperialUnitName.SECOND_OF_ARC to 7.716049382716049E-7,
                    ImperialUnitName.GRAD to 0.0025,
                    ImperialUnitName.CIRCLE to 1.0,
                    ImperialUnitName.SEXTANT to 0.16666666666666666,
                    ImperialUnitName.TURN to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
