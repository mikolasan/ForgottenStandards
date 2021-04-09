package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object TimeUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.HOUR, mapOf(
                    ImperialUnitName.HOUR to 1.0,
                    ImperialUnitName.DAY to 24.0,
                    ImperialUnitName.WEEK to 168.0,
                    ImperialUnitName.MONTH to 730.0,
                    ImperialUnitName.YEAR to 8760.0,
                    ImperialUnitName.FORTNIGHT to 336.0)),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.DAY, mapOf(
                    ImperialUnitName.HOUR to 0.041666666666666664,
                    ImperialUnitName.DAY to 1.0,
                    ImperialUnitName.WEEK to 7.0,
                    ImperialUnitName.MONTH to 30.416666666666668,
                    ImperialUnitName.YEAR to 365.0,
                    ImperialUnitName.FORTNIGHT to 14.0)),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.WEEK, mapOf(
                    ImperialUnitName.HOUR to 0.005952380952380952,
                    ImperialUnitName.DAY to 0.14285714285714285,
                    ImperialUnitName.WEEK to 1.0,
                    ImperialUnitName.MONTH to 4.345238095238095,
                    ImperialUnitName.YEAR to 52.14285714285714,
                    ImperialUnitName.FORTNIGHT to 2.0)),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.MONTH, mapOf(
                    ImperialUnitName.HOUR to 0.0013698630136986301,
                    ImperialUnitName.DAY to 0.03287671232876712,
                    ImperialUnitName.WEEK to 0.23013698630136986,
                    ImperialUnitName.MONTH to 1.0,
                    ImperialUnitName.YEAR to 12.0,
                    ImperialUnitName.FORTNIGHT to 0.4602739726027397)),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.YEAR, mapOf(
                    ImperialUnitName.HOUR to 1.1415525114155251E-4,
                    ImperialUnitName.DAY to 0.0027397260273972603,
                    ImperialUnitName.WEEK to 0.019178082191780823,
                    ImperialUnitName.MONTH to 0.08333333333333333,
                    ImperialUnitName.YEAR to 1.0,
                    ImperialUnitName.FORTNIGHT to 0.038356164383561646)),
            ImperialUnit(ImperialUnitType.TIME, ImperialUnitName.FORTNIGHT, mapOf(
                    ImperialUnitName.HOUR to 0.002976190476190476,
                    ImperialUnitName.DAY to 0.07142857142857142,
                    ImperialUnitName.WEEK to 0.5,
                    ImperialUnitName.MONTH to 2.1726190476190474,
                    ImperialUnitName.YEAR to 26.07142857142857,
                    ImperialUnitName.FORTNIGHT to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
