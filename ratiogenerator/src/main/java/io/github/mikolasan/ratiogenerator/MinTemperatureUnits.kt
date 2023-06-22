package io.github.mikolasan.ratiogenerator

object MinTemperatureUnits : ImperialUnitCategory(
    type = ImperialUnitType.TEMPERATURE,
    ratioList = listOf(),
    formulaList = listOf(
        f(ImperialUnitName.FAHRENHEIT, "x * 9 / 5 + 32", ImperialUnitName.CELSIUS),
        f(ImperialUnitName.KELVIN, "x + 273.15", ImperialUnitName.CELSIUS),
        f(ImperialUnitName.REAUMUR, "x * 4 / 5", ImperialUnitName.CELSIUS),
        f(ImperialUnitName.RANKINE, "x + 459.67", ImperialUnitName.FAHRENHEIT)
    ))