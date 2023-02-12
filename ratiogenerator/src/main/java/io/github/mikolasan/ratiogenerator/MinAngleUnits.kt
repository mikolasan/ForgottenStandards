package io.github.mikolasan.ratiogenerator

object MinAngleUnits : ImperialUnits(
    type = ImperialUnitType.ANGLE,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.TURN), x(2.0 * 3.14, ImperialUnitName.RADIAN)),
        eq(x(1.0, ImperialUnitName.TURN), x(360.0, ImperialUnitName.DEGREE)),
        eq(x(1.0, ImperialUnitName.TURN), x(21600.0, ImperialUnitName.MINUTE_OF_ARC)),
        eq(x(1.0, ImperialUnitName.TURN), x(1296000.0, ImperialUnitName.SECOND_OF_ARC)),
        eq(x(1.0, ImperialUnitName.TURN), x(400.0, ImperialUnitName.GRAD)),
        eq(x(1.0, ImperialUnitName.TURN), x(1.0, ImperialUnitName.CIRCLE)),
        eq(x(1.0, ImperialUnitName.TURN), x(6.0, ImperialUnitName.SEXTANT)),
    ),
    formulaList = listOf())