package io.github.mikolasan.ratiogenerator

object MinEnergyUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.JOULE, mutableMapOf(
                    ImperialUnitName.KILOJOULE to 1000.0,
                    ImperialUnitName.CALORIE to 4.184
            )),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOJOULE, mutableMapOf(
                    ImperialUnitName.KILOWATT_HOUR to 3600.0
            )),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOWATT_HOUR, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.CALORIE, mutableMapOf(
                    ImperialUnitName.KILOCALORIE to 1000.0
            )),
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.KILOCALORIE, mutableMapOf(

            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}