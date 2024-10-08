package io.github.mikolasan.ratiogenerator

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
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
    fun allLengthUnitsWithMapUpdate() {
        val units = MinLengthUnits.units
        val map = MinLengthUnits.nameMap
        val start = System.currentTimeMillis()
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue
                val array = findConversionFormulas(map, fromUnit, toUnit)
                val i = array.iterator()
                var first = true
                while (i.hasNext()) {
                    val formulaArray = i.next()
                    if (first) {
                        if (!toUnit.formulaMap.containsKey(fromUnit.unitName)) {
                            toUnit.formulaMap[fromUnit.unitName] = formulaArray
                        }
                        first = false
                    }
                    val ii = formulaArray.iterator()
                    var x = 42.0
                    while (ii.hasNext()) {
                        val root = FunctionParser().parse(ii.next())
                        x = root.eval(x.toString())
                    }
                    assert(x.isFinite())
//                    assertNotEquals(x, 0.0, 1e-6)
                }
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
    fun finiteValuesInConversionFormulas() {
        val map = MinLengthUnits.nameMap
        val foot = map[ImperialUnitName.SAZHEN]!!
        val span = map[ImperialUnitName.ELL]!!
        val array = findConversionFormulas(map, foot, span)
        assert(array.isNotEmpty())

        val i = array.iterator()
        while (i.hasNext()) {
            var x = 42.0
            val ii = i.next().iterator()
            while (ii.hasNext()) {
                val root = FunctionParser().parse(ii.next())
                x = root.eval(x.toString())
            }
            assert(x.isFinite())
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