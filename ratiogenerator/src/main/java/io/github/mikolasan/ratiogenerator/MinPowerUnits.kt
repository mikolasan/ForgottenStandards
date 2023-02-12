package io.github.mikolasan.ratiogenerator

object MinPowerUnits : ImperialUnits(
    type = ImperialUnitType.POWER,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.KILOWATT), x(1000.0, ImperialUnitName.WATT)),
        eq(x(1.0, ImperialUnitName.HORSEPOWER), x(745.7, ImperialUnitName.WATT)),
    ),
    formulaList = listOf())