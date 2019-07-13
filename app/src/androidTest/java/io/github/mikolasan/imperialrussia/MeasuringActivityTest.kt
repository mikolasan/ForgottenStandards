package io.github.mikolasan.imperialrussia

import androidx.test.InstrumentationRegistry
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class MeasuringActivityTest {

    @Rule @JvmField
    val rule = ActivityTestRule(MeasuringActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("io.github.mikolasan.imperialrussia", appContext.packageName)
    }

    @Test
    fun conversion_zeroOnEmptyInput() {
        val s = ""
        val inches = BasicCalculator(s).eval()
        val arshin = rule.activity.convertToArshin(inches)
        assertEquals("0.0", rule.activity.valueForDisplay(arshin))
    }
}
