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
package com.orange.labs.orangetrainingbox.ui.demo

import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.jetbrains.anko.runOnUiThread
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * To test [GesturesDemo] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 03/09/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestGesturesDemo {


    /**
     * Test the constructors for the object
     */
    @Test
    fun constructor() {

        // Test with virgin sensors
        var sensorA = MutableLiveData<Int>()
        var sensorB = MutableLiveData<Int>()
        GesturesDemoListener(sensorA,sensorB)

        // Test with sensors having observers
        sensorA = MutableLiveData()
        sensorB = MutableLiveData()
        val appContext =  InstrumentationRegistry.getTargetContext()
        val observer = Observer<Int> {}
        appContext!!.runOnUiThread {
            sensorA.observe(mockLifecycleOwner(), observer)
            sensorB.observe(mockLifecycleOwner(), observer)
        }

        GesturesDemo(sensorA, sensorB)

    }

    /**
     * Test the addGestureListeners() method
     */
    @Test
    fun addGestureListeners() {

        val mockedView = Mockito.mock(View::class.java)
        val gesturesDemo = GesturesDemo(MutableLiveData(),MutableLiveData())
        val appContext =  InstrumentationRegistry.getTargetContext()
        appContext!!.runOnUiThread {
            gesturesDemo.addGestureListeners(mockedView, appContext)
        }

    }

    // Helper functions

    /**
     * Helper function creating a mock object of [LifecycleOwner] for live data observers
     */
    private fun mockLifecycleOwner(): LifecycleOwner {
        val owner = Mockito.mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        Mockito.`when`(owner.lifecycle).thenReturn(lifecycle)
        return owner
    }

}