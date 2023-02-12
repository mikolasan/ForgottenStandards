package io.github.mikolasan.ratiogenerator

object MinFuelUnits : ImperialUnits(
    type = ImperialUnitType.FUEL,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.KM_PER_LITER), x(2.352146, ImperialUnitName.MILE_PER_GALLON)),
        eq(x(1.0, ImperialUnitName.LITER_ON_100KM), x(100.0, ImperialUnitName.KM_PER_LITER)),
    ),
    formulaList = listOf())