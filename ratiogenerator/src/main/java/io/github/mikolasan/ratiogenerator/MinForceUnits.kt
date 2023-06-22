package io.github.mikolasan.ratiogenerator

object MinForceUnits : ImperialUnitCategory(
    type = ImperialUnitType.FORCE,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.NEWTON), x(100000.0, ImperialUnitName.DYNE)),
        eq(x(1.0, ImperialUnitName.POUNDAL), x(13825.5, ImperialUnitName.DYNE)),
        eq(x(1.0, ImperialUnitName.KILOGRAM_FORCE), x(980665.0, ImperialUnitName.DYNE)),
        eq(x(1.0, ImperialUnitName.KILONEWTON), x(1000.0, ImperialUnitName.NEWTON)),
    ),
    formulaList = listOf())