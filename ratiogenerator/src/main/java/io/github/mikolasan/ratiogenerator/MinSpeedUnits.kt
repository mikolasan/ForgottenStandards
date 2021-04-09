package io.github.mikolasan.ratiogenerator

object MinSpeedUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}