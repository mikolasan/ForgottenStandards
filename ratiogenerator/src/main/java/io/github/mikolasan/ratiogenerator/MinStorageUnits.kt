package io.github.mikolasan.ratiogenerator

object MinStorageUnits : ImperialUnits() {
    override val units = arrayOf(
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BIT, mutableMapOf(
                    ImperialUnitName.BYTE to 8.0
            )),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BYTE, mutableMapOf(
                    ImperialUnitName.BLOCK to 512.0,
                    ImperialUnitName.KIBIBYTE to 1024.0
            )),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.KIBIBYTE, mutableMapOf(

            )),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.PACKET, mutableMapOf(
                    ImperialUnitName.BLOCK to 4.0
            )),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BLOCK, mutableMapOf(

            ))

    )

    override val nameMap = makeUnitByNameMap(units)
}