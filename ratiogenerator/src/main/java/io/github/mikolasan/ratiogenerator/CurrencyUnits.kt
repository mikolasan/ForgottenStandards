package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object CurrencyUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.DENGA, mapOf(
                    ImperialUnitName.DENGA to 1.0,
                    ImperialUnitName.KOPEIKA to 0.01,
                    ImperialUnitName.ALTIN to 0.0033333333333333335,
                    ImperialUnitName.MOSKOVKA to 2.0)),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.KOPEIKA, mapOf(
                    ImperialUnitName.DENGA to 100.0,
                    ImperialUnitName.KOPEIKA to 1.0,
                    ImperialUnitName.ALTIN to 0.3333333333333333,
                    ImperialUnitName.MOSKOVKA to 200.0)),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.ALTIN, mapOf(
                    ImperialUnitName.DENGA to 300.0,
                    ImperialUnitName.KOPEIKA to 3.0,
                    ImperialUnitName.ALTIN to 1.0,
                    ImperialUnitName.MOSKOVKA to 600.0)),
            ImperialUnit(ImperialUnitType.CURRENCY, ImperialUnitName.MOSKOVKA, mapOf(
                    ImperialUnitName.DENGA to 0.5,
                    ImperialUnitName.KOPEIKA to 0.005,
                    ImperialUnitName.ALTIN to 0.0016666666666666668,
                    ImperialUnitName.MOSKOVKA to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
