package io.github.mikolasan.ratiogenerator

object MinFuelUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.KM_PER_LITER, mutableMapOf(
                    ImperialUnitName.LITER_ON_100KM to 100.0
            )),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.MILE_PER_GALLON, mutableMapOf(
                    ImperialUnitName.KM_PER_LITER to 2.352146
            )),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.LITER_ON_100KM, mutableMapOf(
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}