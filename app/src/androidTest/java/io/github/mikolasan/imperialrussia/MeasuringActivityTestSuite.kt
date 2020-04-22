package io.github.mikolasan.imperialrussia

import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.PositionAssertions.isPartiallyLeftOf
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Test
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import org.hamcrest.Matchers.allOf
import org.junit.Assert
import org.junit.Rule
import java.util.*

@RunWith(AndroidJUnit4::class)
class MeasuringActivityTestSuite {

//    @get:Rule
//    var activityScenarioRule = ActivityScenarioRule<MeasuringActivity>(MeasuringActivity::class.java)
//
//    @Test
//    fun values_panelsAndListAfterDeviceRotation() {
//        // TODO:
//        onView(withId(R.id.converting_area))
//                .check(isPartiallyLeftOf(withId(R.id.motion_layout)))
//    }
//
//    @Test
//    fun conversion_zeroOnEmptyInput() {
//        val s = ""
//        val inch = LengthUnits.imperialUnits[ImperialUnitName.YARD] ?: error("Inch unit is not defined")
//        val yard = LengthUnits.imperialUnits[ImperialUnitName.YARD] ?: error("Yard unit is not defined")
//        val inchValue = BasicCalculator(s).eval()
//        val yardValue = convertValue(inch, yard, inchValue)
//        Assert.assertEquals("0", valueForDisplay(yardValue).toString())
//    }

    val enLocale = Locale("en")

    @Test
    fun valueForDisplay_nullValue() {
        Assert.assertEquals(valueForDisplay(null, enLocale).toString(), "-.-")
    }

    @Test
    fun valueForDisplay_zero() {
        Assert.assertEquals(valueForDisplay(0.0, enLocale).toString(), "0")
    }

    @Test
    fun valueForDisplay_negativeZero() {
        Assert.assertEquals(valueForDisplay(-0.0, enLocale).toString(), "-0")
    }

    @Test
    fun valueForDisplay_closeToZero() {
        Assert.assertEquals(valueForDisplay(0.000000001, enLocale).toString(), "0")
    }

    @Test
    fun valueForDisplay_closeToZeroRoundUp() {
        Assert.assertEquals(valueForDisplay(0.000000009, enLocale).toString(), "0.00000001")
    }

    @Test
    fun valueForDisplay_closeToNegativeZero() {
        Assert.assertEquals(valueForDisplay(-0.0000001, enLocale).toString(), "-0.0000001")
    }

    @Test
    fun valueForDisplay_sixDigitsInteger() {
        Assert.assertEquals(valueForDisplay(123456.0, enLocale).toString(), "123,456")
    }

    @Test
    fun valueForDisplay_sevenDigitsInteger() {
        Assert.assertEquals(valueForDisplay(1234567.0, enLocale).toString(), "1.235x106")
    }

    @Test
    fun valueForDisplay_spannable() {
        val spannable = valueForDisplay(1234567.0, enLocale)
        // 1.235x10^6
        val superscriptSpans = spannable.getSpans(8, 9, SuperscriptSpan::class.java)
        val sizeSpans = spannable.getSpans(8, 9, RelativeSizeSpan::class.java)
        Assert.assertEquals(superscriptSpans.size, 1)
        Assert.assertEquals(sizeSpans.size, 1)
    }

    @Test
    fun valueForDisplay_tenDigitsInteger() {
        Assert.assertEquals(valueForDisplay(1234567890.0, enLocale).toString(), "1.235x109")
    }

    @Test
    fun valueForDisplay_negativeNineDigitsInteger() {
        Assert.assertEquals(valueForDisplay(-123456789.0, enLocale).toString(), "-1.235x108")
    }

    @Test
    fun valueForDisplay_eightDigitsReal() {
        Assert.assertEquals(valueForDisplay(1.2345678, enLocale).toString(), "1.2345678")
    }

    @Test
    fun valueForDisplay_nineDigitsReal() {
        Assert.assertEquals(valueForDisplay(1.23456789, enLocale).toString(), "1.2345679")
    }

    @Test
    fun valueForDisplay_negativeSevenDigitsReal() {
        Assert.assertEquals(valueForDisplay(-1.234567, enLocale).toString(), "-1.234567")
    }
}
