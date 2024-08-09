package io.github.mikolasan.ratiogenerator

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import org.junit.Assert.assertEquals
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
        val start = System.currentTimeMillis()
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                //println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val formulaArray = findConversionFormula(map, fromUnit, toUnit)
                assert(formulaArray.isNotEmpty())
                //println("[OK]: ${fromUnit.unitName} -> ${formulaArray.contentToString()} = ${toUnit.unitName}")
            }
        }
        val end = System.currentTimeMillis()
        println("allLengthUnits took " + (end - start) + " MilliSeconds")
    }

    @Test
    fun allTemperatureUnits() {
        val units = MinTemperatureUnits.units
        val map = MinTemperatureUnits.nameMap
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

    @Test
    fun completeConversion() {
        val units = MinForceUnits.units
        val map = MinForceUnits.nameMap
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue

                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val formulaArray = findConversionFormula(map, fromUnit, toUnit)
                assert(formulaArray.isNotEmpty())
                println("[OK]: ${fromUnit.unitName} -> ${formulaArray.contentToString()} = ${toUnit.unitName}")
                val inputValue = 42.0
                val it = formulaArray.iterator()
                var x = inputValue
                while (it.hasNext()) {
                    val root = FunctionParser().parse(it.next())
                    println("$x, ${x.toString()}")
                    val valueAsString: String = x.toString()
                    x = root.eval(valueAsString)
                }
                val outputValue = x
                println("[OK]: $inputValue ${fromUnit.unitName} = $outputValue ${toUnit.unitName}")
                val inverseFormulaArray = findConversionFormula(map, toUnit, fromUnit)
                assert(inverseFormulaArray.isNotEmpty())
                println("[OK]: ${toUnit.unitName} -> ${inverseFormulaArray.contentToString()} = ${fromUnit.unitName}")
                val it2 = inverseFormulaArray.iterator()
                var y = outputValue
                while (it2.hasNext()) {
                    val root = FunctionParser().parse(it2.next())
                    y = root.eval(y.toString())
                }
                val inverseInputValue = y
                println("[OK]: $outputValue ${fromUnit.unitName} = $inverseInputValue ${toUnit.unitName}")
                assertEquals(inputValue, inverseInputValue, 1e-10)
            }
        }
    }
}