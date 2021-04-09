package io.github.mikolasan.ratiogenerator

object MinForceUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}