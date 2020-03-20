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
 * To test [GameSheepFragment] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 29/08/2019
 * @version 1.0.0
 */
class UnitTestGameSheepFragment {


    /**
     * The fragment to test
     */
    private var gameSheepFragment: GameSheepFragment? = null

    /**
     * Init
     */
    @Before
    fun setup(){
        gameSheepFragment = GameSheepFragment()
    }

    /**
     * Deinit
     */
    @After
    fun teardown(){
        gameSheepFragment = null
    }

    /**
     * Test the introductionLayout property
     */
    @Test
    fun introductionLayout() {
        assertEquals(R.layout.fragment_game_sheep_intro, gameSheepFragment?.introductionLayout)
    }

    /**
     * Test the playingLayout property
     */
    @Test
    fun playingLayout() {
        assertEquals(R.layout.fragment_game_sheep_playing, gameSheepFragment?.playingLayout)
    }

    /**
     * Test the restartLayout property
     */
    @Test
    fun restartLayout() {
        assertEquals(R.layout.fragment_game_sheep_outro, gameSheepFragment?.restartLayout)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun screenTitle() {
        assertEquals(R.string.title_game_sheep, gameSheepFragment?.screenTitle)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun themingColor() {
        assertEquals(R.color.pink, gameSheepFragment?.themingColor)
    }

    /**
     * Test the actionFromIntroductionToPlaying property
     */
    @Test
    fun actionFromIntroductionToPlaying() {
        assertEquals(R.id.action_gameSheepFragment_to_gameSheepPlayingFragment, gameSheepFragment?.actionFromIntroductionToPlaying)
    }

    /**
     * Test the actionGoToPlaying property
     */
    @Test
    fun actionGoToPlaying() {
        assertEquals(R.id.action_global_gameSheepPlayingFragment, gameSheepFragment?.actionGoToPlaying)
    }

}