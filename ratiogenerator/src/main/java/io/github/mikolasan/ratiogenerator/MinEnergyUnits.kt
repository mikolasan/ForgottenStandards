package io.github.mikolasan.ratiogenerator

object MinEnergyUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.ENERGY, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}