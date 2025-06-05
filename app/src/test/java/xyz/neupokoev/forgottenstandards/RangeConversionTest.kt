package xyz.neupokoev.forgottenstandards

import io.github.mikolasan.ratiogenerator.ImperialUnitName
import io.github.mikolasan.ratiogenerator.MinSpeedUnits
import io.github.mikolasan.ratiogenerator.Range
import org.junit.Assert.assertEquals
import org.junit.Test

class RangeConversionTest {
    @Test
    fun beaufortToKnot() {
        val speedUnits = MinSpeedUnits.nameMap
        val beaufort = speedUnits[ImperialUnitName.BEAUFORT]!!
        val knot = speedUnits[ImperialUnitName.KNOT]!!
        val range = convertValueToRange(beaufort, knot, 2.0)
        assertEquals(4.0, range.first, 1e-10)
        assertEquals(6.0, range.second, 1e-10)

//        val meterPerSecond = speedUnits[ImperialUnitName.METER_PER_SECOND]!!
//        val range2 = convertValueToRange(beaufort, meterPerSecond, 2.0)
//        val kmPerHour = speedUnits[ImperialUnitName.KILOMETER_PER_HOUR]!!
//        val range2 = convertValueToRange(beaufort, kmPerHour, 2.0)
        val milePerHour = speedUnits[ImperialUnitName.MILE_PER_HOUR]!!
        val range2 = convertValueToRange(beaufort, milePerHour, 2.0)
        assertEquals(4.0, range2.first, 1e-10)
        assertEquals(6.0, range2.second, 1e-10)
    }

    @Test
    fun findRange() {
        val ranges = mapOf(
            0.0 to Range(Double.NaN, 1.0),
            1.0 to Range(1.0, 3.0),
            2.0 to Range(4.0, 6.0),
            3.0 to Range(7.0, 10.0),
            4.0 to Range(11.0, 16.0),
            5.0 to Range(17.0, 21.0),
            6.0 to Range(22.0, 27.0),
            7.0 to Range(28.0, 33.0),
            8.0 to Range(34.0, 40.0),
            9.0 to Range(41.0, 47.0),
            10.0 to Range(48.0, 55.0),
            11.0 to Range(56.0, 63.0),
            12.0 to Range(64.0, Double.NaN),
        )

        assertEquals(0.0, findKeyForValueInRange(ranges, -20.0), 1e-10)
        assertEquals(0.0, findKeyForValueInRange(ranges, 0.2), 1e-10)
        assertEquals(0.0, findKeyForValueInRange(ranges, 1.0), 1e-10)
        assertEquals(1.0, findKeyForValueInRange(ranges, 3.2), 1e-10)
        assertEquals(2.0, findKeyForValueInRange(ranges, 4.0), 1e-10)
        assertEquals(11.0, findKeyForValueInRange(ranges, 63.0), 1e-10)
        assertEquals(12.0, findKeyForValueInRange(ranges, 63.9), 1e-10)
        assertEquals(12.0, findKeyForValueInRange(ranges, 999.0), 1e-10)
    }

    @Test
    fun knotToBeaufort() {
        val speedUnits = MinSpeedUnits.nameMap
        val beaufort = speedUnits[ImperialUnitName.BEAUFORT]!!
        val knot = speedUnits[ImperialUnitName.KNOT]!!
        var v = 0.0

        v = convertValue(knot, beaufort, 2.0)
        assertEquals(1.0, v, 1e-10)

        v = convertValue(knot, beaufort, 99.0)
        assertEquals(12.0, v, 1e-10)
    }
}