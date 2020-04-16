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
import org.junit.Rule

@RunWith(AndroidJUnit4::class)
class MeasuringActivityTestSuite {

    @get:Rule
    var activityScenarioRule = ActivityScenarioRule<MeasuringActivity>(MeasuringActivity::class.java)

    @Test
    fun values_panelsAndListAfterDeviceRotation() {
//        val scenario = activityScenarioRule.scenario
//        scenario.onActivity { activity ->
//            //activity.topPanel.setString("1.0")
//            activity.topPanel.changeUnit(LengthUnits.lengthUnits[0]) // kilometer
//            activity.topPanel.setValue(1.0)
//        }
//        val view = allOf(withId(R.id.panel_input), withText("1.0"))
//
//        scenario.moveToState(Lifecycle.State.RESUMED)

        onView(withId(R.id.incBase))
                .check(isPartiallyLeftOf(withId(R.id.motion_layout)))




    }

}
