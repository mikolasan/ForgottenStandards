package io.github.mikolasan.imperialrussia

import io.github.mikolasan.imperialrussia.LengthUnits.findConversionRatio
import io.github.mikolasan.imperialrussia.LengthUnits.imperialUnits
import io.github.mikolasan.imperialrussia.LengthUnits.lengthUnits
import org.junit.Test
import org.junit.Assert.*


class ImperialUnitTest {
    val defaultValue = ImperialUnit(0, ImperialUnitName.LENGTH_ZERO_NO_UNIT, mutableMapOf())
    val arshin = imperialUnits.getOrDefault(ImperialUnitName.YARD, defaultValue)
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

    @Test
    fun findConversionRatio_arshinToPoint() {
        val ratio = findConversionRatio(arshin, point)
        assertEquals(2800.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToLine() {
        val ratio = findConversionRatio(arshin, line)
        assertEquals(280.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToInch() {
        val ratio = findConversionRatio(arshin, inch)
        assertEquals(28.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToTip() {
        val ratio = findConversionRatio(arshin, tip)
        assertEquals(16.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToPalm() {
        val ratio = findConversionRatio(arshin, palm)
        assertEquals(9.53191489361702, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToQuarter() {
        val ratio = findConversionRatio(arshin, quarter)
        assertEquals(4.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToFoot() {
        val ratio = findConversionRatio(arshin, foot)
        assertEquals(7.0/3.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToArshin() {
        val ratio = findConversionRatio(arshin, arshin)
        assertEquals(1.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToFathom() {
        val ratio = findConversionRatio(arshin, fathom)
        assertEquals(1.0/3.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToTurn() {
        val ratio = findConversionRatio(arshin, turn)
        assertEquals(1.0/1500.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_arshinToMile() {
        val ratio = findConversionRatio(arshin, mile)
        assertEquals(1.0/10500.0, ratio, 1e-10)
    }

    @Test
    fun findConversionRatio_checkRatioForImperialUnits() {
        val units = arrayOf(point, line, inch, tip, palm, quarter, foot, arshin, fathom, turn, mile)
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
    fun findConversionRatio_inchToArshin() {
        val ratio = findConversionRatio(inch, arshin)
        assertEquals(0.03571428571428571428571428571429, ratio, 1e-10)
    }

    @Test
    fun conversion_zeroOnEmptyInput() {
        val s = ""
        val inch = imperialUnits[ImperialUnitName.YARD] ?: error("Inch unit is not defined")
        val arshin = imperialUnits[ImperialUnitName.YARD] ?: error("Yard unit is not defined")
        val inchValue = BasicCalculator(s).eval()
        val arshinValue = LengthUnits.convertValue(inch, arshin, inchValue)
        assertEquals("0.0", valueForDisplay(arshinValue))
    }
}