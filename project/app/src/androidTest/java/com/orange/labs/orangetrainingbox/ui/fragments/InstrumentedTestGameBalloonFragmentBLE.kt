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
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To test [GameBalloonFragment] class.
 * This class defines the expected resources, e.g. strings and layout (using identifiers).
 * Test cases are mainly factorized in the super class because some games are quite similar.
 *
 * @since 30/08/2019
 * @version 2.0.0
 * @see [AbstractInstrumentedTestSimpleGameFragmentBLE]
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestGameBalloonFragmentBLE : AbstractInstrumentedTestSimpleGameFragmentBLE() {

    // Properties from AbstractInstrumentedTestSimpleGameFragmentBLE

    /**
     * The text to find in the tool bar
     */
    override val resourceStringGameTitle: Int
        get() = R.string.title_game_balloon

    /**
     * First line of instruction
     */
    override val resourceStringGameInstructionLine1: Int
        get() = R.string.game_balloon_instructions_line_1

    /**
     * Second line of instruction
     */
    override val resourceStringGameInstructionLine2: Int
        get() = R.string.game_balloon_instructions_line_2

    /**
     * Id of the layout for playing mode
     */
    override val playingModeLayoutId: Int
        get() = R.id.clBalloonGamePlaying

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
     * Uses a bunch of fake signals (mocks) to test the game.
     * The balloon must be small at the end with the good texts.
     */
    @Test
    fun balloonMustBeSmallOnceAllEventsParsed(){

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-balloon-level1-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_1))))

    }

    /**
     * Using a file of mock frames should display the good congratulation message (level 2).
     */
    @Test
    fun shouldDisplayLevel2Congratulations() {

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-balloon-level2-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_2))))

    }

    /**
     * Using a file of mock frames should display the good congratulation message (level 3).
     */
    @Test
    fun shouldDisplayLevel3Congratulations() {

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-balloon-level3-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_3))))

    }

    /**
     * Using a file of mock frames should display the good congratulation message (level max).
     */
    @Test
    fun shouldDisplayLevelMaxCongratulations() {

        // Given
        goToPlayingScreen()

        // When
        runMockBLEFramesFromFile("game-balloon-levelmax-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.btnRestart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.btn_restart))))

        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations_restart))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_max))))

    }

    /**
     * With a low difficulty and small moves, congratulation message should be _game_balloon_congratulations_level_4_
     */
    @Test
    fun shouldDisplayMessageWithLowDifficultyAndSmallMoves() {

        // Given
        setUpPrerequisites(difficultyFactor = DifficultyFactor.LOW)
        goToPlayingScreen()

        // When
        timeToWaitUntilNextMockFrame = 100
        runMockBLEFramesFromFile("game-balloon-smallmoves-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_4))))

    }

    /**
     * With a low difficulty and small moves, congratulation message should be _game_balloon_congratulations_level_4_
     */
    @Test
    fun shouldDisplayMessageWithMediumDifficultyAndSmallMoves() {

        // Given
        setUpPrerequisites(difficultyFactor = DifficultyFactor.MEDIUM)
        goToPlayingScreen()

        // When
        timeToWaitUntilNextMockFrame = 100
        runMockBLEFramesFromFile("game-balloon-smallmoves-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_3))))

    }

    /**
     * With a low difficulty and small moves, congratulation message should be _game_balloon_congratulations_level_2_
     */
    @Test
    fun shouldDisplayMessageWithHighDifficultyAndSmallMoves() {

        // Given
        setUpPrerequisites(difficultyFactor = DifficultyFactor.HIGH)
        goToPlayingScreen()

        // When
        timeToWaitUntilNextMockFrame = 100
        runMockBLEFramesFromFile("game-balloon-smallmoves-signals.mock")

        // Then
        Espresso
            .onView(ViewMatchers.withId(R.id.tv_congratulations))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext.getString(R.string.game_balloon_congratulations_level_2))))

    }

}