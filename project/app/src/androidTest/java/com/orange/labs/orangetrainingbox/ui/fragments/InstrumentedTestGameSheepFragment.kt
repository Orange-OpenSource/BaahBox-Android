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

import androidx.test.runner.AndroidJUnit4
import com.orange.labs.orangetrainingbox.R
import org.junit.runner.RunWith


/**
 * To test [GameSheepFragment] class.
 * See [AbstractInstrumentedTestSimpleGameFragment].
 *
 * @author Pierre-Yves Lapersonne
 * @since 30/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestGameSheepFragment : AbstractInstrumentedTestSimpleGameFragment() {

    /**
     * The text to find in the tool bar
     */
    override val rStringGameTitle: Int
        get() = R.string.title_game_sheep

    /**
     * First line of instruction
     */
    override val rStringGameInstructionLine1: Int
        get() = R.string.game_sheep_instructions_line_1

    /**
     * Second line of instruction
     */
    override val rStringGameInstructionLine2: Int
        get() = R.string.game_sheep_instructions_line_2

    /**
     * Id of the layout for playing mode
     */
    override val playingModeLayoutId: Int
        get() = R.id.clSheepGamePlaying

}