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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockContext
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockLifecycleOwner
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockView
import org.jetbrains.anko.runOnUiThread

import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

/**
 * To test [GesturesDemo] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/03/2020
 * @version 1.0.0
 */
class UnitTestGesturesDemo {

    /**
     * Rule to use to deal with MutableLiveData objects.
     */
    @Rule
    @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    // ************
    // GesturesDemo
    // ************
    
    /**
     * Test the constructors for the object
     */
    @Test
    fun constructor() {

        // Test with virgin sensors
        var sensorA = MutableLiveData<Int>()
        var sensorB = MutableLiveData<Int>()
        assertNotNull(GesturesDemoListener(sensorA, sensorB))

        // Test with sensors having observers
        sensorA = MutableLiveData()
        sensorB = MutableLiveData()
        val appContext = mockContext()
        val observer = Observer<Int> {}
        appContext.runOnUiThread {
            sensorA.observe(mockLifecycleOwner(), observer)
            sensorB.observe(mockLifecycleOwner(), observer)
        }

        assertNotNull(GesturesDemo(sensorA, sensorB))

    }

    /**
     * Test the addGestureListeners() method by just calling it.
     */
    @Test
    fun addGestureListeners() {

        val mockedView = mockView()
        val gesturesDemo = GesturesDemo(MutableLiveData(),MutableLiveData())
        val appContext = mockContext()
        appContext.runOnUiThread {
            gesturesDemo.addGestureListeners(mockedView, appContext)
        }

    }

}