package io.github.mikolasan.ratiogenerator

object MinTimeUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}