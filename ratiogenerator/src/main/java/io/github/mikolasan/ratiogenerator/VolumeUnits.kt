package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array

object VolumeUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.GARNETS, mapOf(
                    ImperialUnitName.GARNETS to 1.0,
                    ImperialUnitName.VEDRO to 0.25,
                    ImperialUnitName.OSMINA to 0.03125,
                    ImperialUnitName.SHKALIK to 50.0,
                    ImperialUnitName.SHTOF to 2.5)),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.VEDRO, mapOf(
                    ImperialUnitName.GARNETS to 4.0,
                    ImperialUnitName.VEDRO to 1.0,
                    ImperialUnitName.OSMINA to 0.125,
                    ImperialUnitName.SHKALIK to 200.0,
                    ImperialUnitName.SHTOF to 10.0)),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.OSMINA, mapOf(
                    ImperialUnitName.GARNETS to 32.0,
                    ImperialUnitName.VEDRO to 8.0,
                    ImperialUnitName.OSMINA to 1.0,
                    ImperialUnitName.SHKALIK to 1600.0,
                    ImperialUnitName.SHTOF to 80.0)),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.SHKALIK, mapOf(
                    ImperialUnitName.GARNETS to 0.02,
                    ImperialUnitName.VEDRO to 0.005,
                    ImperialUnitName.OSMINA to 6.25E-4,
                    ImperialUnitName.SHKALIK to 1.0,
                    ImperialUnitName.SHTOF to 0.049999999999999996)),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.SHTOF, mapOf(
                    ImperialUnitName.GARNETS to 0.4,
                    ImperialUnitName.VEDRO to 0.1,
                    ImperialUnitName.OSMINA to 0.0125,
                    ImperialUnitName.SHKALIK to 20.0,
                    ImperialUnitName.SHTOF to 1.0))
            )
}
