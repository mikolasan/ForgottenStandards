package io.github.mikolasan.ratiogenerator

object MinCurrencyUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.DENGA, mutableMapOf(
                    ImperialUnitName.MOSKOVKA to 2.0
            )),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.KOPEIKA, mutableMapOf(
                    ImperialUnitName.DENGA to 100.0
            )),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.ALTIN, mutableMapOf(
                    ImperialUnitName.KOPEIKA to 3.0
            )),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.MOSKOVKA, mutableMapOf(

            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}