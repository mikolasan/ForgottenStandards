package io.github.mikolasan.ratiogenerator

object MinPressureUnits : ImperialUnitCategory(
    type = ImperialUnitType.PRESSURE,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.KILOPASCAL), x(1000.0, ImperialUnitName.PASCAL)),
        eq(x(1.0, ImperialUnitName.ATMOSPHERE), x(101325.0, ImperialUnitName.PASCAL)),
        eq(x(1.0, ImperialUnitName.BAR), x(100.0, ImperialUnitName.KILOPASCAL)),
        eq(x(1.0, ImperialUnitName.TORR), x(25.4, ImperialUnitName.INCH_OF_MERCURY)),
        eq(x(1.0, ImperialUnitName.BAR), x(14.503775, ImperialUnitName.POUND_PER_SQUARE_INCH)),
        eq(x(1.0, ImperialUnitName.ATMOSPHERE), x(760.0, ImperialUnitName.TORR)),
    ),
    formulaList = listOf())