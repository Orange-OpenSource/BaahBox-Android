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
import androidx.lifecycle.*
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockContext
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockLifecycleOwner
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockView
import org.jetbrains.anko.runOnUiThread
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
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
    fun gesturesDemoConstructor() {

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

    // ********************
    // GesturesDemoListener
    // ********************

    /**
     * Test the constructors for the object
     */
    @Test
    fun gestureDemoListenerConstructor() {
        // Test with virgin sensors
        createUnobservedDetector()
        // Test with sensors having observers
        createObservedDetector()
    }

    /**
     * Test the onShowPress() method
     */
    @Test
    fun onShowPress() {
        var gesturesDemoListener = createUnobservedDetector()
        gesturesDemoListener.onShowPress(null)
        gesturesDemoListener = createObservedDetector()
        gesturesDemoListener.onShowPress(null)
    }

    /**
     * Test the onSingleTapUp() method
     */
    @Test
    fun onSingleTapUp() {
        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onSingleTapUp(null))
        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onSingleTapUp(null))
    }

    /**
     * Test the onDown() method
     */
    @Test
    fun onDown() {
        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onDown(null))
        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onDown(null))
    }

    /**
     * Test the onLongPress() method
     */
    @Test
    fun onLongPress() {
        var gesturesDemoListener = createUnobservedDetector()
        gesturesDemoListener.onLongPress(null)
        gesturesDemoListener = createObservedDetector()
        gesturesDemoListener.onLongPress(null)
    }

    /**
     * Test the onFling() method
     */
    @Test
    fun onFling() {
        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(
            gesturesDemoListener.onFling(
                null,
                null,
                Float.MIN_VALUE,
                Float.MAX_VALUE
            )
        )
        gesturesDemoListener = createObservedDetector()
        assertTrue(
            gesturesDemoListener.onFling(
                null,
                null,
                Float.MIN_VALUE,
                Float.MAX_VALUE
            )
        )
    }

    /**
     * Test the onScroll() method
     */
    @Test
    fun onScroll() {
        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(
            gesturesDemoListener.onScroll(
                null,
                null,
                Float.MIN_VALUE,
                Float.MAX_VALUE
            )
        )
        gesturesDemoListener = createObservedDetector()
        assertTrue(
            gesturesDemoListener.onScroll(
                null,
                null,
                Float.MIN_VALUE,
                Float.MAX_VALUE
            )
        )
    }

    // ****************
    // Helper functions
    // ****************

    /**
     * Creates an instance of [GesturesDemoListener] with sensors which do not have observers
     *
     * @return GesturesDemoListener
     */
    private fun createUnobservedDetector(): GesturesDemoListener {
        val sensorA = MutableLiveData<Int>()
        val sensorB = MutableLiveData<Int>()
        return GesturesDemoListener(sensorA, sensorB)
    }

    /**
     * Creates an instance of [GesturesDemoListener] with sensors which have observers
     *
     * @return GesturesDemoListener
     */
    private fun createObservedDetector(): GesturesDemoListener {
        val sensorA = MutableLiveData<Int>()
        val sensorB = MutableLiveData<Int>()
        val appContext = mockContext()
        val observer = Observer<Int> {}
        appContext.runOnUiThread {
            sensorA.observe(mockLifecycleOwner(), observer)
            sensorB.observe(mockLifecycleOwner(), observer)
        }
        return GesturesDemoListener(sensorA, sensorB)
    }

}