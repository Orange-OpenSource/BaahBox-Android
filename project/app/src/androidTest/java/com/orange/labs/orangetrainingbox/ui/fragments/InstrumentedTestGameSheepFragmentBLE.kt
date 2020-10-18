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
        Thread.sleep(20000)

        // Then


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
        Assert.assertEquals("Speed fence has not been changed, expected $fencesSpeed but was $savedSpeedOfFences",
            fencesSpeed,
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

}