package io.github.mikolasan.ratiogenerator

import org.junit.Test

class CompleteConversionTest {

    @Test
    fun arshinToMile() {
        val map = MinLengthUnits.nameMap
        val arshin = map[ImperialUnitName.ARSHIN]!!
        val mile = map[ImperialUnitName.MILE]!!
        val formulaArray = findConversionFormula(map, arshin, mile)
        assert(formulaArray.isNotEmpty())
    }

    @Test
    fun allLengthUnits() {
        val units = MinLengthUnits.units
        val map = MinLengthUnits.nameMap
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val formulaArray = findConversionFormula(map, fromUnit, toUnit)
                assert(formulaArray.isNotEmpty())
                println("[OK]: ${fromUnit.unitName} -> ${formulaArray.contentToString()} = ${toUnit.unitName}")
            }
        }
    }
}