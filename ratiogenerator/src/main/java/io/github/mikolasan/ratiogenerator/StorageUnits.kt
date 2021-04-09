package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import io.github.mikolasan.ratiogenerator.ImperialUnits
import kotlin.Array
import kotlin.collections.Map

object StorageUnits : ImperialUnits() {
    override val units: Array<ImperialUnit> = arrayOf(
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BIT, mapOf(
                    ImperialUnitName.BIT to 1.0,
                    ImperialUnitName.BYTE to 8.0,
                    ImperialUnitName.KIBIBYTE to 8192.0,
                    ImperialUnitName.PACKET to 1024.0,
                    ImperialUnitName.BLOCK to 4096.0)),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BYTE, mapOf(
                    ImperialUnitName.BIT to 0.125,
                    ImperialUnitName.BYTE to 1.0,
                    ImperialUnitName.KIBIBYTE to 1024.0,
                    ImperialUnitName.PACKET to 128.0,
                    ImperialUnitName.BLOCK to 512.0)),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.KIBIBYTE, mapOf(
                    ImperialUnitName.BIT to 1.220703125E-4,
                    ImperialUnitName.BYTE to 9.765625E-4,
                    ImperialUnitName.KIBIBYTE to 1.0,
                    ImperialUnitName.PACKET to 0.125,
                    ImperialUnitName.BLOCK to 0.5)),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.PACKET, mapOf(
                    ImperialUnitName.BIT to 9.765625E-4,
                    ImperialUnitName.BYTE to 0.0078125,
                    ImperialUnitName.KIBIBYTE to 8.0,
                    ImperialUnitName.PACKET to 1.0,
                    ImperialUnitName.BLOCK to 4.0)),
            ImperialUnit(ImperialUnitType.STORAGE, ImperialUnitName.BLOCK, mapOf(
                    ImperialUnitName.BIT to 2.44140625E-4,
                    ImperialUnitName.BYTE to 0.001953125,
                    ImperialUnitName.KIBIBYTE to 2.0,
                    ImperialUnitName.PACKET to 0.25,
                    ImperialUnitName.BLOCK to 1.0))
            )

    override val nameMap: Map<ImperialUnitName, ImperialUnit> = makeUnitByNameMap(units)
}
