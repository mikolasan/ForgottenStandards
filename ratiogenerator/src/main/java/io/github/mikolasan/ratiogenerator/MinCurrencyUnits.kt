package io.github.mikolasan.ratiogenerator

object MinCurrencyUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}