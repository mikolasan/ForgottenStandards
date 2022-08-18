package io.github.mikolasan.ratiogenerator

object MinTemperatureUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.CELSIUS, mutableMapOf(), mutableMapOf(
                    ImperialUnitName.FAHRENHEIT to "x * 9 / 5 + 32",
                    ImperialUnitName.KELVIN to "x + 273.15",
                    ImperialUnitName.REAUMUR to "x * 5 / 4"
            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.FAHRENHEIT, mutableMapOf(), mutableMapOf(
                    ImperialUnitName.RANKINE to "x + 459.67"
            )),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.KELVIN, mutableMapOf(), mutableMapOf()),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.RANKINE, mutableMapOf(), mutableMapOf()),
            ImperialUnit(ImperialUnitType.TEMPERATURE, ImperialUnitName.REAUMUR, mutableMapOf(), mutableMapOf())
    )

    override val nameMap = makeUnitByNameMap(units)
}