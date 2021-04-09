package io.github.mikolasan.ratiogenerator

import org.junit.Test

class ImperialUnitAreaTest {
    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        addInverseRatios(MinAreaUnits)
        val units = MinAreaUnits.units
        val nameMap = MinAreaUnits.nameMap
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