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

import com.orange.labs.orangetrainingbox.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * To test [GameStarFragment] class.
 *
 * @since 29/08/2019
 * @version 1.0.0
 */
class UnitTestGameStarFragment {


    /**
     * The fragment to test
     */
    private var gameStarFragment: GameStarFragment? = null

    /**
     * Init
     */
    @Before
    fun setup(){
        gameStarFragment = GameStarFragment()
    }

    /**
     * Deinit
     */
    @After
    fun teardown(){
        gameStarFragment = null
    }

    /**
     * Test the introductionLayout property
     */
    @Test
    fun introductionLayout() {
        assertEquals(R.layout.fragment_game_star_intro, gameStarFragment?.introductionLayout)
    }

    /**
     * Test the playingLayout property
     */
    @Test
    fun playingLayout() {
        assertEquals(R.layout.fragment_game_star_playing, gameStarFragment?.playingLayout)
    }

    /**
     * Test the restartLayout property
     */
    @Test
    fun restartLayout() {
        assertEquals(R.layout.fragment_game_star_outro, gameStarFragment?.restartLayout)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun screenTitle() {
        assertEquals(R.string.title_game_star, gameStarFragment?.screenTitle)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun themingColor() {
        assertEquals(R.color.purple, gameStarFragment?.themingColor)
    }

    /**
     * Test the actionFromIntroductionToPlaying property
     */
    @Test
    fun actionFromIntroductionToPlaying() {
        assertEquals(R.id.action_gameStarFragment_to_gameStarPlayingFragment, gameStarFragment?.actionFromIntroductionToPlaying)
    }

    /**
     * Test the actionGoToPlaying property
     */
    @Test
    fun actionGoToPlaying() {
        assertEquals(R.id.action_global_gameStarPlayingFragment, gameStarFragment?.actionGoToPlaying)
    }

}