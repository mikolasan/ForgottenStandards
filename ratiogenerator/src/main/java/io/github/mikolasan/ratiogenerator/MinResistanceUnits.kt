package io.github.mikolasan.ratiogenerator

object MinResistanceUnits : ImperialUnitCategory(
    type = ImperialUnitType.RESISTANCE,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.KILOOHM), x(1000.0, ImperialUnitName.OHM)),
        eq(x(1.0, ImperialUnitName.PLANCK_IMPEDANCE), x(29.9792458, ImperialUnitName.OHM)),
        eq(x(1.0, ImperialUnitName.QUANTIZED_HALL_RESISTANCE), x(25812.807557, ImperialUnitName.OHM)),
    ),
    formulaList = listOf())