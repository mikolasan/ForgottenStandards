package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object AreaUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.DESYATINA, mapOf(
                    ImperialUnitName.DESYATINA to 1.0,
                    ImperialUnitName.ARE to 0.009152982957145735,
                    ImperialUnitName.ACRE to 0.37040807864242964,
                    ImperialUnitName.HECTARE to 0.9152982957145733,
                    ImperialUnitName.SQUARE_CENTIMETER to 9.152982957145735E-9,
                    ImperialUnitName.SQUARE_KILOMETER to 91.52982957145734,
                    ImperialUnitName.SQUARE_METER to 9.152982957145734E-5,
                    ImperialUnitName.SQUARE_INCH to 5.9051384846321433E-8,
                    ImperialUnitName.SQUARE_FOOT to 8.503399417870285E-6,
                    ImperialUnitName.SQUARE_MILE to 237.06117033115487)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.ARE, mapOf(
                    ImperialUnitName.DESYATINA to 109.25399999999999,
                    ImperialUnitName.ARE to 1.0,
                    ImperialUnitName.ACRE to 40.468564224000005,
                    ImperialUnitName.HECTARE to 100.0,
                    ImperialUnitName.SQUARE_CENTIMETER to 1.0E-6,
                    ImperialUnitName.SQUARE_KILOMETER to 10000.0,
                    ImperialUnitName.SQUARE_METER to 0.01,
                    ImperialUnitName.SQUARE_INCH to 6.451600000000001E-6,
                    ImperialUnitName.SQUARE_FOOT to 9.290304000000001E-4,
                    ImperialUnitName.SQUARE_MILE to 25899.881103359996)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.ACRE, mapOf(
                    ImperialUnitName.DESYATINA to 2.6997251346813678,
                    ImperialUnitName.ARE to 0.02471053814671653,
                    ImperialUnitName.ACRE to 1.0,
                    ImperialUnitName.HECTARE to 2.471053814671653,
                    ImperialUnitName.SQUARE_CENTIMETER to 2.471053814671653E-8,
                    ImperialUnitName.SQUARE_KILOMETER to 247.10538146716536,
                    ImperialUnitName.SQUARE_METER to 2.471053814671653E-4,
                    ImperialUnitName.SQUARE_INCH to 1.5942250790735638E-7,
                    ImperialUnitName.SQUARE_FOOT to 2.295684113865932E-5,
                    ImperialUnitName.SQUARE_MILE to 640.0)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.HECTARE, mapOf(
                    ImperialUnitName.DESYATINA to 1.09254,
                    ImperialUnitName.ARE to 0.01,
                    ImperialUnitName.ACRE to 0.4046856422400001,
                    ImperialUnitName.HECTARE to 1.0,
                    ImperialUnitName.SQUARE_CENTIMETER to 1.0E-8,
                    ImperialUnitName.SQUARE_KILOMETER to 100.00000000000001,
                    ImperialUnitName.SQUARE_METER to 1.0E-4,
                    ImperialUnitName.SQUARE_INCH to 6.451600000000001E-8,
                    ImperialUnitName.SQUARE_FOOT to 9.290304E-6,
                    ImperialUnitName.SQUARE_MILE to 258.99881103359996)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_CENTIMETER, mapOf(
                    ImperialUnitName.DESYATINA to 1.09254E8,
                    ImperialUnitName.ARE to 1000000.0,
                    ImperialUnitName.ACRE to 4.046856422400001E7,
                    ImperialUnitName.HECTARE to 1.0E8,
                    ImperialUnitName.SQUARE_CENTIMETER to 1.0,
                    ImperialUnitName.SQUARE_KILOMETER to 1.0E10,
                    ImperialUnitName.SQUARE_METER to 10000.0,
                    ImperialUnitName.SQUARE_INCH to 6.4516,
                    ImperialUnitName.SQUARE_FOOT to 929.0304,
                    ImperialUnitName.SQUARE_MILE to 2.5899881103359997E10)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_KILOMETER, mapOf(
                    ImperialUnitName.DESYATINA to 0.010925399999999998,
                    ImperialUnitName.ARE to 9.999999999999999E-5,
                    ImperialUnitName.ACRE to 0.0040468564224,
                    ImperialUnitName.HECTARE to 0.009999999999999998,
                    ImperialUnitName.SQUARE_CENTIMETER to 9.999999999999999E-11,
                    ImperialUnitName.SQUARE_KILOMETER to 1.0,
                    ImperialUnitName.SQUARE_METER to 1.0E-6,
                    ImperialUnitName.SQUARE_INCH to 6.4516E-10,
                    ImperialUnitName.SQUARE_FOOT to 9.290304E-8,
                    ImperialUnitName.SQUARE_MILE to 2.589988110336)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_METER, mapOf(
                    ImperialUnitName.DESYATINA to 10925.4,
                    ImperialUnitName.ARE to 100.0,
                    ImperialUnitName.ACRE to 4046.8564224000006,
                    ImperialUnitName.HECTARE to 10000.0,
                    ImperialUnitName.SQUARE_CENTIMETER to 1.0E-4,
                    ImperialUnitName.SQUARE_KILOMETER to 1000000.0,
                    ImperialUnitName.SQUARE_METER to 1.0,
                    ImperialUnitName.SQUARE_INCH to 6.451600000000001E-4,
                    ImperialUnitName.SQUARE_FOOT to 0.09290304,
                    ImperialUnitName.SQUARE_MILE to 2589988.1103359996)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_INCH, mapOf(
                    ImperialUnitName.DESYATINA to 1.6934403868807737E7,
                    ImperialUnitName.ARE to 155000.31000061997,
                    ImperialUnitName.ACRE to 6272640.0,
                    ImperialUnitName.HECTARE to 1.5500031000062E7,
                    ImperialUnitName.SQUARE_CENTIMETER to 0.15500031000062,
                    ImperialUnitName.SQUARE_KILOMETER to 1.5500031000062E9,
                    ImperialUnitName.SQUARE_METER to 1550.0031000062,
                    ImperialUnitName.SQUARE_INCH to 1.0,
                    ImperialUnitName.SQUARE_FOOT to 144.0,
                    ImperialUnitName.SQUARE_MILE to 4.0144896E9)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_FOOT, mapOf(
                    ImperialUnitName.DESYATINA to 117600.02686672039,
                    ImperialUnitName.ARE to 1076.391041670972,
                    ImperialUnitName.ACRE to 43560.0,
                    ImperialUnitName.HECTARE to 107639.10416709722,
                    ImperialUnitName.SQUARE_CENTIMETER to 0.0010763910416709721,
                    ImperialUnitName.SQUARE_KILOMETER to 1.0763910416709723E7,
                    ImperialUnitName.SQUARE_METER to 10.763910416709722,
                    ImperialUnitName.SQUARE_INCH to 0.006944444444444444,
                    ImperialUnitName.SQUARE_FOOT to 1.0,
                    ImperialUnitName.SQUARE_MILE to 2.78784E7)),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_MILE, mapOf(
                    ImperialUnitName.DESYATINA to 0.004218320522939638,
                    ImperialUnitName.ARE to 3.8610215854244585E-5,
                    ImperialUnitName.ACRE to 0.0015625,
                    ImperialUnitName.HECTARE to 0.0038610215854244585,
                    ImperialUnitName.SQUARE_CENTIMETER to 3.8610215854244583E-11,
                    ImperialUnitName.SQUARE_KILOMETER to 0.3861021585424459,
                    ImperialUnitName.SQUARE_METER to 3.8610215854244587E-7,
                    ImperialUnitName.SQUARE_INCH to 2.4909766860524435E-10,
                    ImperialUnitName.SQUARE_FOOT to 3.587006427915519E-8,
                    ImperialUnitName.SQUARE_MILE to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
