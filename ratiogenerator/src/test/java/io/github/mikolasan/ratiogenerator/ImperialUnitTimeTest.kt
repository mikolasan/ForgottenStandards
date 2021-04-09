package io.github.mikolasan.ratiogenerator

import org.junit.Test

class ImperialUnitTimeTest {
    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        addInverseRatios(MinTimeUnits)
        val units = MinTimeUnits.units
        val nameMap = MinTimeUnits.nameMap
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = findConversionRatio(nameMap, fromUnit, toUnit, null)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }
}