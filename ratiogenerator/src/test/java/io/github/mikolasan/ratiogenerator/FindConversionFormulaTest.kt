package io.github.mikolasan.ratiogenerator

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test

class FindConversionFormulaTest {
    @Test
    fun findConversionFormula_sameUnit() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val formulaArray = findConversionFormula(temperatureUnits, celsius, celsius)
        assertEquals(1, formulaArray.size)
        assertEquals("x", formulaArray[0])
    }

    @Test
    fun findConversionFormula_directKnown() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        // fahrenheit -> celsius
        assertEquals(true, celsius.formulaMap?.contains(fahrenheit.unitName))
        assertArrayEquals(celsius.formulaMap!![fahrenheit.unitName], findConversionFormula(temperatureUnits, fahrenheit, celsius))
    }

    @Test
    fun findConversionFormula_inverseKnown() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
        // celsius -> fahrenheit
        assertEquals(false, fahrenheit.formulaMap?.contains(celsius.unitName))
        assertEquals(true, celsius.formulaMap?.contains(fahrenheit.unitName))
        val formulaArray = celsius.formulaMap!![fahrenheit.unitName]!!
        val inverseFormulaArray = FunctionParser().inverse(formulaArray)
        assertArrayEquals(inverseFormulaArray, findConversionFormula(temperatureUnits, celsius, fahrenheit))
    }

    @Test
    fun findConversionFormula_directSearch() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val rankine = temperatureUnits[ImperialUnitName.RANKINE]!!
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        // rankine -> celsius = rankine -> fahrenheit -> celsius
        assertEquals(false, rankine.formulaMap?.contains(celsius.unitName))
        assertArrayEquals(arrayOf("x + 459.67", "x * 9 / 5 + 32"), findConversionFormula(temperatureUnits, rankine, celsius))
    }

    @Test
    fun findConversionFormula_reverseSearch() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val rankine = temperatureUnits[ImperialUnitName.RANKINE]!!
        // celsius -> rankine = celsius -> fahrenheit -> rankine
        assertEquals(false, celsius.formulaMap?.contains(rankine.unitName))
        assertArrayEquals(arrayOf("(x - 32) * 5 / 9", "x - 459.67"), findConversionFormula(temperatureUnits, celsius, rankine))
    }

//    @Test
//    fun findConversionFormula_oneStepSearch() {
//        val temperatureUnits = MinTemperatureUnits.nameMap
//        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
//        assertEquals(false, fahrenheit.formulaMap?.contains(ImperialUnitName.KELVIN))
//        val kelvin = temperatureUnits[ImperialUnitName.KELVIN]!!
//        assertArrayEquals(arrayOf(""), findConversionFormula(temperatureUnits, fahrenheit, kelvin))
//    }
}