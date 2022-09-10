package io.github.mikolasan.ratiogenerator

typealias x<A, B> = Pair<A, B>
typealias eq<A, B> = Pair<A, B>
typealias f<A, B, C> = Triple<A, B, C>

object MinLengthUnits : ImperialUnits() {
    val conversionTable = mapOf(
        eq(x(1.0, ImperialUnitName.ARSHIN), x(28.0, ImperialUnitName.INCH)),
        eq(x(1.0, ImperialUnitName.ARSHIN), x(16.0, ImperialUnitName.VERSHOK)),
        eq(x(1.0, ImperialUnitName.ARSHIN), x(4.0, ImperialUnitName.SPAN)),
        eq(x(1.0, ImperialUnitName.SAZHEN), x(3.0, ImperialUnitName.ARSHIN)),
        eq(x(1.0, ImperialUnitName.SAZHEN), x(48.0, ImperialUnitName.VERSHOK)),
        eq(x(1.0, ImperialUnitName.SAZHEN), x(84.0, ImperialUnitName.INCH)),
        eq(x(3.0, ImperialUnitName.SPAN), x(4.0, ImperialUnitName.VERSHOK)),
        eq(x(1.0, ImperialUnitName.SPAN), x(7.0, ImperialUnitName.INCH)),
        eq(x(1.0, ImperialUnitName.INCH), x(2.54, ImperialUnitName.CENTIMETER)),
        eq(x(1.0, ImperialUnitName.INCH), x(10.0, ImperialUnitName.LINE)),
        eq(x(1.0, ImperialUnitName.INCH), x(100.0, ImperialUnitName.POINT)),
        eq(x(1.0, ImperialUnitName.FOOT), x(12.0, ImperialUnitName.INCH)),
        eq(x(3.0, ImperialUnitName.ELL), x(2.0, ImperialUnitName.ARSHIN)),
        eq(x(1.0, ImperialUnitName.ELL), x(6.0, ImperialUnitName.PALM)),
        eq(x(1.0, ImperialUnitName.ELL), x(8.0, ImperialUnitName.VERSHOK)),
        eq(x(1.0, ImperialUnitName.MILE), x(7.0, ImperialUnitName.VERST)),
        eq(x(1.0, ImperialUnitName.METER), x(10.0, ImperialUnitName.DECIMETER)),
        eq(x(1.0, ImperialUnitName.METER), x(100.0, ImperialUnitName.CENTIMETER)),
        eq(x(1.0, ImperialUnitName.METER), x(1000.0, ImperialUnitName.MILLIMETER)),
        eq(x(1.0, ImperialUnitName.METER), x(1000000.0, ImperialUnitName.MICROMETER)),
        eq(x(1.0, ImperialUnitName.KILOMETER), x(1000.0, ImperialUnitName.METER)),
        eq(x(1.0, ImperialUnitName.VERST), x(500.0, ImperialUnitName.SAZHEN)),
        eq(x(1.0, ImperialUnitName.POPRISCHE), x(20.0, ImperialUnitName.VERST)),
        eq(x(1.0, ImperialUnitName.VERST), x(1.0668, ImperialUnitName.KILOMETER)),
    )
    override val units = arrayOf(
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.PALM, mutableMapOf(
            ImperialUnitName.FOOT to 27.0 / 7.0, //// 1 foot = 3/7 arshins = 3/7 * 3/2 ells = 9/14 * 6 palms
            
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.SPAN, mutableMapOf(
            ImperialUnitName.ELL to 2.0, // 1 ell = 2 quarters
            ImperialUnitName.SAZHEN to 12.0, // 1 sazhen = 12 quarters
            ImperialUnitName.VERSHOK to 0.25, // 1 vershok = 1/4 quarters
        // TODO: step or arshin?
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.FOOT, mutableMapOf(
            ImperialUnitName.SAZHEN to 7.0, // 1 sazhen = 7 feet
            ImperialUnitName.ARSHIN to 7.0 / 3.0)), //// 1 arshin = 7/3 feet
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.SAZHEN, mutableMapOf(
            ImperialUnitName.VERST to 500.0, // 1 verst = 500 sazhens
            ImperialUnitName.FOOT to 1.0 / 7.0, // 1 foot = 1/7 sazhens
            ImperialUnitName.SPAN to 1.0 / 12.0, // 1 quarter = 1/12 sazhens


        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.POPRISCHE, mutableMapOf(ImperialUnitName.VERST to 0.05)),
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.VERST, mutableMapOf(
            ImperialUnitName.MILE to 7.0, // 1 mile = 7 versts
            ImperialUnitName.SAZHEN to 1.0 / 500.0, // 1 sazhen = 1/500 versts
            ImperialUnitName.ARSHIN to 1.0 / 1500.0)),
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.MILE, mutableMapOf(
            ImperialUnitName.VERST to 1.0 / 7.0, // 1 verst = 1/7 miles
            ImperialUnitName.ARSHIN to 1.0 / 10500.0)),
        ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.METER, mutableMapOf(
            ImperialUnitName.VERST to 1066.8, // 1 verst ≈ 1066.8 m
            ImperialUnitName.KILOMETER to 1000.0, // 1 km = 1000 m
            ImperialUnitName.CENTIMETER to 0.01)),

    )

    override val nameMap = makeUnitByNameMap(units)
}