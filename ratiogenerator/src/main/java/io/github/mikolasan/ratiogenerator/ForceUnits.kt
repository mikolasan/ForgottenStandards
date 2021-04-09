package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object ForceUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.DYNE, mapOf(
                    ImperialUnitName.DYNE to 1.0,
                    ImperialUnitName.NEWTON to 100000.0,
                    ImperialUnitName.KILONEWTON to 1.0E8,
                    ImperialUnitName.POUNDAL to 13825.5,
                    ImperialUnitName.KILOGRAM_FORCE to 980665.0)),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.NEWTON, mapOf(
                    ImperialUnitName.DYNE to 1.0E-5,
                    ImperialUnitName.NEWTON to 1.0,
                    ImperialUnitName.KILONEWTON to 1000.0,
                    ImperialUnitName.POUNDAL to 0.13825500000000002,
                    ImperialUnitName.KILOGRAM_FORCE to 9.806650000000001)),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.KILONEWTON, mapOf(
                    ImperialUnitName.DYNE to 1.0E-8,
                    ImperialUnitName.NEWTON to 0.001,
                    ImperialUnitName.KILONEWTON to 1.0,
                    ImperialUnitName.POUNDAL to 1.3825500000000002E-4,
                    ImperialUnitName.KILOGRAM_FORCE to 0.009806650000000002)),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.POUNDAL, mapOf(
                    ImperialUnitName.DYNE to 7.233011464323171E-5,
                    ImperialUnitName.NEWTON to 7.23301146432317,
                    ImperialUnitName.KILONEWTON to 7233.011464323171,
                    ImperialUnitName.POUNDAL to 1.0,
                    ImperialUnitName.KILOGRAM_FORCE to 70.93161187660482)),
            ImperialUnit(ImperialUnitType.FORCE, ImperialUnitName.KILOGRAM_FORCE, mapOf(
                    ImperialUnitName.DYNE to 1.0197162129779282E-6,
                    ImperialUnitName.NEWTON to 0.10197162129779282,
                    ImperialUnitName.KILONEWTON to 101.97162129779282,
                    ImperialUnitName.POUNDAL to 0.014098086502526346,
                    ImperialUnitName.KILOGRAM_FORCE to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
