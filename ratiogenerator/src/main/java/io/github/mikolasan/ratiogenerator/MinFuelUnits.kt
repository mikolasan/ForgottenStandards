package io.github.mikolasan.ratiogenerator

object MinFuelUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.KM_L, mutableMapOf(
                    ImperialUnitName.L_100KM to 100.0
            )),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.MI_GAL, mutableMapOf(
                    ImperialUnitName.KM_L to 2.352146
            )),
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.L_100KM, mutableMapOf(
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}