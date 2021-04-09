package io.github.mikolasan.ratiogenerator

object MinForceUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.DYNE, mutableMapOf(
                    ImperialUnitName.NEWTON to 100000.0,
                    ImperialUnitName.POUNDAL to 13825.5,
                    ImperialUnitName.KILOGRAM_FORCE to 980665.0
            )),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.NEWTON, mutableMapOf(
                    ImperialUnitName.KILONEWTON to 1000.0
            )),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.KILONEWTON, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.POUNDAL, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.KILOGRAM_FORCE, mutableMapOf(

            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}