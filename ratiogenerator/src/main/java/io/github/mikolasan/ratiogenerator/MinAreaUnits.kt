package io.github.mikolasan.ratiogenerator

object MinAreaUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.DESYATINA, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.ARE, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.ACRE, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.HECTARE, mutableMapOf(
                    ImperialUnitName.DESYATINA to 1.09254
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_CENTIMETER, mutableMapOf(
                    ImperialUnitName.SQUARE_METER to 10000.0,
                    ImperialUnitName.SQUARE_INCH to 6.4516
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_KILOMETER, mutableMapOf(
                    ImperialUnitName.SQUARE_MILE to 2.589988110336
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_METER, mutableMapOf(
                    ImperialUnitName.ARE to 100.0,
                    ImperialUnitName.HECTARE to 10000.0,
                    ImperialUnitName.SQUARE_KILOMETER to 1000000.0,
                    ImperialUnitName.SQUARE_FOOT to 0.09290304,
                    ImperialUnitName.DESYATINA to 10925.4
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_INCH, mutableMapOf(
                    ImperialUnitName.SQUARE_FOOT to 144.0
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_FOOT, mutableMapOf(
                    ImperialUnitName.ACRE to 43560.0,
                    ImperialUnitName.SQUARE_MILE to 27878400.0
            )),
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.SQUARE_MILE, mutableMapOf(

            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}