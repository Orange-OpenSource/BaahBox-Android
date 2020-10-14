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

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import java.io.File

/**
 * To test [GameStarFragment] class.
 * This class defines only the expected resources, e.g. strings and layout (using identifiers).
 * Test cases are mainly factorized in the super class because some games are quite similar.
 *
 * @since 30/08/2019
 * @version 2.1.0
 * @see [AbstractInstrumentedTestSimpleGameFragment]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestGameStarFragment : AbstractInstrumentedTestSimpleGameFragment() {

    // Properties

    /**
     * The text to find in the tool bar
     */
    override val resourceStringGameTitle: Int
        get() = R.string.title_game_star

    /**
     * First line of instruction
     */
    override val resourceStringGameInstructionLine1: Int
        get() = R.string.game_star_instructions_line_1

    /**
     * Second line of instruction
     */
    override val resourceStringGameInstructionLine2: Int
        get() = R.string.game_star_instructions_line_2

    /**
     * Id of the layout for playing mode
     */
    override val playingModeLayoutId: Int
        get() = R.id.clStarGamePlaying

    // More tests

    /**
     * Uses a bunch of fake signals (mocks) to test the game.
     * The start must be shining at the end.
     */
    @Test
    fun starMustShineOnceAllEventsParsed(){

        goToPlayingScreen()

        // Read mock events
        readMockEventFromFile()

        // Process events
        activityActivityTestRule.activity.model.sensorB.postValue(500)

        // Final assertion

    }

    // Utils

    /**
     * Go to the playing screen by clicking on the play button
     */
    private fun goToPlayingScreen(){
        Espresso.onView(ViewMatchers.withId(R.id.btnPlay)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(playingModeLayoutId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    /**
     * Reads from a text file the mock events to process
     * @param name The file name, default set to "game-star-win-signals.mock"
     * @return The list of fake interruptions to process
     */
    private fun readMockEventFromFile(name: String = "game-star-win-signals.mock") { //: List<MockInterruption> {
        activityActivityTestRule.activity.applicationContext.assets.open(name).bufferedReader().use {
            Logger.d("----- ${it.readLine()}")
        }

    }

    /**
     * Models the sensors of the Baah Box which can be mapped for example to the [TrainingBoxViewModel].
     */
    enum class Sensor {
        SENSOR_A,
        SENSOR_B
    }

    /**
     * Models a fake interruption with mock values
     *
     * @param sensor The sensor of the Baah Box (A or B)
     * @param interruption The value to broadcast
     */
    data class MockInterruption(val sensor: Sensor, val interruption: Int)
}