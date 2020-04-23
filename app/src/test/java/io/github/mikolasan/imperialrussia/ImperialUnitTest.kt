package io.github.mikolasan.imperialrussia

import io.github.mikolasan.imperialrussia.LengthUnits.imperialUnits
import io.github.mikolasan.imperialrussia.LengthUnits.lengthUnits
import org.junit.Test
import org.junit.Assert.*


class ImperialUnitTest {
    val defaultValue = ImperialUnit(0, ImperialUnitName.LENGTH_ZERO_NO_UNIT, mutableMapOf())
    val yard = imperialUnits.getOrDefault(ImperialUnitName.YARD, defaultValue)
    val point = imperialUnits.getOrDefault(ImperialUnitName.POINT, defaultValue)
    val line = imperialUnits.getOrDefault(ImperialUnitName.LINE, defaultValue)
    val inch = imperialUnits.getOrDefault(ImperialUnitName.INCH, defaultValue)
    val tip = imperialUnits.getOrDefault(ImperialUnitName.TIP, defaultValue)
    val palm = imperialUnits.getOrDefault(ImperialUnitName.PALM, defaultValue)
    val quarter = imperialUnits.getOrDefault(ImperialUnitName.QUARTER, defaultValue)
    val foot = imperialUnits.getOrDefault(ImperialUnitName.FOOT, defaultValue)
    val fathom = imperialUnits.getOrDefault(ImperialUnitName.FATHOM, defaultValue)
    val turn = imperialUnits.getOrDefault(ImperialUnitName.TURN, defaultValue)
    val mile = imperialUnits.getOrDefault(ImperialUnitName.MILE, defaultValue)
    val elbow = imperialUnits.getOrDefault(ImperialUnitName.ELBOW, defaultValue)
    val stride = imperialUnits.getOrDefault(ImperialUnitName.STRIDE, defaultValue)
    val centimeter = imperialUnits.getOrDefault(ImperialUnitName.CENTIMETER, defaultValue)

    @Test
    fun findConversionRatio_yardToCentimeter() {
        val ratio = findConversionRatio(yard, centimeter)
        assertEquals(71.12, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_elbowToCentimeter() {
        val ratio = findConversionRatio(elbow, centimeter)
        assertEquals(47.41333, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_fathomToCentimeter() {
        val ratio = findConversionRatio(fathom, centimeter)
        assertEquals(213.36, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_fathomToFoot() {
        val ratio = findConversionRatio(fathom, foot)
        assertEquals(7.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_palmToCentimeter() {
        val ratio = findConversionRatio(palm, centimeter)
        assertEquals(7.902222222222222, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_footToCentimeter() {
        val ratio = findConversionRatio(foot, centimeter)
        assertEquals(30.48, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_footToFathom() {
        val ratio = findConversionRatio(foot, fathom)
        assertEquals(1.0/7.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_quarterToCentimeter() {
        val ratio = findConversionRatio(quarter, centimeter)
        assertEquals(17.78, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_strideToCentimeter() {
        val ratio = findConversionRatio(stride, centimeter)
        assertEquals(71.12, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToPoint() {
        // 1 yard = 2800 points
        val ratio = findConversionRatio(yard, point)
        assertEquals(2800.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToLine() {
        val ratio = findConversionRatio(yard, line)
        assertEquals(280.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToInch() {
        val ratio = findConversionRatio(yard, inch)
        assertEquals(28.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToTip() {
        val ratio = findConversionRatio(yard, tip)
        assertEquals(16.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToPalm() {
        val ratio = findConversionRatio(yard, palm)
        assertEquals(9.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToQuarter() {
        val ratio = findConversionRatio(yard, quarter)
        assertEquals(4.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToFoot() {
        val ratio = findConversionRatio(yard, foot)
        assertEquals(7.0/3.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToYard() {
        val ratio = findConversionRatio(yard, yard)
        assertEquals(1.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToFathom() {
        val ratio = findConversionRatio(yard, fathom)
        assertEquals(1.0/3.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToTurn() {
        val ratio = findConversionRatio(yard, turn)
        assertEquals(1.0/1500.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_yardToMile() {
        val ratio = findConversionRatio(yard, mile)
        assertEquals(1.0/10500.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_checkRatioForImperialUnits() {
        val units = arrayOf(point, line, inch, tip, palm, quarter, foot, yard, fathom, turn, mile)
        for (fromUnit in units) {
            for (toUnit in units) {
                println("Find ratio for ${fromUnit.unitName} -> ${toUnit.unitName}")
                val ratio = findConversionRatio(fromUnit, toUnit)
                println("${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    @Test
    fun findConversionRatio_checkRatioForAllUnits() {
        val units = lengthUnits
        for (fromUnit in units) {
            for (toUnit in units) {
                println("Finding ratio for ${fromUnit.unitName} -> ${toUnit.unitName}...")
                val ratio = findConversionRatio(fromUnit, toUnit)
                println("[OK]: ${fromUnit.unitName} -> $ratio ${toUnit.unitName}")
            }
        }
    }

    // inverse
    @Test
    fun findConversionRatio_inchToYard() {
        val ratio = findConversionRatio(inch, yard)
        assertEquals(0.03571428571428571428571428571429, ratio, 1e-10)
    }


}