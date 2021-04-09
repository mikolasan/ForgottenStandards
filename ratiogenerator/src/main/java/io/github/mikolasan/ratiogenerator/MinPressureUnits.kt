package io.github.mikolasan.ratiogenerator

object MinPressureUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.BAR, mutableMapOf(
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.PASCAL, mutableMapOf(
                    ImperialUnitName.KILOPASCAL to 1000.0,
                    ImperialUnitName.ATMOSPHERE to 101325.0
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.KILOPASCAL, mutableMapOf(
                    ImperialUnitName.BAR to 100.0
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.ATMOSPHERE, mutableMapOf(
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.INCH_OF_MERCURY, mutableMapOf(
                    ImperialUnitName.TORR to 25.4
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.POUND_PER_SQUARE_INCH, mutableMapOf(
                    ImperialUnitName.BAR to 14.503775
            )),
            ImperialUnit(ImperialUnitType.PRESSURE, ImperialUnitName.TORR, mutableMapOf(
                    ImperialUnitName.ATMOSPHERE to 760.0
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}