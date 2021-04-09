package io.github.mikolasan.ratiogenerator

object MinTemperatureUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.CELSIUS, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.FAHRENHEIT, mutableMapOf(
                    ImperialUnitName.CELSIUS to 1.0
            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.KELVIN, mutableMapOf(
                    ImperialUnitName.CELSIUS to 1.0
            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.RANKINE, mutableMapOf(
                    ImperialUnitName.CELSIUS to 1.0
            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.REAUMUR, mutableMapOf(
                    ImperialUnitName.CELSIUS to 1.25
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}