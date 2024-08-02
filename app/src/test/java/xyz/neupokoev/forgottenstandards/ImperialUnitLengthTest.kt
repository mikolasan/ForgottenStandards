package xyz.neupokoev.forgottenstandards

import io.github.mikolasan.ratiogenerator.ImperialUnit
import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.ImperialUnitType
import org.junit.Assert.*
import org.junit.Test


class ImperialUnitLengthTest {
    val defaultValue = ImperialUnit(ImperialUnitType.LENGTH, ImperialUnitName.LENGTH_ZERO_NO_UNIT, mutableMapOf())
    val imperialUnits = LengthUnits.nameMap
    val arshin = imperialUnits.getOrDefault(ImperialUnitName.ARSHIN, defaultValue)
    val point = imperialUnits.getOrDefault(ImperialUnitName.POINT, defaultValue)
    val line = imperialUnits.getOrDefault(ImperialUnitName.LINE, defaultValue)
    val inch = imperialUnits.getOrDefault(ImperialUnitName.INCH, defaultValue)
    val vershok = imperialUnits.getOrDefault(ImperialUnitName.VERSHOK, defaultValue)
    val palm = imperialUnits.getOrDefault(ImperialUnitName.PALM, defaultValue)
    val span = imperialUnits.getOrDefault(ImperialUnitName.SPAN, defaultValue)
    val foot = imperialUnits.getOrDefault(ImperialUnitName.FOOT, defaultValue)
    val sazhen = imperialUnits.getOrDefault(ImperialUnitName.SAZHEN, defaultValue)
    val verst = imperialUnits.getOrDefault(ImperialUnitName.VERST, defaultValue)
    val mile = imperialUnits.getOrDefault(ImperialUnitName.MILE, defaultValue)
    val ell = imperialUnits.getOrDefault(ImperialUnitName.ELL, defaultValue)
    val step = imperialUnits.getOrDefault(ImperialUnitName.STEP, defaultValue)
    val centimeter = imperialUnits.getOrDefault(ImperialUnitName.CENTIMETER, defaultValue)

    @Test
    fun getConversionRatio_arshinToCentimeter() {
        val ratio = getConversionRatio(arshin, centimeter)
        assertEquals(71.12, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_ellToCentimeter() {
        val ratio = getConversionRatio(ell, centimeter)
        assertEquals(47.41333, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_sazhenToCentimeter() {
        val ratio = getConversionRatio(sazhen, centimeter)
        assertEquals(213.36, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_sazhenToFoot() {
        val ratio = getConversionRatio(sazhen, foot)
        assertEquals(7.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_palmToCentimeter() {
        val ratio = getConversionRatio(palm, centimeter)
        assertEquals(7.902222222222222, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_footToCentimeter() {
        val ratio = getConversionRatio(foot, centimeter)
        assertEquals(30.48, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_footToSazhen() {
        val ratio = getConversionRatio(foot, sazhen)
        assertEquals(1.0/7.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_spanToCentimeter() {
        val ratio = getConversionRatio(span, centimeter)
        assertEquals(17.78, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_stepToCentimeter() {
        val ratio = getConversionRatio(step, centimeter)
        assertEquals(71.12, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToPoint() {
        // 1 arshin = 2800 points
        val ratio = getConversionRatio(arshin, point)
        assertEquals(2800.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToLine() {
        val ratio = getConversionRatio(arshin, line)
        assertEquals(280.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToInch() {
        val ratio = getConversionRatio(arshin, inch)
        assertEquals(28.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToVershok() {
        val ratio = getConversionRatio(arshin, vershok)
        assertEquals(16.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToPalm() {
        val ratio = getConversionRatio(arshin, palm)
        assertEquals(9.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToSpan() {
        val ratio = getConversionRatio(arshin, span)
        assertEquals(4.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToFoot() {
        val ratio = getConversionRatio(arshin, foot)
        assertEquals(7.0/3.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToArshin() {
        val ratio = getConversionRatio(arshin, arshin)
        assertEquals(1.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToSazhen() {
        val ratio = getConversionRatio(arshin, sazhen)
        assertEquals(1.0/3.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToVerst() {
        val ratio = getConversionRatio(arshin, verst)
        assertEquals(1.0/1500.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_arshinToMile() {
        val ratio = getConversionRatio(arshin, mile)
        assertEquals(1.0/10500.0, ratio, 1e-10)
    }

    @Test
    fun getConversionRatio_checkRatioForImperialUnits() {
        val units = arrayOf(point, line, inch, vershok, palm, span, foot, arshin, sazhen, verst, mile)
        for (fromUnit in units) {
            for (toUnit in units) {
                println("Find ratio for ${fromUnit.unitName} -> ${toUnit.unitName}")
                val ratio = getConversionRatio(fromUnit, toUnit)
                println("${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    @Test
    fun getConversionRatio_checkRatioForAllUnits() {
        val units = LengthUnits.units
        for (fromUnit in units) {
            for (toUnit in units) {
                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = getConversionRatio(fromUnit, toUnit)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    // inverse
    @Test
    fun getConversionRatio_inchToArshin() {
        val ratio = getConversionRatio(inch, arshin)
        assertEquals(0.03571428571428571428571428571429, ratio, 1e-10)
    }


}