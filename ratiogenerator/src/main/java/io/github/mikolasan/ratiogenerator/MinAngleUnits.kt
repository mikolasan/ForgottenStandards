package io.github.mikolasan.ratiogenerator

object MinAngleUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.RADIAN, mutableMapOf(
                    ImperialUnitName.TURN to 2.0 * 3.14
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.DEGREE, mutableMapOf(
                    ImperialUnitName.TURN to 360.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.MINUTE_OF_ARC, mutableMapOf(
                    ImperialUnitName.TURN to 21600.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.SECOND_OF_ARC, mutableMapOf(
                    ImperialUnitName.TURN to 1296000.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.GRAD, mutableMapOf(
                    ImperialUnitName.TURN to 400.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.CIRCLE, mutableMapOf(
                    ImperialUnitName.TURN to 1.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.SEXTANT, mutableMapOf(
                    ImperialUnitName.TURN to 6.0
            )),
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.TURN, mutableMapOf())
    )

    override val nameMap = makeUnitByNameMap(units)
}