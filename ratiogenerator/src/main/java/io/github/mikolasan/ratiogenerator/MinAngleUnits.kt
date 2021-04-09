package io.github.mikolasan.ratiogenerator

object MinAngleUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.ANGLE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}