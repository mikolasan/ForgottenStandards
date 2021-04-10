package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnitName

object ImperialSymbol {
    val symbols: Map<ImperialUnitName, String> = mapOf(
            ImperialUnitName.RADIAN to "rad",
            ImperialUnitName.DEGREE to "deg",
            ImperialUnitName.MINUTE_OF_ARC to "min",
            ImperialUnitName.SECOND_OF_ARC to "sec",
            ImperialUnitName.GRAD to "grad",
//            ImperialUnitName.CIRCLE to "circle",

            // Area
//            ImperialUnitName.DESYATINA to "",
            ImperialUnitName.ARE to "a", // sotka
//            ImperialUnitName.ACRE to "",
            ImperialUnitName.HECTARE to "ha",
            ImperialUnitName.SQUARE_CENTIMETER to "cm²",
            ImperialUnitName.SQUARE_KILOMETER to "km²",
            ImperialUnitName.SQUARE_METER to "m²",
            ImperialUnitName.SQUARE_INCH to "in²",
            ImperialUnitName.SQUARE_FOOT to "ft²",
            ImperialUnitName.SQUARE_MILE to "mi²",

            // Currency
//            ImperialUnitName.MOSKOVKA to "",
//            ImperialUnitName.DENGA to "",
//            ImperialUnitName.KOPEIKA to "",
//            ImperialUnitName.ALTIN to "",

            // Energy
            ImperialUnitName.JOULE to "J",
            ImperialUnitName.KILOJOULE to "kJ",
            ImperialUnitName.KILOWATT_HOUR to "kW/h",
            ImperialUnitName.CALORIE to "cal",
            ImperialUnitName.KILOCALORIE to "kcal",

            // Force
            ImperialUnitName.DYNE to "dyn",
            ImperialUnitName.NEWTON to "N",
            ImperialUnitName.KILONEWTON to "kN",
//            ImperialUnitName.POUNDAL to "",
            ImperialUnitName.KILOGRAM_FORCE to "kgf",

            // Fuel
            ImperialUnitName.KM_PER_LITER to "km/l",
            ImperialUnitName.MILE_PER_GALLON to "mi/gal",
            ImperialUnitName.LITER_ON_100KM to "l/100km",

            // Length
            ImperialUnitName.KILOMETER to "km",
            ImperialUnitName.METER to "m",
            ImperialUnitName.DECIMETER to "dm",
            ImperialUnitName.CENTIMETER to "cm",
            ImperialUnitName.MILLIMETER to "mm",
            ImperialUnitName.MICROMETER to "um",
//            ImperialUnitName.POINT to "", // tochka
//            ImperialUnitName.LINE to "", // liniya
            ImperialUnitName.INCH to "in", // dyuym
//            ImperialUnitName.VERSHOK to "",
//            ImperialUnitName.PALM to "", // ladon
//            ImperialUnitName.SPAN to "", // pyad
            ImperialUnitName.FOOT to "ft",
//            ImperialUnitName.ELL to "", // lokot
//            ImperialUnitName.STEP to "",
//            ImperialUnitName.ARSHIN to "",
//            ImperialUnitName.SAZHEN to "",
//            ImperialUnitName.VERST to "",
            ImperialUnitName.MILE to "mi",
//            ImperialUnitName.POPRISCHE to "",
            ImperialUnitName.FURLONG to "fur",
            ImperialUnitName.CHAIN to "ch",
//            ImperialUnitName.ROD to "",
//            ImperialUnitName.PARSEC to "",
//            ImperialUnitName.LIGHT_YEAR to "",
//            ImperialUnitName.ASTRONOMICAL_UNIT to "",

            // Power
            ImperialUnitName.WATT to "W",
            ImperialUnitName.KILOWATT to "kW",
            ImperialUnitName.HORSEPOWER to "HP",


            // Pressure
            ImperialUnitName.BAR to "bar",
            ImperialUnitName.PASCAL to "Pa",
            ImperialUnitName.KILOPASCAL to "kPa",
            ImperialUnitName.ATMOSPHERE to "atm",
            ImperialUnitName.INCH_OF_MERCURY to "inHg", // "Hg
            ImperialUnitName.POUND_PER_SQUARE_INCH to "psi",
            ImperialUnitName.TORR to "mmHg",

            // Resistance
            ImperialUnitName.OHM to "Ω",
            ImperialUnitName.KILOOHM to "kΩ",

            // Speed
            ImperialUnitName.METER_PER_SECOND to "m/s",
            ImperialUnitName.MILE_PER_HOUR to "mph",
//            ImperialUnitName.KNOT to "",
//            ImperialUnitName.MACH to "",
            //ImperialUnitName.BEAUFORT to "", // TODO: this unit is not in constant relation to other units, need a formula

            // Storage
//            ImperialUnitName.BIT to "",
            ImperialUnitName.BYTE to "B",
            ImperialUnitName.KIBIBYTE to "KiB",
//            ImperialUnitName.PACKET to "",
//            ImperialUnitName.BLOCK to "",

            // Temperature
            ImperialUnitName.CELSIUS to "°C",
            ImperialUnitName.FAHRENHEIT to "°F",
            ImperialUnitName.KELVIN to "°K",
            ImperialUnitName.RANKINE to "°R",
            ImperialUnitName.REAUMUR to "°Re",


            // Time
            ImperialUnitName.HOUR to "h",
//            ImperialUnitName.DAY to "",
//            ImperialUnitName.WEEK to "",
//            ImperialUnitName.MONTH to "",
//            ImperialUnitName.YEAR to "",
//            ImperialUnitName.FORTNIGHT to "",


            // Volume
//            ImperialUnitName.BOCHKA to "",
//            ImperialUnitName.VEDRO to "",
//            ImperialUnitName.GARNETS to "",
//            ImperialUnitName.OSMINA to "",
//            ImperialUnitName.SHKALIK to "",
//            ImperialUnitName.CHARKA to "",
//            ImperialUnitName.SHTOF to "",
//            ImperialUnitName.SOROKOVKA to "",
            ImperialUnitName.LITER to "L",
            ImperialUnitName.MILLILITER to "mL",
            ImperialUnitName.GALLON to "gal",
            ImperialUnitName.FLUID_ONCE to "fl oz",
            ImperialUnitName.PINT to "pt",
            ImperialUnitName.QUART to "qt",
            ImperialUnitName.CUBIC_INCH to "in³",
            ImperialUnitName.CUBIC_FOOT to "ft³",
            ImperialUnitName.CUBIC_METER to "m³",

            // Weight
//            ImperialUnitName.DOLYA to "",
//            ImperialUnitName.ZOLOTNIK to "",
//            ImperialUnitName.LOT to "",
//            ImperialUnitName.FUNT to "",
//            ImperialUnitName.POOD to "",
//            ImperialUnitName.BERKOVETS to "",
//            ImperialUnitName.GRAN to "",
//            ImperialUnitName.SCRUPUL to "",
//            ImperialUnitName.DRAKHMA to "dr",
//            ImperialUnitName.UNTSIYA to "",
            ImperialUnitName.KILOGRAMM to "kg",
            ImperialUnitName.GRAMM to "g",
            ImperialUnitName.POUND to "lb",
            ImperialUnitName.STONE to "st",
//            ImperialUnitName.CARAT to "",
//            ImperialUnitName.TOLA to "",
//            ImperialUnitName.RATTI to "",
            ImperialUnitName.GRAIN to "gr"
    )
}