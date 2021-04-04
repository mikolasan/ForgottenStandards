package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import io.github.mikolasan.ratiogenerator.MinWeightUnits
import io.github.mikolasan.ratiogenerator.findConversionRatio
import org.junit.Test

class ImperialUnitWeightTest {
    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        val units = MinWeightUnits.units
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = findConversionRatio(MinWeightUnits.nameMap, fromUnit, toUnit, null)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    @Test
    fun findConversionRatio_lotToGran() {
        val nameMap = MinWeightUnits.nameMap
        val fromUnit = nameMap.getValue(ImperialUnitName.LOT)
        val toUnit = nameMap.getValue(ImperialUnitName.GRAN)
        val ratio = findConversionRatio(nameMap, fromUnit, toUnit, null)
        println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
    }
}