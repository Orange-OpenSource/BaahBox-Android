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
package com.orange.labs.orangetrainingbox.game

import android.bluetooth.BluetoothGattCharacteristic
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockBluetoothGattCharacteristic
import org.junit.Assert.*
import org.junit.Test

import java.lang.IllegalArgumentException

/**
* To test [InputsParser] class.
*
* @since 23/08/2019
* @version 1.0.0
*/
class UnitTestInputsParser {

    // ************
    // InputsParser
    // ************

    /**
     * The object to test
     */
    private var inputsParser = InputsParser()

    /**
     * A negative factor can't be used
     */
    @Test (expected = IllegalArgumentException::class)
    fun `should throw exception if factor is negative`() {
        // Given
        val sensorValue = 888
        val factor = -888.0
        // When - Then
        inputsParser.prepareValue(sensorValue, factor)
    }

    /**
     * A null factor can't be used
     */
    @Test (expected = IllegalArgumentException::class)
    fun `should throw exception if factor is null`() {
        // Given
        val sensorValue = 888
        val factor = 0.0
        // When - Then
        inputsParser.prepareValue(sensorValue, factor)
    }

    /**
     * A prepared value must be equal to sensor value if factor is equal to [GAME_LOGIC_DIVIDER]
     */
    @Test
    fun `prepared value should be equal to sensor value if factor equal to divider`() {
        // Given
        val sensorValue = 237
        val factor = GAME_LOGIC_DIVIDER.toDouble()
        // When
        val gameValue = inputsParser.prepareValue(sensorValue, factor)
        // Then
        assertTrue("$sensorValue != $gameValue", sensorValue.toDouble() == gameValue)
    }

    /**
     * A prepared value must be greater than sensor value if factor is greater than [GAME_LOGIC_DIVIDER]
     */
    @Test
    fun `prepared value should be greater than sensor value if factor greater than divider`() {
        // Given
        val sensorValue = 237
        val factor = GAME_LOGIC_DIVIDER.toDouble() * 2
        // When
        val gameValue = inputsParser.prepareValue(sensorValue, factor)
        // Then
        assertTrue("$sensorValue >= $gameValue", gameValue > sensorValue.toDouble())
    }

    /**
     * A prepared value must be lower than sensor value if factor is lower than [GAME_LOGIC_DIVIDER]
     */
    @Test
    fun `prepared value should be lower than sensor value if factor lower than divider`() {
        // Given
        val sensorValue = 237
        val factor = GAME_LOGIC_DIVIDER.toDouble() * 0.5
        // When
        val gameValue = inputsParser.prepareValue(sensorValue, factor)
        // Then
        assertTrue("$sensorValue <= $gameValue", gameValue < sensorValue.toDouble())
    }

    /**
     * Ensures the [GAME_LOGIC_DIVIDER] factor ha snot been changed without care
     */
    @Test
    fun `the game logic divider should be equal to 10`() {
        assertTrue("$GAME_LOGIC_DIVIDER != 10", GAME_LOGIC_DIVIDER == 10)
    }

    /**
     * If no frame is processed, returns default muscle data
     */
    @Test
    fun `muscle data should be -1 if not frame is processed`(){
        // Given
        val frame: BluetoothGattCharacteristic? = null
        // When
        val muscleData = inputsParser.extractValuesCharacteristic(frame)
        // Then
        assertTrue(muscleData.muscle1 == -1)
        assertTrue(muscleData.muscle2 == -1)
        assertTrue(muscleData.joystick == -1)
    }

    /**
     * Checks if the muscle 1 value if computed using c1 multiplied by 32 and with added a1
     */
    @Test
    fun `muscle1 should be equal to c1 * 32 + a1`(){
        // Given
        val c1 = 5
        val a1 = 6
        val expected = c1 * 32 + a1
        val frame = mockBluetoothGattCharacteristic(c1, a1, 0, 0, 0)
        // When
        val muscleData = inputsParser.extractValuesCharacteristic(frame)
        // Then
        assertEquals("${muscleData.muscle1} != $expected", muscleData.muscle1, expected)
    }

