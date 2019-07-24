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

/**
 * Utility class allowing to prepare raw sensors inputs for game logic.
 *
 * @author Pierre-Yves Lapersonne
 * @since 16/05/2019
 * @version 1.1.0
 */
class InputsParser {


    // **********
    // Companions
    // **********

    companion object Parser {

        /**
         * Parses a raw value returned by sensors to a game-logic suitable value.
         * Will be divided by 10 and multiplied by a factor.
         * A [DifficultyFactor.MEDIUM] will be applied.
         *
         * @param sensorValue The integer to parsed, should be between [0 ; 1000]
         * @param factor The factor to apply for the hardness difficulty
         * @return Int The game-logic value
         */
        fun prepareValue(sensorValue: Int, factor: Double): Int {
            return (sensorValue / 10 * factor).toInt()
        }

        /**
         * Using a BLE frame, extracts from it integer values, parse them and return suitable data.
         * Returns a bundle of data with cleaned and computed values for muscles and joystick.
         * If these values are equal to 1, the frame is null.
         *
         * @param frame The object where the data must be extracted
         * @return MuscleData The bundle of data
         */
        fun extractValuesCharacteristic(frame: BluetoothGattCharacteristic?): MuscleData {

            /*
            The frame returned by the Baah box Bluetooth device contains 6 bytes and follows this template:

            C1|a1|C2|a2|JBin|90

            It models data like <muscle1, muscle2, Joystic=JBin, EndOfFrame>
            Where:
                - muscle1 = C1 x 32 + a1
                - muscle2 = C2 x 32 + a2
                - joystic = JBin
                - EndOfFrame = 90 -> '\n'

            */

            if (frame == null) return MuscleData(-1, -1, -1)

            val c1 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0)
            val a1 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,1)
            val c2 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,2)
            val a2 = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,3)
            val joystick = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,4)
            //val stop = frame.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,5)

            val muscle1 = c1 * 32 + a1
            val muscle2 = c2 * 32 + a2

            return MuscleData(muscle1, muscle2, joystick)

        }

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