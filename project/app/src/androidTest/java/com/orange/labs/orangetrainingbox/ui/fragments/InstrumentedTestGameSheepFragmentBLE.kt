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

import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import org.junit.Before
import org.junit.runner.RunWith

/**
 * To test [GameSheepFragment] class.
 * This class defines only the expected resources, e.g. strings and layout (using identifiers).
 * Test cases are mainly factorized in the super class because some games are quite similar.
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

}