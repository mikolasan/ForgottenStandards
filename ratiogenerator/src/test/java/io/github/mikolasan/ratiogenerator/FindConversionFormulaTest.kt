package io.github.mikolasan.ratiogenerator

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import org.junit.Assert.assertEquals
import org.junit.Test

class FindConversionFormulaTest {
    @Test
    fun findConversionFormula_sameUnit() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        assertEquals("x", findConversionFormula(temperatureUnits, celsius, celsius))
    }

    @Test
    fun findConversionFormula_conversionKnown() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
        assertEquals(celsius.formulaMap!![ImperialUnitName.FAHRENHEIT], findConversionFormula(temperatureUnits, fahrenheit, celsius))
    }

    @Test
    fun findConversionFormula_directSearch() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
        val formula = celsius.formulaMap!![ImperialUnitName.FAHRENHEIT]!!
        val inverseFormula = FunctionParser().inverse(formula)
        assertEquals(null, fahrenheit.formulaMap!![ImperialUnitName.CELSIUS])
        assertEquals(inverseFormula, findConversionFormula(temperatureUnits, celsius, fahrenheit))
    }

    @Test
    fun findConversionFormula_oneStepSearch() {
        val temperatureUnits = MinTemperatureUnits.nameMap
        val celsius = temperatureUnits[ImperialUnitName.CELSIUS]!!
        val fahrenheit = temperatureUnits[ImperialUnitName.FAHRENHEIT]!!
        val formula = celsius.formulaMap!![ImperialUnitName.FAHRENHEIT]!!
        val inverseFormula = FunctionParser().inverse(formula)
        assertEquals(null, fahrenheit.formulaMap!![ImperialUnitName.CELSIUS])
        assertEquals(inverseFormula, findConversionFormula(temperatureUnits, celsius, fahrenheit))
    }
}