package io.github.mikolasan.ratiogenerator

object MinVolumeUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.GARNETS, mutableMapOf(
                    ImperialUnitName.VEDRO to 0.25
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.VEDRO, mutableMapOf(
                    ImperialUnitName.GARNETS to 4.0
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.OSMINA, mutableMapOf(
                    ImperialUnitName.GARNETS to 32.0
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.SHKALIK, mutableMapOf(
                    ImperialUnitName.VEDRO to 1.0/200.0
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.SHTOF, mutableMapOf(
                    ImperialUnitName.VEDRO to 0.1
            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}