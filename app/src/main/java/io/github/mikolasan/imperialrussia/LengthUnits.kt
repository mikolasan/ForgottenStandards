package io.github.mikolasan.imperialrussia

object LengthUnits {
    private val yardRatio = mutableMapOf(
            ImperialUnitName.INCH to 1.0/28.0, // 1 inch = 1/28 yards
            ImperialUnitName.TIP to 0.0625, // 1 tip = 1/16 yards
            ImperialUnitName.QUARTER to 0.25, // 1 quarter = 1/4 yards
            ImperialUnitName.FOOT to 3.0/7.0, //// 1 foot = 1/7 fathom = 3/7 yards
            ImperialUnitName.FATHOM to 3.0, // 1 fathom = 3 yards
            ImperialUnitName.ELBOW to 2.0/3.0, // 1 elbow = 2/3 yards
            ImperialUnitName.TURN to 1500.0, //// 1 turn = 500 fathoms = 1500 yards
            ImperialUnitName.MILE to 10500.0 //// 1 mile = 7 turns = 10500 yards
    )

    private val inchRatio = mutableMapOf(
            ImperialUnitName.TIP to 1.75, // 1 top = 1.75 inches
            ImperialUnitName.QUARTER to 7.0, // 1 quarter = 7 inches
            ImperialUnitName.FOOT to 12.0, // 1 foot = 12 inches
            ImperialUnitName.YARD to 28.0, // 1 yard = 28 inches
            ImperialUnitName.FATHOM to 84.0, // 1 fathom = 84 inches
            ImperialUnitName.TURN to 42000.0, //// 1 turn = 500 fathoms = 42000 inches
            ImperialUnitName.MILE to 294000.0 //// 1 mile = 7 turns = 294000 inches
    )

    private val centimeterRatio = mutableMapOf(
            ImperialUnitName.METER to 100.0, // 1 meter = 100 cm
            ImperialUnitName.INCH to 2.54, // 1 inch ≈ 2.54 cm
            ImperialUnitName.TIP to 4.445, // 1 tip ≈ 4.445 cm
            ImperialUnitName.QUARTER to 17.78, // 1 quarter ≈ 17.78 cm
            ImperialUnitName.FOOT to 30.48, // 1 foot ≈ 30.48 cm
            ImperialUnitName.FATHOM to 213.36, // 1 fathom ≈ 213.36 cm
            ImperialUnitName.ELBOW to 47.41333,
            //ImperialUnitName.STRIDE to 71.0, // TODO
            ImperialUnitName.YARD to 71.12 // 1 yard ≈ 71.12 cm
    )

    private val millimeterRatio = mutableMapOf(
            ImperialUnitName.POINT to 0.254,
            ImperialUnitName.LINE to 2.54,
            ImperialUnitName.METER to 1000.0,
            ImperialUnitName.YARD to 711.2
    )

    private val kilometerRatio = mutableMapOf(
            ImperialUnitName.QUARTER to 0.0001778,
            ImperialUnitName.TURN to 1.0668, // 1 turn ≈ 1.0668 km
            ImperialUnitName.MILE to 7.4676, // 1 mile = 7 turns ≈ 7 * 1.0668 km
            ImperialUnitName.METER to 0.001
    )

