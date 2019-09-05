/**
    Orange Baah Box
    Copyright (C) 2017 – 2019 Orange SA

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

import com.orange.labs.orangetrainingbox.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * To test [GameBalloonFragment] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 29/08/2019
 * @version 1.0.0
 */
class UnitTestGameBalloonFragment {


    /**
     * The fragment to test
     */
    private var gameBalloonFragment: GameBalloonFragment? = null

    /**
     * Init
     */
    @Before
    fun setup(){
        gameBalloonFragment = GameBalloonFragment()
    }

    /**
     * Deinit
     */
    @After
    fun tearDown(){
        gameBalloonFragment = null
    }

    /**
     * Test the introductionLayout property
     */
    @Test
    fun introductionLayout() {
        assertEquals(R.layout.fragment_game_balloon_intro, gameBalloonFragment?.introductionLayout)
    }

    /**
     * Test the playingLayout property
     */
    @Test
    fun playingLayout() {
        assertEquals(R.layout.fragment_game_balloon_playing, gameBalloonFragment?.playingLayout)
    }

    /**
     * Test the restartLayout property
     */
    @Test
    fun restartLayout() {
        assertEquals(R.layout.fragment_game_balloon_outro, gameBalloonFragment?.restartLayout)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun screenTitle() {
        assertEquals(R.string.title_game_balloon, gameBalloonFragment?.screenTitle)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun themingColor() {
        assertEquals(R.color.orange, gameBalloonFragment?.themingColor)
    }

    /**
     * Test the actionFromIntroductionToPlaying property
     */
    @Test
    fun actionFromIntroductionToPlaying() {
        assertEquals(R.id.action_gameBalloonFragment_to_gameBalloonPlayingFragment, gameBalloonFragment?.actionFromIntroductionToPlaying)
    }

    /**
     * Test the actionGoToPlaying property
     */
    @Test
    fun actionGoToPlaying() {
        assertEquals(R.id.action_global_gameBalloonPlayingFragment, gameBalloonFragment?.actionGoToPlaying)
    }

}