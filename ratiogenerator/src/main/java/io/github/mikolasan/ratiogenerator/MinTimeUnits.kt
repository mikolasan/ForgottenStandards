package io.github.mikolasan.ratiogenerator

object MinTimeUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.HOUR, mutableMapOf(
                    ImperialUnitName.DAY to 24.0
            )),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.DAY, mutableMapOf(
                    ImperialUnitName.WEEK to 7.0,
                    ImperialUnitName.YEAR to 365.0
            )),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.WEEK, mutableMapOf(
                    ImperialUnitName.FORTNIGHT to 2.0
            )),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.MONTH, mutableMapOf(
                    ImperialUnitName.YEAR to 12.0
            )),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.YEAR, mutableMapOf(
            )),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.FORTNIGHT, mutableMapOf(
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}