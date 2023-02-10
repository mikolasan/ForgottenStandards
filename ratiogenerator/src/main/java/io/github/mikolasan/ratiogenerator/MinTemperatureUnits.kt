package io.github.mikolasan.ratiogenerator

object MinTemperatureUnits : ImperialUnits(
    type = ImperialUnitType.TEMPERATURE,
    ratioList = listOf(),
    formulaList = listOf(
        f(ImperialUnitName.CELSIUS, "x * 9 / 5 + 32", ImperialUnitName.FAHRENHEIT),
        f(ImperialUnitName.CELSIUS, "x + 273.15", ImperialUnitName.KELVIN),
        f(ImperialUnitName.CELSIUS, "x * 5 / 4", ImperialUnitName.REAUMUR),
        f(ImperialUnitName.FAHRENHEIT, "x + 459.67", ImperialUnitName.RANKINE)
    ))