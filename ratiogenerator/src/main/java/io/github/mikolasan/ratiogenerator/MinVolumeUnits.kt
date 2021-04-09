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
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.CHARKA, mutableMapOf(
                    ImperialUnitName.SHKALIK to 0.5
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.SOROKOVKA, mutableMapOf(
                    ImperialUnitName.SHTOF to 4.0
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.LITER, mutableMapOf(
                    ImperialUnitName.QUART to 1.13652,
                    ImperialUnitName.SHTOF to 1.23
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.MILLILITER, mutableMapOf(
                    ImperialUnitName.PINT to 568.26125,
                    ImperialUnitName.SHKALIK to 61.5,
                    ImperialUnitName.CHARKA to 123.0
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.GALLON, mutableMapOf(
                    ImperialUnitName.PINT to 8.0,
                    ImperialUnitName.VEDRO to 2.71
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.FLOZ, mutableMapOf(
                    ImperialUnitName.SHKALIK to 2.16,
                    ImperialUnitName.CHARKA to 4.33
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.PINT, mutableMapOf(
                    ImperialUnitName.SHTOF to 2.16
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.QUART, mutableMapOf(
                    ImperialUnitName.PINT to 2.0,
                    ImperialUnitName.GARNETS to 3.466
            )),
            ImperialUnit(ImperialUnitType.VOLUME, ImperialUnitName.BOCHKA, mutableMapOf(
                    ImperialUnitName.VEDRO to 1.0/40.0
            ))
    )

    override val nameMap = makeUnitByNameMap(units)
}