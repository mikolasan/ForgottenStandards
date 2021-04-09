package io.github.mikolasan.ratiogenerator

object MinTemperatureUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.DOLYA, mutableMapOf(
                    ImperialUnitName.GRAN to 1.4
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}