    /**
     * Checks if the muscle 2 value if computed using c2 multiplied by 32 and with added a2
     */
    @Test
    fun `muscle2 should be equal to c2 * 32 + a2`(){
        // Given
        val c2 = 64
        val a2 = 9
        val expected = c2 * 32 + a2
        val frame = mockBluetoothGattCharacteristic(0, 0, c2, a2, 0)
        // When
        val muscleData = inputsParser.extractValuesCharacteristic(frame)
        // Then
        assertEquals("${muscleData.muscle2} != $expected", muscleData.muscle2, expected)
    }

    /**
     * Checks if the joystick value is equal to jbin
     */
    @Test
    fun `joystick should be equal to jbin`(){
        // Given
        val joystick = 42 // expected and for mock
        val frame = mockBluetoothGattCharacteristic(0, 0, 0, 0, joystick)
        // When
        val muscleData = inputsParser.extractValuesCharacteristic(frame)
        // Then
        assertEquals("${muscleData.joystick} != $joystick", muscleData.joystick, joystick)
    }

    /**
     * Test the extractValuesCharacteristic() method with limit and random values (defined or null).
     */
    @Test
    fun extractValuesCharacteristic() {

        // Null frame
        val muscleData = inputsParser.extractValuesCharacteristic(null)
        assertTrue(muscleData.muscle1 == -1)
        assertTrue(muscleData.muscle2 == -1)
        assertTrue(muscleData.joystick == -1)

        // To compute our own muscle data and ensure the BluetoothGattCharacteristic computes another object with the
        // same raw values
        val mockAndCheck: (Int, Int, Int, Int, Int) -> Pair<MuscleData, BluetoothGattCharacteristic> = {
            c1, a1, c2, a2, joystick ->
            val expectedMuscleData = MuscleData( c1 * 32 + a1, c2 * 32 + a2, joystick)
            val mock = mockBluetoothGattCharacteristic(c1, a1, c2, a2, joystick)
            Pair(expectedMuscleData, mock)
        }

        // To test if the BluetoothGattCharacteristic produces expected muscle data according to raw values
        val testFrameParsing: (Int, Int, Int, Int, Int) -> Unit = {
            c1, a1, c2, a2, joystick ->
            val (expectedMuscle, bluetoothMock) = mockAndCheck(c1, a1, c2, a2, joystick)
            val gottenMuscle = inputsParser.extractValuesCharacteristic(bluetoothMock)
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

    // **********
    // MuscleData
    // **********

    /**
     *
     */
    @Test
    fun `muscle1 can be equal to 0`(){
        assertNotNull(MuscleData(0, 1, 1))
    }

    /**
     *
     */
    @Test
    fun `muscle1 can be negative`(){
        assertNotNull(MuscleData(-1, 1, 1))
    }

    /**
     *
     */
    @Test
    fun `muscle1 can be positive`(){
        assertNotNull(MuscleData(1, 1, 1))
    }

    /**
     *
     */
    @Test
    fun `muscle2 can be equal to 0`(){
        assertNotNull(MuscleData(1, 0, 1))
    }

    /**
     *
     */
    @Test
    fun `muscle2 can be negative`(){
        assertNotNull(MuscleData(1, -1, 1))
    }

    /**
     *
     */
    @Test
    fun `muscle2 can be positive`(){
        assertNotNull(MuscleData(1, 1, 1))
    }

    /**
     *
     */
    @Test
    fun `joystick can be equal to 0`(){
        assertNotNull(MuscleData(1, 1, 0))
    }

    /**
     *
     */
    @Test
    fun `joystick can be negative`(){
        assertNotNull(MuscleData(1, 1, -1))
    }

    /**
     *
     */
    @Test
    fun `joystick can be positive`(){
        assertNotNull(MuscleData(1, 1, 1))
    }

}