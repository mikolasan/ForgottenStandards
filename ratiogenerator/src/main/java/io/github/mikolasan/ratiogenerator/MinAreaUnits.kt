package io.github.mikolasan.ratiogenerator

object MinAreaUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.AREA, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}