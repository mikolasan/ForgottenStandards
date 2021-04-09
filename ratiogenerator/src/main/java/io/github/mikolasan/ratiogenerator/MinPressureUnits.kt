package io.github.mikolasan.ratiogenerator

object MinPressureUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}