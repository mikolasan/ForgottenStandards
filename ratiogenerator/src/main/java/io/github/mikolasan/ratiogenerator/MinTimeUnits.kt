package io.github.mikolasan.ratiogenerator

object MinTimeUnits : ImperialUnitCategory(
    type = ImperialUnitType.TIME,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.DAY), x(24.0, ImperialUnitName.HOUR)),
        eq(x(1.0, ImperialUnitName.WEEK), x(7.0, ImperialUnitName.DAY)),
        eq(x(1.0, ImperialUnitName.YEAR), x(365.0, ImperialUnitName.DAY)),
        eq(x(1.0, ImperialUnitName.FORTNIGHT), x(2.0, ImperialUnitName.WEEK)),
        eq(x(1.0, ImperialUnitName.YEAR), x(12.0, ImperialUnitName.MONTH)),
    ),
    formulaList = listOf())