package io.github.mikolasan.ratiogenerator

object MinFuelUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.FUEL, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}