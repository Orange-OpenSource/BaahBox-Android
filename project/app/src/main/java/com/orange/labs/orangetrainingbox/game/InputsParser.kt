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
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import java.lang.IllegalArgumentException

/**
 * A divider to make the sensor value more suitable for game logic
 */
const val GAME_LOGIC_DIVIDER: Int = 10

/**
 * Utility class allowing to prepare raw sensors inputs for game logic.
 *
 * @since 16/05/2019
 * @version 1.3.0
 */
class InputsParser {

    // *******
    // Methods
    // *******

    /**
     * Parses a raw value returned by sensors to a game-logic suitable value.
     *
     * @param sensorValue The integer to parse, should be between [ 0 ; 1024 ]
     * @param factor The factor to apply for the difficulty, must be >= 0
     * @return Int The game-logic value
     */
    fun prepareValue(sensorValue: Int, factor: Double): Double {
        return prepareValue(sensorValue.toDouble(), factor)
    }

    /**
     * Parses a raw value returned by sensors to a game-logic suitable value.
     *
     * Needs to have [Double] values on both sides so as to keep information.
     * Will be divided by [GAME_LOGIC_DIVIDER] and multiplied by a [factor].
     *
     * Thus three cases occur:
     * - First, the [factor] is equal to [GAME_LOGIC_DIVIDER], then the game-logic value will be equal to the sensor value
     * - Second, the [factor] is greater than [GAME_LOGIC_DIVIDER], then the game-logic value will be greater than the sensor value
     * - Third, the [factor] is lower than [GAME_LOGIC_DIVIDER], then the game-logic value will be lower than the sensor value.
     *
     * The lower is the factor the harder may be the game.
     *
     * In fact, if [sensorValue] were an [Int], in the case where [factor] might be equal to [GAME_LOGIC_DIVIDER],
     * the game-logic value will be lower than the sensor value ; it may be a non-sense because
     * (x / y * z) == x if y == z
     *
     *
     * @param sensorValue The integer to parse, should be between [0 ; 1024]
     * @param factor The factor to apply for the difficulty, must be >= 0
     * @return Int The game-logic value
     */
    private fun prepareValue(sensorValue: Double, factor: Double): Double {
        if (factor <= 0) throw IllegalArgumentException("The factor must not be negative or null")
        return (sensorValue / GAME_LOGIC_DIVIDER * factor)
    }

    /**
     * Using a BLE frame, extracts from it integer values, parse them and return suitable data.
     * Returns a bundle of data with cleaned and computed values for muscles and joystick.
     * If these values are equal to -1, the frame is null.
     *
     * This implementation follows the frame format of version v2.0.0 of Arduino firmware embedded in the
     * Baah box (see https://github.com/Orange-OpenSource/BaahBox-Arduino/releases).
     *
     * In a nutshell, a frame with 6 bytes like:
     <pre>

        C1|a1|C2|a2|JBin|90

        Where:
            - muscle1 = C1 x 32 + a1
            - muscle2 = C2 x 32 + a2
            - joystick = JBin
            - EndOfFrame = 90 -> '\n', end of frame

     </pre>
     *
     * @param frame The object where the data must be extracted
     * @return MuscleData The bundle of data
     */
    fun extractValuesCharacteristic(frame: BluetoothGattCharacteristic?): MuscleData {

        /*
        The frame returned by the Baah box Bluetooth device contains 6 bytes and follows this template:

        C1|a1|C2|a2|JBin|90

        It models data like <muscle1, muscle2, Joystick=JBin, EndOfFrame>
        Where:
            - muscle1 = C1 x 32 + a1
            - muscle2 = C2 x 32 + a2
            - joystick = JBin
            - EndOfFrame = 90 -> '\n'

        */

        if (frame == null) return MuscleData(-1, -1, -1)

        val c1 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0)
        val a1 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,1)
        val c2 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,2)
        val a2 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,3)
        val joystick = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,4)
        val stop = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,5)

        val muscle1 = c1 * 32 + a1
        val muscle2 = c2 * 32 + a2

        Logger.d("BLE characteristic for sensor - <$c1 | $a1 | $c2 | $a2 | $joystick | $stop>, giving muscle 1 = $muscle1, muscle 2 = $muscle2")
        return MuscleData(muscle1, muscle2, joystick)

    }

}


/**
 * Models a bundle of data extracted from a BLE device frame.
 *
 * @param muscle1 The computed value picked from sensor for the muscle 1
 * @param muscle2 The computed value picked from sensor for the muscle 2
 * @param joystick The computed value picked from sensor for the joystick
 */
data class MuscleData(val muscle1: Int, val muscle2: Int, val joystick: Int)