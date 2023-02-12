package io.github.mikolasan.ratiogenerator

object MinVolumeUnits : ImperialUnits(
    type = ImperialUnitType.VOLUME,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.GARNETS), x(4.0, ImperialUnitName.VEDRO)),
        eq(x(1.0, ImperialUnitName.GARNETS), x(32.0, ImperialUnitName.OSMINA)),
        eq(x(1.0, ImperialUnitName.VEDRO), x(200.0, ImperialUnitName.SHKALIK)),
        eq(x(1.0, ImperialUnitName.VEDRO), x(10.0, ImperialUnitName.SHTOF)),
        eq(x(1.0, ImperialUnitName.CHARKA), x(2.0, ImperialUnitName.SHKALIK)),
        eq(x(1.0, ImperialUnitName.SHTOF), x(4.0, ImperialUnitName.SOROKOVKA)),
        eq(x(1.0, ImperialUnitName.QUART), x(1.13652, ImperialUnitName.LITER)),
        eq(x(1.0, ImperialUnitName.SHTOF), x(1.23, ImperialUnitName.LITER)),
        eq(x(1.0, ImperialUnitName.PINT), x(568.26125, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.SHKALIK), x(61.5, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.CHARKA), x(123.0, ImperialUnitName.MILLILITER)),
        eq(x(1.0, ImperialUnitName.PINT), x(8.0, ImperialUnitName.GALLON)),
        eq(x(1.0, ImperialUnitName.VEDRO), x(2.71, ImperialUnitName.GALLON)),
        eq(x(1.0, ImperialUnitName.SHKALIK), x(2.16, ImperialUnitName.FLUID_ONCE)),
        eq(x(1.0, ImperialUnitName.CHARKA), x(4.33, ImperialUnitName.FLUID_ONCE)),
        eq(x(1.0, ImperialUnitName.SHTOF), x(2.16, ImperialUnitName.PINT)),
        eq(x(1.0, ImperialUnitName.PINT), x(2.0, ImperialUnitName.QUART)),
        eq(x(1.0, ImperialUnitName.GARNETS), x(3.466, ImperialUnitName.QUART)),
        eq(x(1.0, ImperialUnitName.BOCHKA), x(40.0, ImperialUnitName.VEDRO)),
    ),
    formulaList = listOf())