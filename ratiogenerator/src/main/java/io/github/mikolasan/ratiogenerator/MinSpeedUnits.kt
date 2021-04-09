package io.github.mikolasan.ratiogenerator

object MinSpeedUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.METER_PER_SECOND, mutableMapOf(
                    ImperialUnitName.MACH to 343.2,
                    ImperialUnitName.KNOT to 0.514444,
                    ImperialUnitName.MILE_PER_HOUR to 0.44704
            )),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.MILE_PER_HOUR, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.KNOT, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.SPEED, ImperialUnitName.MACH, mutableMapOf(

            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}