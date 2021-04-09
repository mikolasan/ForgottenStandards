package io.github.mikolasan.ratiogenerator

object MinPowerUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.POWER, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}