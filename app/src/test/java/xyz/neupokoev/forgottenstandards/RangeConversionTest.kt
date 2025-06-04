package io.github.mikolasan.ratiogenerator

import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Test
import xyz.neupokoev.forgottenstandards.convertValueToRange

class RangeConversionTest {
    @Test
    fun beaufortToKnot() {
        val speedUnits = MinSpeedUnits.nameMap
        val beaufort = speedUnits[ImperialUnitName.BEAUFORT]!!
        val knot = speedUnits[ImperialUnitName.KNOT]!!
        val range = convertValueToRange(beaufort, knot, 2.0)
        assertEquals(4.0, range.first, 1e-10)
        assertEquals(6.0, range.second, 1e-10)
    }

    @Test
    fun knotToBeaufort() {
        val speedUnits = MinSpeedUnits.nameMap
        val beaufort = speedUnits[ImperialUnitName.BEAUFORT]!!
        val knot = speedUnits[ImperialUnitName.KNOT]!!
        val v = convertValue(speedUnits, knot, beaufort,2.0)
        assertEquals(5.0, v, 1e-10)
    }
}