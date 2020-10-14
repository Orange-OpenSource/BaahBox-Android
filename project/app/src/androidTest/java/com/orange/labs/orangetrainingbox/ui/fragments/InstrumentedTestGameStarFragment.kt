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
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.`_`.readMockEventsFromFile
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To test [GameStarFragment] class.
 * This class defines the expected resources, e.g. strings and layout (using identifiers).
 * Test cases are mainly factorized in the super class because some games are quite similar.
 * Uses mocks to make more tests on the game logic.
 *
 * We assume we have a sensor reactivity defined to "medium" in prerequisite, no control on this value has been made yet.
 *
 * @since 30/08/2019
 * @version 2.1.0
 * @see [AbstractInstrumentedTestSimpleGameFragment]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestGameStarFragment : AbstractInstrumentedTestSimpleGameFragment() {

    // Properties from AbstractInstrumentedTestSimpleGameFragment

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

    // Other properties

    /**
     * To load mock data
     */
    private lateinit var appContext: Context

    // More tests

    /**
     *
     */
    @Before
    fun setUp() {
        appContext = activityActivityTestRule.activity.applicationContext
    }

    /**
     * Uses a bunch of fake signals (mocks) to test the game.
     * The star must be clear at the end.
     */
    @Test
    fun starMustBeClearOnceAllEventsParsed(){

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-star-level1-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_star_congratulations_level_1))))

    }

    /**
     * Using a file of mock frames should display a congratulation message (level 2)
     */
    @Test
    fun shouldDisplayLevel2Congratulations() {

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-star-level2-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_star_congratulations_level_2))))

    }

    /**
     * Using a file of mock frames should display a congratulation message (level 3)
     */
    @Test
    fun shouldDisplayLevel3Congratulations() {

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-star-level3-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_star_congratulations_level_3))))

    }

    /**
     * Uses a bunch of fake signals (mocks) to test the game.
     * The start must be shining at the end.
     */
    @Test
    fun starMustShineOnceAllEventsParsed(){

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-star-levelmax-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.btnRestart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.btn_restart))))

        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations_restart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_star_congratulations_level_max))))

    }

}