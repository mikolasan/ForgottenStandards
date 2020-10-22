package io.github.mikolasan.ratiogenerator

object MinVolumeUnits {
    val volumeUnits = arrayOf(
            ImperialUnit(ImperialUnitName.GARNETS, mutableMapOf(
                    ImperialUnitName.VEDRO to 0.25
            )),
            ImperialUnit(ImperialUnitName.VEDRO, mutableMapOf(
                    ImperialUnitName.GARNETS to 4.0
            )),
            ImperialUnit(ImperialUnitName.OSMINA, mutableMapOf(
                    ImperialUnitName.GARNETS to 32.0
            )),
            ImperialUnit(ImperialUnitName.SHKALIK, mutableMapOf(
                    ImperialUnitName.VEDRO to 1.0/200.0
            )),
            ImperialUnit(ImperialUnitName.SHTOF, mutableMapOf(
                    ImperialUnitName.VEDRO to 0.1
            ))

    )
}