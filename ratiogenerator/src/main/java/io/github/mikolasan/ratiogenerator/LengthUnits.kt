package io.github.mikolasan.ratiogenerator

object LengthUnits {
    private val arshinRatio = mutableMapOf(
            ImperialUnitName.INCH to 1.0/28.0, // 1 inch = 1/28 arshins
            ImperialUnitName.VERSHOK to 0.0625, // 1 vershok = 1/16 arshins
            ImperialUnitName.SPAN to 0.25, // 1 quarter = 1/4 arshins
            ImperialUnitName.FOOT to 3.0/7.0, //// 1 foot = 1/7 sazhen = 3/7 arshins
            ImperialUnitName.SAZHEN to 3.0, // 1 sazhen = 3 arshins
            ImperialUnitName.ELL to 2.0/3.0, // 1 ell = 2/3 arshins
            ImperialUnitName.VERST to 1500.0, //// 1 verst = 500 sazhens = 1500 arshins
            ImperialUnitName.MILE to 10500.0 //// 1 mile = 7 versts = 10500 arshins
    )

    private val inchRatio = mutableMapOf(
            ImperialUnitName.VERSHOK to 1.75, // 1 vershok = 1.75 inches
            ImperialUnitName.SPAN to 7.0, // 1 quarter = 7 inches
            ImperialUnitName.FOOT to 12.0, // 1 foot = 12 inches
            ImperialUnitName.ARSHIN to 28.0, // 1 arshin = 28 inches
            ImperialUnitName.SAZHEN to 84.0, // 1 sazhen = 84 inches
            ImperialUnitName.VERST to 42000.0, //// 1 verst = 500 sazhens = 42000 inches
            ImperialUnitName.MILE to 294000.0 //// 1 mile = 7 versts = 294000 inches
    )

    private val centimeterRatio = mutableMapOf(
            ImperialUnitName.METER to 100.0, // 1 meter = 100 cm
            ImperialUnitName.INCH to 2.54, // 1 inch ≈ 2.54 cm
            ImperialUnitName.VERSHOK to 4.445, // 1 vershok ≈ 4.445 cm
            ImperialUnitName.SPAN to 17.78, // 1 quarter ≈ 17.78 cm
            ImperialUnitName.FOOT to 30.48, // 1 foot ≈ 30.48 cm
            ImperialUnitName.SAZHEN to 213.36, // 1 sazhen ≈ 213.36 cm
            ImperialUnitName.ELL to 47.41333,
            //ImperialUnitName.STRIDE to 71.0, // TODO
            ImperialUnitName.ARSHIN to 71.12 // 1 arshin ≈ 71.12 cm
    )

    private val millimeterRatio = mutableMapOf(
            ImperialUnitName.POINT to 0.254,
            ImperialUnitName.LINE to 2.54,
            ImperialUnitName.METER to 1000.0,
            ImperialUnitName.ARSHIN to 711.2
    )

    private val kilometerRatio = mutableMapOf(
            ImperialUnitName.SPAN to 0.0001778,
            ImperialUnitName.VERST to 1.0668, // 1 verst ≈ 1.0668 km
            ImperialUnitName.MILE to 7.4676, // 1 mile = 7 versts ≈ 7 * 1.0668 km
            ImperialUnitName.METER to 0.001
    )

