package io.github.mikolasan.imperialrussia

import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore


//
//    POINT,
//    LINE,
//    INCH,
//    TIP, // vershok
//    PALM, // ladon
//    QUARTER, // pyad
//    FOOT,
//    YARD, // arshin
//    FATHOM, // sazhen
//    TURN, // versta
//    MILE,
//


class ImperialUnitTest {

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
    fun findConversionRatio_inchToArshin() {
        val ratio = findConversionRatio(inch, arshin)
        assertEquals(0.03571428571428571428571428571429, ratio, 1e-10)
    }

}