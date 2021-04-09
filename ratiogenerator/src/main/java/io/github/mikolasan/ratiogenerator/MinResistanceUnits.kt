package io.github.mikolasan.ratiogenerator

object MinResistanceUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.RESISTANCE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}