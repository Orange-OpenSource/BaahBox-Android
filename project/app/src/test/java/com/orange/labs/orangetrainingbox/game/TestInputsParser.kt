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
package com.orange.labs.orangetrainingbox.game

import android.bluetooth.BluetoothGattCharacteristic
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

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

        // To compute our own muscle data and ensure the BluetoothGattCharacteristic computes another object with the
        // ame raw values
        val mockAndCheck: (Int, Int, Int, Int, Int) -> Pair<MuscleData, BluetoothGattCharacteristic> = {
            c1, a1, c2, a2, joystick ->
            val expectedMuscleData = MuscleData( c1 * 32 + a1, c2 * 32 + a2, joystick)
            val mock = createMockCharacteristic(c1, a1, c2, a2, joystick)
            Pair(expectedMuscleData, mock)
        }

        // To test if the BluetoothGattCharacteristic produces expected muscle data according to raw values
        val testFrameParsing: (Int, Int, Int, Int, Int) -> Unit = {
            c1, a1, c2, a2, joystick ->
            val (expectedMuscle, bluetoothMock) = mockAndCheck(c1, a1, c2, a2, joystick)
            val gottenMuscle = InputsParser.extractValuesCharacteristic(bluetoothMock)
            assertEquals(expectedMuscle.muscle1, gottenMuscle.muscle1)
            assertEquals(expectedMuscle.muscle2, gottenMuscle.muscle2)
            assertEquals(expectedMuscle.joystick, gottenMuscle.joystick)
        }

        // Time to test!
        testFrameParsing(0, 0, 0, 0, 0)
        testFrameParsing(20, 15, 20, 15, 0)
        testFrameParsing(6, 15, 0, 0, 1)
        testFrameParsing(7, 0, 90, 0, 2)
        testFrameParsing(20, 0, 90, 0, 3)
        testFrameParsing(85, 4, 42, 3, 4)

    }

    // ****************
    // Helper functions
    // ****************

    /**
     * Creates using Mockito library a mock object for class BluetoothGattCharacteristic.
     *
     * This mock will return the same frame with raw values.
     * The format of the frame follows implementation of the v2.0.0 protocol of Arduino firmware embedded in
     * the Baah Box (https://github.com/Orange-OpenSource/BaahBox-Arduino/releases).
     *
     * Thus the frame contains 6 bytes like:
     <pre>
        It models data like <muscle1, muscle2, Joystic=JBin, EndOfFrame>
        Where:
            - muscle1 = C1 x 32 + a1
            - muscle2 = C2 x 32 + a2
            - joystic = JBin
            - EndOfFrame = 90 -> '\n'
     </pre>
     *
     * See: https://site.mockito.org/
     *
     * @param c1 Used to compute the muscle 1 value, multiplied by 32
     * @param a1 Added to result of c1 x 32 to compute muscle 1 value
     * @param c2 Used to compute the muscle 2 value, multiplied by 32
     * @param a2 Added to result of c2 x 32 to compute muscle 2 value
     * @param joystick Joystick value
     * @return BluetoothGattCharacteristic The mocked object
     */
    private fun createMockCharacteristic(c1: Int, a1: Int, c2: Int, a2: Int, joystick: Int): BluetoothGattCharacteristic {

        val mock: BluetoothGattCharacteristic = mock(BluetoothGattCharacteristic::class.java)

        `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0)).thenReturn(c1)
        `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,1)).thenReturn(a1)
        `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,2)).thenReturn(c2)
        `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,3)).thenReturn(a2)
        `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,4)).thenReturn(joystick)

        return mock

    }

}