package io.github.mikolasan.ratiogenerator

object MinResistanceUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.RESISTANCE, ImperialUnitName.OHM, mutableMapOf(
                    ImperialUnitName.KILOOHM to 1000.0
            )),
            ImperialUnit(ImperialUnitType.RESISTANCE, ImperialUnitName.KILOOHM, mutableMapOf(
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}