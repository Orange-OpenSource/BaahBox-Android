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

    }
}