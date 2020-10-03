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

import android.app.Activity
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGattCharacteristic
import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.orange.labs.orangetrainingbox.ui.settings.SettingsActivity
import org.mockito.ArgumentMatchers
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

/**
 * Utility object to use for mocks
 *
 * @since 20/03/2020
 * @version 1.0.0
 */
class MockUtils {

    companion object {

        // *****
        // Views
        // *****

        /**
         * Creates a mock view
         */
        fun mockView(): View {
            return mock(View::class.java)
        }

        /**
         * Creates a mock view
         *
         * @param x - 2D position in X axis
         * @param y - 2D position in Y axis
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
         * Creates a dumb mock [Activity]
         * @return Activity
         */
        fun mockActivity(): Activity {
            return mock(Activity::class.java)
        }

        /**
         * Create a mock [SettingsActivity] with details to display about version.
         *
         * @param fakeVersionName -
         * @param fakeVersionCode -
         * @return SettingsActivity
         */
        fun mockSettingsActivity(fakeVersionName: String, fakeVersionCode: Int): SettingsActivity {
            return mock(SettingsActivity::class.java)
        }

        /**
         * Creates a dumb mock of [Resources]
         * @return Resources
         */
        private fun mockResources(): Resources {
            return mock(Resources::class.java)
        }

        /**
         * Creates a mock [Activity] used for licenses displayer tests.
         * Will embeds mock data for 4 entries with managed licenses (if flag set to true, or not if false)
         *
         * @param containsUnmanagedLicense -
         * @return Activity
         */
        fun mockActivityWithGoodLicenses(containsUnmanagedLicense: Boolean): Activity {

            // Mocks
            val activity = mockActivity()
            val resources = mockResources()
            `when`(activity.resources).thenReturn(resources)

            // Return the array of licences names
            `when`(resources.getStringArray(ArgumentMatchers.eq(R.array.credits_names)))
                .thenReturn(
                    if (containsUnmanagedLicense) {
                        arrayOf("Apache 2.0", "EPL 1.0", "GPL 3.0", "MIT", "Dummy license")
                    } else {
                        arrayOf("Apache 2.0", "EPL 1.0", "GPL 3.0", "MIT")
                    }
                )

            // Return the array for credits lines
            `when`(resources.getStringArray(ArgumentMatchers.eq(R.array.credits_copyrights)))
                .thenReturn(
                    if (containsUnmanagedLicense) {
                        arrayOf("Foo", "Bar", "Wizz", "Yolo", "Dummy")
                    } else {
                        arrayOf("Foo", "Bar", "Wizz", "Yolo")
                    }
                )

            // Returns the licences body in an array
            `when`(resources.getStringArray(ArgumentMatchers.eq(R.array.credits_licenses)))
                .thenReturn(
                    if (containsUnmanagedLicense) {
                        arrayOf("Apache 2.0", "EPL 1.0", "GPL 3.0", "MIT", "Dummy license")
                    } else {
                        arrayOf("Apache 2.0", "EPL 1.0", "GPL 3.0", "MIT")
                    }
                )

            // Returns the URL
            `when`(resources.getStringArray(ArgumentMatchers.eq(R.array.credits_url)))
                .thenReturn(
                    if (containsUnmanagedLicense) {
                        arrayOf("https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt")
                    } else {
                        arrayOf("https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "https://github.com/Orange-OpenSource/BaahBox-Android/blob/dev/LICENSE.txt",
                            "dummy")
                    }
            )


            // Mock the values which will be used for the dialog
            `when`(activity.getString(ArgumentMatchers.eq(de.psdev.licensesdialog.R.string.notices_title)))
                .thenReturn("Notice title text")
            `when`(activity.getString(ArgumentMatchers.eq(de.psdev.licensesdialog.R.string.notices_close)))
                .thenReturn("Notice close text")
            `when`(activity.getString(ArgumentMatchers.eq(de.psdev.licensesdialog.R.string.notices_default_style)))
                .thenReturn("Notices style")
            `when`(activity.getString(ArgumentMatchers.eq(de.psdev.licensesdialog.R.string.notices_title)))
                .thenReturn("Notice title")

            return activity
        }

        /**
        * Creates a dumb mock [Activity]
        * @return Activity
        */
        fun mockImageView(): ImageView {
            return mock(ImageView::class.java)
        }

        // ********
        // LiveData
        // ********

        /**
         * Mocks a [Context] object
         */
        fun mockContext(): Context {
            return mock(Context::class.java)
        }

        /**
         * Helper function creating a mock object of [LifecycleOwner] for live data observers
         */
        fun mockLifecycleOwner(): LifecycleOwner {
            val owner = mock(LifecycleOwner::class.java)
            val lifecycle = LifecycleRegistry(owner)
            lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
            `when`(owner.lifecycle).thenReturn(lifecycle)
            return owner
        }

        // *********
        // Bluetooth
        // *********

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
         * Creates using Mockito library a mock object for class BluetoothGattCharacteristic.
         *
         * This mock will return the same frame with raw values.
         * The format of the frame follows implementation of the v2.0.0 protocol of Arduino firmware embedded in
         * the Baah Box (https://github.com/Orange-OpenSource/BaahBox-Arduino/releases).
         *
         * Thus the frame contains 6 bytes like:
        <pre>
        It models data like <muscle1, muscle2, Joystick = JBin, EndOfFrame>
        Where:
        - muscle1 = C1 x 32 + a1
        - muscle2 = C2 x 32 + a2
        - joystick = JBin
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

        // *******
        // Motions
        // *******

//        /**
//         * Creates a mock [MotionEvent] which returns the given value for x and y when called
//         *
//         * @param fakeX The value to return for X property
//         * @param fakeY The value to return for Y property
//         * @return MotionEvent The mock object
//         */
//        fun mockMotionEvent(fakeX: Float, fakeY: Float): MotionEvent {
//            val mockMotionEvent = mock(MotionEvent::class.java)
//            `when`(mockMotionEvent.x).thenReturn(fakeX)
//            `when`(mockMotionEvent.y).thenReturn(fakeY)
//            return mockMotionEvent
//        }

    }
}