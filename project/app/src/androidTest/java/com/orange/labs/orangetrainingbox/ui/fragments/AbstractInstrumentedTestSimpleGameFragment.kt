/**
    Orange Baah Box
    Copyright (C) 2017 – 2020 Orange SA

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.orange.labs.orangetrainingbox.ui.fragments

import android.content.Context
import android.widget.TextView
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.`_`.readMockEventsFromFile
import com.orange.labs.orangetrainingbox.ui.MainActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To test simple game fragments like [GameStarFragment], [GameBalloonFragment]
 * or [GameSheepFragment] classes.
 * Allows to factorize instrumented tests for very similar games.
 *
 * @since 30/08/2019
 * @version 2.0.0
 * @see [InstrumentedTestSimpleGameFragment], [InstrumentedTestMockFramesCapable]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AbstractInstrumentedTestSimpleGameFragment : InstrumentedTestSimpleGameFragment, InstrumentedTestMockFramesCapable {

    // Internal properties

    /**
     * Activity to play with
     */
    @Rule
    @JvmField var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * To get UI thread
     */
    private var appContext: Context? = null

    // Properties from InstrumentedTestMockFramesCapable

    /**
     * The time in milliseconds during each mock frame processing
     */
    override val timeToWaitUntilNextMockFrame: Long
        get() = 500

    // Configuration

    /**
     *
     */
    @Before
    fun setup(){
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        goToGame()
    }

    // Tests

    /**
     * Test the layout elements.
     * Will look for the toolbar and its text, and also for the instructions lines in 2 fields.
     * Will check if the play button exists.
     */
    @Test
    fun layoutTextContents(){

        // Test the app bar

        Espresso.onView(
            CoreMatchers.allOf(
                IsInstanceOf.instanceOf(TextView::class.java),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(resourceStringGameTitle))))

        // Test the text contents

        Espresso.onView(ViewMatchers.withId(R.id.tvLine1))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(resourceStringGameInstructionLine1))))

        Espresso.onView(ViewMatchers.withId(R.id.tvLine2))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(resourceStringGameInstructionLine2))))

        // Test the button

        Espresso.onView(ViewMatchers.withId(R.id.btnPlay))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(R.string.btn_start))))

    }

    /**
     * Test the action on the play button: should go from intro screen to playing screen.
     */
    @Test
    fun playButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnPlay)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(playingModeLayoutId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    // Inner part

    /**
     * Goes to star game screen
     */
    private fun goToGame(){
        Espresso.onView(ViewMatchers.withText(appContext!!.getString(resourceStringGameTitle)))
            .perform(ViewActions.click())
    }

    /**
     * Go to the playing screen by clicking on the play button
     */
    protected fun goToPlayingScreen(){
        Espresso.onView(ViewMatchers.withId(R.id.btnPlay)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(playingModeLayoutId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Using the file with the given name, stored in assets folder, load the mock frames and interruptions and
     * run each of them.
     * Uses also the _timeToWaitUntilNextMockFrame_ interval between each bunch of frames process.
     *
     * @param name The name of the mock file to use
     */
    protected fun runMockBLEFramesFromFile(name: String) {
        val mockEvents = readMockEventsFromFile(name, appContext!!)
        mockEvents.forEach { frame ->
            val (sensorA, sensorB, _) = frame // Joystick management is not yet implemented
            if (sensorA != null) activityActivityTestRule.activity.model.sensorA.postValue(sensorA.payload)
            if (sensorB != null) activityActivityTestRule.activity.model.sensorB.postValue(sensorB.payload)
            Thread.sleep(timeToWaitUntilNextMockFrame)
        }
    }

}

/**
 * Contains some properties which must be defined by classes dedicated to instrumented tests.
 */
interface InstrumentedTestSimpleGameFragment {

    /**
     * The title of the game, in R.string
     */
    val resourceStringGameTitle: Int

    /**
     * The first line for instructions
     */
    val resourceStringGameInstructionLine1: Int

    /**
     * The second line for instructions
     */
    val resourceStringGameInstructionLine2: Int

    /**
     * The id of the playing mode layout
     */
    val playingModeLayoutId: Int

}

/**
 * Contains a property subclasses should define so as to wait between each mock frame processing
 */
interface InstrumentedTestMockFramesCapable {

    /**
     * The time in milliseconds during each mock frame processing
     */
    val timeToWaitUntilNextMockFrame: Long

}