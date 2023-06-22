package io.github.mikolasan.ratiogenerator

object MinCurrencyUnits : ImperialUnitCategory(
    type = ImperialUnitType.CURRENCY,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.MOSKOVKA), x(2.0, ImperialUnitName.DENGA)),
        eq(x(1.0, ImperialUnitName.DENGA), x(100.0, ImperialUnitName.KOPEIKA)),
        eq(x(1.0, ImperialUnitName.KOPEIKA), x(3.0, ImperialUnitName.ALTIN)),
    ),
    formulaList = listOf())