package io.github.mikolasan.ratiogenerator

import org.junit.Test

class ImperialUnitAngleTest {
    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        val units = MinAngleUnits.units
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = findConversionRatio(MinAngleUnits.nameMap, fromUnit, toUnit, null)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    @Test
    fun findConversionRatio_turnToTurn_emptyMap() {
        val nameMap = MinAngleUnits.nameMap
        val fromUnit = nameMap.getValue(ImperialUnitName.TURN)
        val toUnit = nameMap.getValue(ImperialUnitName.TURN)
        val ratio = findConversionRatio(nameMap, fromUnit, toUnit, null)
        println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
    }
}