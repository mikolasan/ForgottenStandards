package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object PressureUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.BAR, mapOf(
                    ImperialUnitName.BAR to 1.0,
                    ImperialUnitName.PASCAL to 1.0E-5,
                    ImperialUnitName.KILOPASCAL to 0.01,
                    ImperialUnitName.ATMOSPHERE to 1.01325,
                    ImperialUnitName.INCH_OF_MERCURY to 5.2489121425611284E-5,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 0.06894756709891046,
                    ImperialUnitName.TORR to 0.0013332236842105263)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.PASCAL, mapOf(
                    ImperialUnitName.BAR to 100000.0,
                    ImperialUnitName.PASCAL to 1.0,
                    ImperialUnitName.KILOPASCAL to 1000.0,
                    ImperialUnitName.ATMOSPHERE to 101325.0,
                    ImperialUnitName.INCH_OF_MERCURY to 5.248912142561128,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 6894.756709891046,
                    ImperialUnitName.TORR to 133.32236842105263)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.KILOPASCAL, mapOf(
                    ImperialUnitName.BAR to 100.0,
                    ImperialUnitName.PASCAL to 0.001,
                    ImperialUnitName.KILOPASCAL to 1.0,
                    ImperialUnitName.ATMOSPHERE to 101.325,
                    ImperialUnitName.INCH_OF_MERCURY to 0.005248912142561128,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 6.894756709891046,
                    ImperialUnitName.TORR to 0.13332236842105263)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.ATMOSPHERE, mapOf(
                    ImperialUnitName.BAR to 0.9869232667160128,
                    ImperialUnitName.PASCAL to 9.869232667160129E-6,
                    ImperialUnitName.KILOPASCAL to 0.009869232667160128,
                    ImperialUnitName.ATMOSPHERE to 1.0,
                    ImperialUnitName.INCH_OF_MERCURY to 5.180273518441774E-5,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 0.0680459581533782,
                    ImperialUnitName.TORR to 0.0013157894736842105)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.INCH_OF_MERCURY, mapOf(
                    ImperialUnitName.BAR to 19051.56674068591,
                    ImperialUnitName.PASCAL to 0.1905156674068591,
                    ImperialUnitName.KILOPASCAL to 190.5156674068591,
                    ImperialUnitName.ATMOSPHERE to 19304.0,
                    ImperialUnitName.INCH_OF_MERCURY to 1.0,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 1313.5591761928129,
                    ImperialUnitName.TORR to 25.4)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.POUND_PER_SQUARE_INCH, mapOf(
                    ImperialUnitName.BAR to 14.503775,
                    ImperialUnitName.PASCAL to 1.4503775E-4,
                    ImperialUnitName.KILOPASCAL to 0.14503775,
                    ImperialUnitName.ATMOSPHERE to 14.695950018749999,
                    ImperialUnitName.INCH_OF_MERCURY to 7.612904071047453E-4,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 1.0,
                    ImperialUnitName.TORR to 0.019336776340460524)),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.TORR, mapOf(
                    ImperialUnitName.BAR to 750.0616827041697,
                    ImperialUnitName.PASCAL to 0.007500616827041697,
                    ImperialUnitName.KILOPASCAL to 7.500616827041697,
                    ImperialUnitName.ATMOSPHERE to 760.0,
                    ImperialUnitName.INCH_OF_MERCURY to 0.03937007874015748,
                    ImperialUnitName.POUND_PER_SQUARE_INCH to 51.714928196567435,
                    ImperialUnitName.TORR to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
