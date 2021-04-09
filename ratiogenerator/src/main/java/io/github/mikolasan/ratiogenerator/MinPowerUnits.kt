package io.github.mikolasan.ratiogenerator

object MinPowerUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.WATT, mutableMapOf(
                    ImperialUnitName.KILOWATT to 1000.0,
                    ImperialUnitName.HORSEPOWER to 745.7
            )),
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.KILOWATT, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.HORSEPOWER, mutableMapOf(

            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}