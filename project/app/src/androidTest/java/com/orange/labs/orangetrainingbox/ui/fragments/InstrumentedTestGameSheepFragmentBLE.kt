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

import androidx.preference.PreferenceManager
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import com.orange.labs.orangetrainingbox.game.SheepGameFencesSpeed
import com.orange.labs.orangetrainingbox.ui.settings.PreferencesKeys
import com.orange.labs.orangetrainingbox.utils.properties.PropertiesKeys
import com.orange.labs.orangetrainingbox.utils.properties.loadProperties
import org.junit.Assert
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To test [GameSheepFragment] class.
 * This class defines the expected resources, e.g. strings and layout (using identifiers).
 * Test cases are mainly factorized in the super class because some games are quite similar.
 *
 * Specific tests cases are also defined in this class.
 *
 * Note simulators used for tests where Pixel 3a XL (API 38) and Nexus 4 (API 28)
 *
 * @since 30/08/2019
 * @version 2.0.0
 * @see [AbstractInstrumentedTestSimpleGameFragmentBLE]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestGameSheepFragmentBLE : AbstractInstrumentedTestSimpleGameFragmentBLE() {

    // Properties from AbstractInstrumentedTestSimpleGameFragmentBLE

    /**
     * The text to find in the tool bar
     */
    override val resourceStringGameTitle: Int
        get() = R.string.title_game_sheep

    /**
     * First line of instruction
     */
    override val resourceStringGameInstructionLine1: Int
        get() = R.string.game_sheep_instructions_line_1

    /**
     * Second line of instruction
     */
    override val resourceStringGameInstructionLine2: Int
        get() = R.string.game_sheep_instructions_line_2

    /**
     * Id of the layout for playing mode
     */
    override val playingModeLayoutId: Int
        get() = R.id.clSheepGamePlaying

    // More tests

    /**
     * For all test cases, except if overridden in such test cases, disabled demo mode
     * and defines difficulty to LOW.
     */
    @Before
    fun setUp(){
        demoModeMustBeEnabled = false
        difficultyFactor = DifficultyFactor.MEDIUM
        super.setup()
    }

    /**
     * Should win once the only fence has been jumped.
     */
    @Test
    fun shouldWinOnceOneFenceOfOneJumped() {

        // Given
        difficultyFactor = DifficultyFactor.LOW
        timeToWaitUntilNextMockFrame = 50
        this.setUpPrerequisites(fencesNumber = 1, fencesSpeed = SheepGameFencesSpeed.LOW)
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-sheep-1-jump.mock")
        waitAMoment()

        // Then
        // TODO Check victory message
        Espresso
            .onView(ViewMatchers.withId(R.id.btnRestart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.btn_restart))))

    }

    /**
     * Should loose when the seep hit the fence
     */
    @Test
    fun shouldFailOnceOneFenceOfOneJumped() {

        // Given
        difficultyFactor = DifficultyFactor.LOW
        timeToWaitUntilNextMockFrame = 50
        this.setUpPrerequisites(fencesNumber = 1, fencesSpeed = SheepGameFencesSpeed.LOW)
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-sheep-1-fail.mock")
        waitAMoment()

        // Then
        // TODO Check victory message
        Espresso
            .onView(ViewMatchers.withId(R.id.btnRestart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.btn_restart))))

    }

    /**
     * Should display remaining fences and update counter
     */
    @Test
    fun shouldUpdateCountersForEachJumpOfFence() {

        // Given
        difficultyFactor = DifficultyFactor.LOW
        timeToWaitUntilNextMockFrame = 50
        this.setUpPrerequisites(fencesNumber = 3, fencesSpeed = SheepGameFencesSpeed.HIGH)
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-sheep-1-jump.mock")
        waitAMoment(5000)

        // Then

        // We suppose message for the 1st fence has been updated after 5 seconds
       var expectedString = appContext.resources.getQuantityString(R.plurals.game_sheep_instructions_line_0_started, 1,1, 2)
        Espresso
            .onView(ViewMatchers.withId(R.id.tvLine0))
            .check(ViewAssertions.matches(ViewMatchers.withText(expectedString)))

        // We suppose message for the 2nd fence has been updated after 8 seconds
        waitAMoment(8000)
        expectedString = appContext.resources.getQuantityString(R.plurals.game_sheep_instructions_line_0_started, 2,2, 1)
        Espresso
            .onView(ViewMatchers.withId(R.id.tvLine0))
            .check(ViewAssertions.matches(ViewMatchers.withText(expectedString)))

    }


    // Inner methods

    /**
     * Defines the prerequisites to ensure the tests are triggered with the expected conditions.
     * Settings are changed by code using [PreferenceManager].
     * Uses the super class [AbstractInstrumentedTestSimpleGameFragmentBLE] method for basic settings,
     * and defines more prerequisites for the game.
     *
     * @param enableDemoMode - True to enable it, false to disable, default set to _self.demoModeMustBeEnabled_
     * @param difficultyFactor - The difficulty factor, default set to _self.difficultyFactor_
     * @param fencesNumber - The number of fences to jump over
     * @param fencesSpeed - The speed of fences'moves
     */
    private fun setUpPrerequisites(enableDemoMode: Boolean = this.demoModeMustBeEnabled,
                                         difficultyFactor: DifficultyFactor = this.difficultyFactor,
                                              fencesNumber: Int, fencesSpeed: SheepGameFencesSpeed) {

        // Check parameters
        val maxNumberOfFences = appContext.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_MAX_FENCES_NUMBER.key).toInt()
        if (fencesNumber < 0 || fencesNumber > maxNumberOfFences) fail("Fences number is $fencesNumber but must be between 0 and $maxNumberOfFences")

        // Defines basic prerequisites
        super.setUpPrerequisites(enableDemoMode, difficultyFactor)

        // Go to settings activity
        // Useful only when finished
        Espresso.onView(ViewMatchers.withId(R.id.action_settings)).perform(ViewActions.click())

        // Update fences configuration
        val preferences = PreferenceManager.getDefaultSharedPreferences(appContext)
        val preferencesEditor = preferences.edit()

        preferencesEditor.putInt(PreferencesKeys.SHEEP_GAME_FENCES_SPEED.key, fencesSpeed.preferencesValue).apply()
        val savedSpeedOfFences = preferences.getInt(PreferencesKeys.SHEEP_GAME_FENCES_SPEED.key, fencesSpeed.preferencesValue * -1) // Trick to ensure to have a failing value if preference not saved
        Assert.assertEquals("Speed fence has not been changed, expected ${fencesSpeed.preferencesValue} but was $savedSpeedOfFences",
            fencesSpeed.preferencesValue,
            savedSpeedOfFences)

        preferencesEditor.putInt(PreferencesKeys.SHEEP_GAME_FENCES_NUMBER.key, fencesNumber).apply()
        val savedNumberOfFences = preferences.getInt(PreferencesKeys.SHEEP_GAME_FENCES_NUMBER.key, fencesNumber * -1) // Trick to ensure to have a failing value if preference not saved
        Assert.assertEquals("Number of fences has not been changed, expected $fencesNumber but was $savedNumberOfFences",
            fencesNumber,
            savedNumberOfFences)

        // Go to back.
        // The finishing of the settings activity triggers the onActivityResult() of the main activity
        // and updates objects like model.
        Espresso.pressBack()

    }

    /**
     * Waits during the given _period_.
     * Allows to wait during mock frames processing before doing assertions.
     *
     * @param duration - The delay to use, default set to 15000 ms
     */
    private fun waitAMoment(duration: Long = 20000){
        Thread.sleep(duration)
    }

}