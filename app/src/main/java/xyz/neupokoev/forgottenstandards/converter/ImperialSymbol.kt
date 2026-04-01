package xyz.neupokoev.forgottenstandards.converter

import io.github.mikolasan.ratiogenerator.ImperialUnitName

object ImperialSymbol {
    val symbols: Map<ImperialUnitName, String> = mapOf(
            ImperialUnitName.RADIAN to "rad",
            ImperialUnitName.DEGREE to "deg",
            ImperialUnitName.MINUTE_OF_ARC to "min",
            ImperialUnitName.SECOND_OF_ARC to "sec",
            ImperialUnitName.GRAD to "grad",

            // Area
            ImperialUnitName.ARE to "a", // sotka
            ImperialUnitName.HECTARE to "ha",
            ImperialUnitName.SQUARE_CENTIMETER to "cm²",
            ImperialUnitName.SQUARE_KILOMETER to "km²",
            ImperialUnitName.SQUARE_METER to "m²",
            ImperialUnitName.SQUARE_INCH to "in²",
            ImperialUnitName.SQUARE_FOOT to "ft²",
            ImperialUnitName.SQUARE_MILE to "mi²",

            // Cooking
            ImperialUnitName.TEASPOON to "tsp",
            ImperialUnitName.TABLESPOON to "tbsp",
            ImperialUnitName.CUP to "cup",
            ImperialUnitName.DASH to "dash",
            ImperialUnitName.PINCH to "pinch",
            ImperialUnitName.SMIDGEN to "smidgen",
            ImperialUnitName.DROP to "drop",
            ImperialUnitName.STAKAN to "st",

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
            ImperialUnitName.INCH to "in", // dyuym
            ImperialUnitName.FOOT to "ft",
            ImperialUnitName.MILE to "mi",
            ImperialUnitName.FURLONG to "fur",
            ImperialUnitName.CHAIN to "ch",

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
            ImperialUnitName.KILOMETER_PER_HOUR to "km/h",

            // Storage
            ImperialUnitName.BYTE to "B",
            ImperialUnitName.KIBIBYTE to "KiB",

            // Temperature
            ImperialUnitName.CELSIUS to "°C",
            ImperialUnitName.FAHRENHEIT to "°F",
            ImperialUnitName.KELVIN to "°K",
            ImperialUnitName.RANKINE to "°R",
            ImperialUnitName.REAUMUR to "°Re",


            // Time
            ImperialUnitName.HOUR to "h",


            // Volume
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
            ImperialUnitName.KILOGRAMM to "kg",
            ImperialUnitName.GRAMM to "g",
            ImperialUnitName.POUND to "lb",
            ImperialUnitName.STONE to "st",
            ImperialUnitName.GRAIN to "gr"
    )
}