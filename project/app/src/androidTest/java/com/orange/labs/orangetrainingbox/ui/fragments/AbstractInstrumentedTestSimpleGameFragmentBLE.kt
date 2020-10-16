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
import androidx.preference.PreferenceManager
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import androidx.test.rule.ActivityTestRule
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.`_`.readMockEventsFromFile
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import com.orange.labs.orangetrainingbox.ui.MainActivity
import com.orange.labs.orangetrainingbox.ui.settings.PreferencesKeys
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Assert
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
 * @see [InstrumentedTestSimpleGameFragment], [InstrumentedTestMockBLEFramesCapable], [InstrumentedTestPrerequisitesCapable]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AbstractInstrumentedTestSimpleGameFragmentBLE : InstrumentedTestSimpleGameFragment,
                                                                InstrumentedTestMockBLEFramesCapable,
                                                                InstrumentedTestPrerequisitesCapable {

    // Internal properties

    /**
     * Activity to play with
     */
    @Rule
    @JvmField var activityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * To get UI thread, [Context]...
     */
    protected lateinit var appContext: Context

    // Properties from InstrumentedTestMockBLEFramesCapable

    /**
     * By default 500ms between each fake BLE frame
     */
    override var timeToWaitUntilNextMockFrame: Long = 500

    // Properties from InstrumentedTestPrerequisitesCapable

    /**
     * By default demo mode must be disabled
     */
    override var demoModeMustBeEnabled: Boolean = false

    /**
     * By default difficulty is set to medium
     */
    override var difficultyFactor: DifficultyFactor = DifficultyFactor.MEDIUM

    // Configuration

    /**
     * Retrieves the [Context].
     * Go to the game screen with layouts defined in subclasses).
     * Set tup prerequisites.
     */
    @Before
    fun setup(){
        appContext = InstrumentationRegistry.getInstrumentation().targetContext
        goToGame()
        setUpPrerequisites()
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
     * To speed up or make slower the frames execution, change value of  _timeToWaitUntilNextMockFrame_
     * before calling this method.
     *
     * @param name The name of the mock file to use
     */
    protected fun runMockBLEFramesFromFile(name: String) {
        val mockEvents = readMockEventsFromFile(name, appContext!!)
        mockEvents.forEach { frame ->
            val (sensorA, sensorB, _) = frame // Joystick management is not yet implemented
            if (sensorA != null) activityTestRule.activity.model.sensorA.postValue(sensorA.payload)
            if (sensorB != null) activityTestRule.activity.model.sensorB.postValue(sensorB.payload)
            Thread.sleep(timeToWaitUntilNextMockFrame)
        }
    }

    /**
     * Defines the prerequisites to ensure the tests are triggered with the expected conditions.
     * Settings are changed by code using [PreferenceManager].
     *
     * @param enableDemoMode - True to enable it, false to disable, default set to _self.demoModeMustBeEnabled_
     * @param difficultyFactor -The difficulty factor, default set to _self.difficultyFactor_
     */
    protected open fun setUpPrerequisites(enableDemoMode: Boolean = this.demoModeMustBeEnabled,
                                          difficultyFactor: DifficultyFactor = this.difficultyFactor) {

        Espresso.onView(ViewMatchers.withId(R.id.action_settings)).perform(ViewActions.click())
        val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        val preferencesEditor = preferences.edit()

        // Deal with the demo mode
        preferencesEditor.putBoolean(PreferencesKeys.ENABLE_DEMO_MODE.key, enableDemoMode).apply()
        val demoModeState = preferences.getBoolean(PreferencesKeys.ENABLE_DEMO_MODE.key, !enableDemoMode) // Trick to ensure to have a failing value if preference not saved
        Assert.assertEquals("Demo mode has not been changed, expected $enableDemoMode but was $demoModeState",
            enableDemoMode,
            demoModeState)

        // Define difficulty
        preferencesEditor.putInt(PreferencesKeys.DIFFICULTY_FACTOR.key, difficultyFactor.preferencesValue).apply()
        val difficultyFactorState = preferences.getInt(PreferencesKeys.DIFFICULTY_FACTOR.key, difficultyFactor.preferencesValue * -1) // Trick to ensure we make the test fail if preferences unsaved
        Assert.assertEquals("Difficulty factor mode has not been changed, expected $difficultyFactor.preferencesValue but was $difficultyFactorState",
            difficultyFactor.preferencesValue,
            difficultyFactorState)

        // Back to playing screen
        Espresso.pressBack()

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
 * Contains a property subclasses should define so as to wait between each mock BLE frame processing
 */
interface InstrumentedTestMockBLEFramesCapable {

    /**
     * The time in milliseconds during each mock frame processing
     */
    var timeToWaitUntilNextMockFrame: Long

}

/**
 * Contains basic properties to override in subclasses for the minimum of prerequisites to define
 * for each game before each test case.
 */
interface InstrumentedTestPrerequisitesCapable {

    /**
     * If the demo mode has to be enabled or not
     */
    var demoModeMustBeEnabled: Boolean

    /**
     * The difficulty factor
     */
    var difficultyFactor: DifficultyFactor

}