    val lengthUnits = arrayOf(
            ImperialUnit(R.string.unit_yard, ImperialUnitName.YARD, yardRatio),
            ImperialUnit(R.string.unit_inch, ImperialUnitName.INCH, inchRatio),
            ImperialUnit(R.string.unit_tip, ImperialUnitName.TIP, mutableMapOf(
                    ImperialUnitName.ELBOW to 8.0, //// 1 elbow = 8 tips
                    ImperialUnitName.QUARTER to 4.0, // 1 quarter = 4 tips
                    ImperialUnitName.FATHOM to 48.0, // 1 fathom = 48 tips
                    ImperialUnitName.YARD to 16.0)), // 1 yard = 16 tips
            ImperialUnit(R.string.unit_palm, ImperialUnitName.PALM, mutableMapOf(
                    ImperialUnitName.FOOT to 27.0/7.0, //// 1 foot = 3/7 yards = 3/7 * 3/2 elbows = 9/14 * 6 palms
                    ImperialUnitName.TURN to 13500.0, //// 1 turn = 500 fathoms = 3500 feet = 3500 * 27 / 7 plams
                    ImperialUnitName.ELBOW to 6.0)), // 1 elbow = 6 palms
            ImperialUnit(R.string.unit_quarter, ImperialUnitName.QUARTER, mutableMapOf(
                    ImperialUnitName.ELBOW to 2.0, // 1 elbow = 2 quarters
                    ImperialUnitName.FATHOM to 12.0, // 1 fathom = 12 quarters
                    ImperialUnitName.TIP to 0.25, // 1 tip = 1/4 quarters
                    ImperialUnitName.YARD to 4.0)), // 1 yard = 4 quarters
            ImperialUnit(R.string.unit_elbow, ImperialUnitName.ELBOW, mutableMapOf(
                    ImperialUnitName.YARD to 3.0/2.0)), // 1 yard = 3/2 elbows
            ImperialUnit(R.string.unit_stride, ImperialUnitName.STRIDE, mutableMapOf(ImperialUnitName.YARD to 1.0)),
            ImperialUnit(R.string.unit_foot, ImperialUnitName.FOOT, mutableMapOf(
                    ImperialUnitName.FATHOM to 7.0, // 1 fathom = 7 feet
                    ImperialUnitName.YARD to 7.0/3.0)), //// 1 yard = 7/3 feet
            ImperialUnit(R.string.unit_fathom, ImperialUnitName.FATHOM, mutableMapOf(
                    ImperialUnitName.TURN to 500.0, // 1 turn = 500 fathoms
                    ImperialUnitName.FOOT to 1.0/7.0, // 1 foot = 1/7 fathoms
                    ImperialUnitName.QUARTER to 1.0/12.0, // 1 quarter = 1/12 fathoms
                    ImperialUnitName.YARD to 1.0/3.0)), // 1 yard = 1/3 fathoms

            ImperialUnit(R.string.unit_point, ImperialUnitName.POINT, mutableMapOf(ImperialUnitName.INCH to 100.0)),
            ImperialUnit(R.string.unit_line, ImperialUnitName.LINE, mutableMapOf(ImperialUnitName.INCH to 10.0)),

            ImperialUnit(R.string.unit_turn, ImperialUnitName.TURN, mutableMapOf(
                    ImperialUnitName.MILE to 7.0, // 1 mile = 7 turns
                    ImperialUnitName.FATHOM to 1.0/500.0, // 1 fathom = 1/500 turns
                    ImperialUnitName.YARD to 1.0/1500.0)),
            ImperialUnit(R.string.unit_mile, ImperialUnitName.MILE, mutableMapOf(
                    ImperialUnitName.TURN to 1.0/7.0, // 1 turn = 1/7 miles
                    ImperialUnitName.YARD to 1.0/10500.0)),
            ImperialUnit(R.string.unit_kilometer, ImperialUnitName.KILOMETER, kilometerRatio),
            ImperialUnit(R.string.unit_meter, ImperialUnitName.METER, mutableMapOf(
                    ImperialUnitName.TURN to 1066.8, // 1 turn ≈ 1066.8 m
                    ImperialUnitName.CENTIMETER to 0.01)),
            ImperialUnit(R.string.unit_decimeter, ImperialUnitName.DECIMETER, mutableMapOf(
                    ImperialUnitName.METER to 10.0,
                    ImperialUnitName.YARD to 7.112)),
            ImperialUnit(R.string.unit_centimeter, ImperialUnitName.CENTIMETER, centimeterRatio),
            ImperialUnit(R.string.unit_millimeter, ImperialUnitName.MILLIMETER, millimeterRatio),
            ImperialUnit(R.string.unit_micrometer, ImperialUnitName.MICROMETER, mutableMapOf(
                    ImperialUnitName.MILLIMETER to 1000.0,
                    ImperialUnitName.CENTIMETER to 10000.0,
                    ImperialUnitName.DECIMETER to 100000.0,
                    ImperialUnitName.METER to 1000000.0))
    )

    private fun makeUnitByNameMap(units: Array<ImperialUnit>): Map<ImperialUnitName, ImperialUnit> {
        val map: MutableMap<ImperialUnitName, ImperialUnit> = mutableMapOf()
        units.forEach {
            map[it.unitName] = it
        }
        return map.toMap()
    }

    val imperialUnits = makeUnitByNameMap(lengthUnits)
}