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
package com.orange.labs.orangetrainingbox.utils.game

import com.orange.labs.orangetrainingbox.game.InputsParser
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
* To test [InputsParser] class.
*
* @author Pierre-Yves Lapersonne
* @since 23/08/2019
* @version 1.0.0
*/
class TestInputsParser {

    /**
     * Test the prepareValue() method
     */
    @Test
    fun prepareValue() {

        //                                              Picked from prepareValue() definition
        val expectedPreparedValue: (Int, Double) -> Int = { value, factor -> ((value / 10) * factor).toInt() }

        // Be lazy ;-)
        val assertEqualsForValues: (Int, Double) -> Unit = { value, factor ->
            assertEquals(InputsParser.prepareValue(value, factor), expectedPreparedValue(value, factor))
        }

        assertEqualsForValues(0, 0.0)
        assertEqualsForValues(Int.MIN_VALUE, Double.MIN_VALUE)
        assertEqualsForValues(Int.MAX_VALUE, Double.MAX_VALUE)
        assertEqualsForValues(0, 10.0)
        assertEqualsForValues(10, 0.0)
        assertEqualsForValues(10, 10.0)
        assertEqualsForValues(1023, 1023.0)
        assertEqualsForValues(1024, 1024.0)
        assertEqualsForValues(1024, 1.5)
        assertEqualsForValues(2022, 0.9)
        assertEqualsForValues(888, 0.7)

    }

    /**
     * Test the extractValuesCharacteristic() method
     */
    @Test
    fun extractValuesCharacteristic() {

        // Null frame
        val muscleData = InputsParser.extractValuesCharacteristic(null)
        assertTrue(muscleData.muscle1 == -1)
        assertTrue(muscleData.muscle2 == -1)
        assertTrue(muscleData.joystick == -1)

    }

}