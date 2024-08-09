package xyz.neupokoev.forgottenstandards

import io.github.mikolasan.convertmeifyoucan.FunctionParser
import io.github.mikolasan.ratiogenerator.MinLengthUnits
import io.github.mikolasan.ratiogenerator.findConversionFormula
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class ConvertValueTest {
    @Test
    fun allLengthUnits() {
        val units = MinLengthUnits.units
        val start = System.currentTimeMillis()
        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue
                val ret = convertValue(fromUnit, toUnit, 42.0)
                assertNotEquals(ret, 0.0, 1e-6)
                assert(ret.isFinite())
            }
        }

        for (fromUnit in units) {
            for (toUnit in units) {
                if (fromUnit.unitName == toUnit.unitName) continue
                val ret = convertValue(fromUnit, toUnit, 42.0)
                assert(ret.isFinite())
            }
        }
        val end = System.currentTimeMillis()
        println("allLengthUnits took " + (end - start) + " MilliSeconds")
    }
}