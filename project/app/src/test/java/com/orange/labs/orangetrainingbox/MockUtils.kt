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
package com.orange.labs.orangetrainingbox

import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.view.View
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Utility object to use for mocks
 *
 * @author Pierre-Yves Lapersonne
 * @since 20/03/2020
 * @version 1.0.0
 */
class MockUtils {

    companion object {

        /**
         * Provides a mock [BluetoothDevice] given a name
         *
         * @param name - A string to identify the mock device, default valued to "mock_BluetoothDevice"
         * @return BluetoothDevice - The fake object
         */
        fun mockBluetoothDevice(name: String = "mock_BluetoothDevice"): BluetoothDevice {
            val device: BluetoothDevice = mock(BluetoothDevice::class.java)
            `when`(device.name).thenReturn(name)
            return device
        }

        /**
         * Creates a mock view
         *
         * @param x - 2D position in X axis
         * @param y - 2D poxition in Y axis
         * @param width - Width of the view
         * @param height - Height of the view
         * @return View
         */
        fun mockView(x: Int, y: Int, width: Int, height: Int): View {
            val view = mock(View::class.java)
            // Mock getLocationOnScreen() because it does not return anything and modify array in param
            Mockito.doAnswer { invocation ->
                val args = invocation.arguments[0] as IntArray
                args[0] = x
                args[1] = y
                args
            }.`when`(view).getLocationOnScreen(ArgumentMatchers.any(IntArray::class.java))
            // Mock properties
            `when`(view.width).thenReturn(width)
            `when`(view.height).thenReturn(height)
            return view
        }

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
        fun mockBluetoothGattCharacteristic(c1: Int, a1: Int, c2: Int, a2: Int, joystick: Int): BluetoothGattCharacteristic {

            val mock: BluetoothGattCharacteristic = mock(BluetoothGattCharacteristic::class.java)

            `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,0)).thenReturn(c1)
            `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,1)).thenReturn(a1)
            `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,2)).thenReturn(c2)
            `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,3)).thenReturn(a2)
            `when`(mock.getIntValue(BluetoothGattCharacteristic.FORMAT_UINT8,4)).thenReturn(joystick)

            return mock

        }

    }
}