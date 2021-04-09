package io.github.mikolasan.ratiogenerator

object MinStorageUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}