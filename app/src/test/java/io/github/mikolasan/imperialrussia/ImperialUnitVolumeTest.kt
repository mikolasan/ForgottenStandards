package io.github.mikolasan.imperialrussia

import io.github.mikolasan.ratiogenerator.MinVolumeUnits
import io.github.mikolasan.ratiogenerator.findConversionRatio
import org.junit.Test

class ImperialUnitVolumeTest {
    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        val units = MinVolumeUnits.units
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = findConversionRatio(MinVolumeUnits.nameMap, fromUnit, toUnit, null)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }
}