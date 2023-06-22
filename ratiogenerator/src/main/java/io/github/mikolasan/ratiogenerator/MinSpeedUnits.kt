package io.github.mikolasan.ratiogenerator

object MinSpeedUnits : ImperialUnitCategory(
    type = ImperialUnitType.SPEED,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.MACH), x(343.2, ImperialUnitName.METER_PER_SECOND)),
        eq(x(1.0, ImperialUnitName.KNOT), x(0.514444, ImperialUnitName.METER_PER_SECOND)),
        eq(x(1.0, ImperialUnitName.MILE_PER_HOUR), x(0.44704, ImperialUnitName.METER_PER_SECOND)),
    ),
    formulaList = listOf())