    val lengthUnits = arrayOf(
            ImperialUnit(R.string.unit_arshin, ImperialUnitName.ARSHIN, arshinRatio),
            ImperialUnit(R.string.unit_inch, ImperialUnitName.INCH, inchRatio),
            ImperialUnit(R.string.unit_vershok, ImperialUnitName.VERSHOK, mutableMapOf(
                    ImperialUnitName.ELL to 8.0, //// 1 ell = 8 vershoks
                    ImperialUnitName.SPAN to 4.0, // 1 quarter = 4 vershoks
                    ImperialUnitName.SAZHEN to 48.0, // 1 sazhen = 48 vershoks
                    ImperialUnitName.ARSHIN to 16.0)), // 1 arshin = 16 vershoks
            ImperialUnit(R.string.unit_palm, ImperialUnitName.PALM, mutableMapOf(
                    ImperialUnitName.FOOT to 27.0 / 7.0, //// 1 foot = 3/7 arshins = 3/7 * 3/2 ells = 9/14 * 6 palms
                    ImperialUnitName.VERST to 13500.0, //// 1 verst = 500 sazhens = 3500 feet = 3500 * 27 / 7 palms
                    ImperialUnitName.ELL to 6.0)), // 1 ell = 6 palms
            ImperialUnit(R.string.unit_span, ImperialUnitName.SPAN, mutableMapOf(
                    ImperialUnitName.ELL to 2.0, // 1 ell = 2 quarters
                    ImperialUnitName.SAZHEN to 12.0, // 1 sazhen = 12 quarters
                    ImperialUnitName.VERSHOK to 0.25, // 1 vershok = 1/4 quarters
                    ImperialUnitName.ARSHIN to 4.0)), // 1 arshin = 4 quarters
            ImperialUnit(R.string.unit_ell, ImperialUnitName.ELL, mutableMapOf(
                    ImperialUnitName.ARSHIN to 3.0 / 2.0)), // 1 arshin = 3/2 ells
            ImperialUnit(R.string.unit_step, ImperialUnitName.STEP, mutableMapOf(ImperialUnitName.ARSHIN to 1.0)),
            ImperialUnit(R.string.unit_foot, ImperialUnitName.FOOT, mutableMapOf(
                    ImperialUnitName.SAZHEN to 7.0, // 1 sazhen = 7 feet
                    ImperialUnitName.ARSHIN to 7.0 / 3.0)), //// 1 arshin = 7/3 feet
            ImperialUnit(R.string.unit_sazhen, ImperialUnitName.SAZHEN, mutableMapOf(
                    ImperialUnitName.VERST to 500.0, // 1 verst = 500 sazhens
                    ImperialUnitName.FOOT to 1.0 / 7.0, // 1 foot = 1/7 sazhens
                    ImperialUnitName.SPAN to 1.0 / 12.0, // 1 quarter = 1/12 sazhens
                    ImperialUnitName.ARSHIN to 1.0 / 3.0)), // 1 arshin = 1/3 sazhens

            ImperialUnit(R.string.unit_point, ImperialUnitName.POINT, mutableMapOf(ImperialUnitName.INCH to 100.0)),
            ImperialUnit(R.string.unit_line, ImperialUnitName.LINE, mutableMapOf(ImperialUnitName.INCH to 10.0)),

            ImperialUnit(R.string.unit_verst, ImperialUnitName.VERST, mutableMapOf(
                    ImperialUnitName.MILE to 7.0, // 1 mile = 7 versts
                    ImperialUnitName.SAZHEN to 1.0 / 500.0, // 1 sazhen = 1/500 versts
                    ImperialUnitName.ARSHIN to 1.0 / 1500.0)),
            ImperialUnit(R.string.unit_mile, ImperialUnitName.MILE, mutableMapOf(
                    ImperialUnitName.VERST to 1.0 / 7.0, // 1 verst = 1/7 miles
                    ImperialUnitName.ARSHIN to 1.0 / 10500.0)),
            ImperialUnit(R.string.unit_kilometer, ImperialUnitName.KILOMETER, kilometerRatio),
            ImperialUnit(R.string.unit_meter, ImperialUnitName.METER, mutableMapOf(
                    ImperialUnitName.VERST to 1066.8, // 1 verst ≈ 1066.8 m
                    ImperialUnitName.CENTIMETER to 0.01)),
            ImperialUnit(R.string.unit_decimeter, ImperialUnitName.DECIMETER, mutableMapOf(
                    ImperialUnitName.METER to 10.0,
                    ImperialUnitName.ARSHIN to 7.112)),
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