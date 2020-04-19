package io.github.mikolasan.imperialrussia

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

@RunWith(AndroidJUnit4::class)
class MeasuringActivityTestSuite {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<MeasuringActivity>(MeasuringActivity::class.java)

    @Test
    fun values_panelsAndListAfterDeviceRotation() {
        // TODO:
        onView(withId(R.id.converting_area))
                .check(isPartiallyLeftOf(withId(R.id.motion_layout)))
    }

    @Test
    fun conversion_zeroOnEmptyInput() {
        val s = ""
        val inch = LengthUnits.imperialUnits[ImperialUnitName.YARD] ?: error("Inch unit is not defined")
        val yard = LengthUnits.imperialUnits[ImperialUnitName.YARD] ?: error("Yard unit is not defined")
        val inchValue = BasicCalculator(s).eval()
        val yardValue = convertValue(inch, yard, inchValue)
        Assert.assertEquals("0", valueForDisplay(yardValue).toString())
    }
}
