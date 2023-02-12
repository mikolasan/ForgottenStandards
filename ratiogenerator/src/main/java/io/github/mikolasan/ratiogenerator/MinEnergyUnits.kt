package io.github.mikolasan.ratiogenerator

object MinEnergyUnits : ImperialUnits(
    type = ImperialUnitType.ENERGY,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.KILOJOULE), x(1000.0, ImperialUnitName.JOULE)),
        eq(x(1.0, ImperialUnitName.CALORIE), x(4.184, ImperialUnitName.JOULE)),
        eq(x(1.0, ImperialUnitName.KILOWATT_HOUR), x(3600.0, ImperialUnitName.KILOJOULE)),
        eq(x(1.0, ImperialUnitName.KILOCALORIE), x(1000.0, ImperialUnitName.CALORIE)),
    ),
    formulaList = listOf())