package io.github.mikolasan.ratiogenerator

object MinSpeedUnits : ImperialUnitCategory(
    type = ImperialUnitType.SPEED,
    ratioList = listOf(
        eq(x(1.0, ImperialUnitName.MACH), x(343.2, ImperialUnitName.METER_PER_SECOND)),
        eq(x(1.0, ImperialUnitName.KNOT), x(0.514444, ImperialUnitName.METER_PER_SECOND)),
        eq(x(1.0, ImperialUnitName.MILE_PER_HOUR), x(0.44704, ImperialUnitName.METER_PER_SECOND)),
    ),
    formulaList = listOf(
        f(ImperialUnitName.KILOMETER_PER_HOUR, "x * 36 / 10", ImperialUnitName.METER_PER_SECOND)
    ),
//    rangeList = listOf(
//        eq(r(Double.NaN, 1.0, ImperialUnitName.KNOT), x(0.0, ImperialUnitName.BEAUFORT)), //
//        eq(r(1.0, 3.0, ImperialUnitName.KNOT), x(1.0, ImperialUnitName.BEAUFORT)), // light air
//        eq(r(4.0, 6.0, ImperialUnitName.KNOT), x(2.0, ImperialUnitName.BEAUFORT)), // light breeze
//        eq(r(7.0, 10.0, ImperialUnitName.KNOT), x(3.0, ImperialUnitName.BEAUFORT)), // gentle breeze
//        eq(r(11.0, 16.0, ImperialUnitName.KNOT), x(4.0, ImperialUnitName.BEAUFORT)), // moderate breeze
//        eq(r(17.0, 21.0, ImperialUnitName.KNOT), x(5.0, ImperialUnitName.BEAUFORT)), // fresh breeze
//        eq(r(22.0, 27.0, ImperialUnitName.KNOT), x(6.0, ImperialUnitName.BEAUFORT)), // strong breeze
//        eq(r(28.0, 33.0, ImperialUnitName.KNOT), x(7.0, ImperialUnitName.BEAUFORT)), // near gale
//        eq(r(34.0, 40.0, ImperialUnitName.KNOT), x(8.0, ImperialUnitName.BEAUFORT)), // gale
//        eq(r(41.0, 47.0, ImperialUnitName.KNOT), x(9.0, ImperialUnitName.BEAUFORT)), // strong gale
//        eq(r(48.0, 55.0, ImperialUnitName.KNOT), x(10.0, ImperialUnitName.BEAUFORT)), // storm
//        eq(r(56.0, 63.0, ImperialUnitName.KNOT), x(11.0, ImperialUnitName.BEAUFORT)), // violent storm
//        eq(r(64.0, Double.NaN, ImperialUnitName.KNOT), x(12.0, ImperialUnitName.BEAUFORT)), // hurricane
//    ))
    rangeList = listOf(
        f(ImperialUnitName.KNOT, ImperialUnitName.BEAUFORT, listOf(
            eq(r(Double.NaN, 1.0), r(0.0, "still")),
            eq(r(1.0, 3.0), r(1.0, "light air")),
            eq(r(4.0, 6.0), r(2.0, "light breeze")),
            eq(r(7.0, 10.0), r(3.0, "gentle breeze")),
            eq(r(11.0, 16.0), r(4.0, "moderate breeze")),
            eq(r(17.0, 21.0), r(5.0, "fresh breeze")),
            eq(r(22.0, 27.0), r(6.0, "strong breeze")),
            eq(r(28.0, 33.0), r(7.0, "near gale")),
            eq(r(34.0, 40.0), r(8.0, "gale")),
            eq(r(41.0, 47.0), r(9.0, "strong gale")),
            eq(r(48.0, 55.0), r(10.0, "storm")),
            eq(r(56.0, 63.0), r(11.0, "violent storm")),
            eq(r(64.0, Double.NaN), r(12.0, "hurricane")),
        ))
    ))