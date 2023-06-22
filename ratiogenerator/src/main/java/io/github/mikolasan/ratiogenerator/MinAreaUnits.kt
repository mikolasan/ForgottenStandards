package io.github.mikolasan.ratiogenerator

object MinAreaUnits : ImperialUnitCategory(
    type = ImperialUnitType.AREA,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.DESYATINA), x(1.09254, ImperialUnitName.HECTARE)),
        eq(x(1.0, ImperialUnitName.SQUARE_METER), x(10000.0, ImperialUnitName.SQUARE_CENTIMETER)),
        eq(x(1.0, ImperialUnitName.SQUARE_INCH), x(6.4516, ImperialUnitName.SQUARE_CENTIMETER)),
        eq(x(1.0, ImperialUnitName.SQUARE_MILE), x(2.589988110336, ImperialUnitName.SQUARE_KILOMETER)),
        eq(x(1.0, ImperialUnitName.ARE), x(100.0, ImperialUnitName.SQUARE_METER)),
        eq(x(1.0, ImperialUnitName.HECTARE), x(10000.0, ImperialUnitName.SQUARE_METER)),
        eq(x(1.0, ImperialUnitName.SQUARE_KILOMETER), x(1000000.0, ImperialUnitName.SQUARE_METER)),
        eq(x(1.0, ImperialUnitName.SQUARE_FOOT), x(0.09290304, ImperialUnitName.SQUARE_METER)),
        eq(x(1.0, ImperialUnitName.DESYATINA), x(10925.4, ImperialUnitName.SQUARE_METER)),
        eq(x(1.0, ImperialUnitName.SQUARE_FOOT), x(144.0, ImperialUnitName.SQUARE_INCH)),
        eq(x(1.0, ImperialUnitName.ACRE), x(43560.0, ImperialUnitName.SQUARE_FOOT)),
        eq(x(1.0, ImperialUnitName.SQUARE_MILE), x(27878400.0, ImperialUnitName.SQUARE_FOOT)),
    ),
    formulaList = listOf())