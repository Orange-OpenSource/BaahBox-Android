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

import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.jetbrains.anko.runOnUiThread
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

/**
 * To test [GesturesDemoListener] class.
 * <b>It seems Mockito cannot mock final class, specially big classes like [MotionEvent]

    <pre>
        org.mockito.exceptions.base.MockitoException:
        Cannot mock/spy class android.view.MotionEvent
        Mockito cannot mock/spy because :
        - final class
    </pre>
 *
 * So some test cases are for now commented. :'(
 * </b>
 *
 * @author Pierre-Yves Lapersonne
 * @since 03/09/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestGesturesDemoListener {


    /**
     * Test the constructors for the object
     */
    @Test
    fun constructor() {

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
        //gesturesDemoListener.onShowPress(Mockito.mock(MotionEvent::class.java))

        gesturesDemoListener = createObservedDetector()
        gesturesDemoListener.onShowPress(null)
        //gesturesDemoListener.onShowPress(Mockito.mock(MotionEvent::class.java))

    }

    /**
     * Test the onSingleTapUp() method
     */
    @Test
    fun onSingleTapUp() {

        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onSingleTapUp(null))
        //assertTrue(gesturesDemoListener.onSingleTapUp(Mockito.mock(MotionEvent::class.java)))

        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onSingleTapUp(null))
        //assertTrue(gesturesDemoListener.onSingleTapUp(Mockito.mock(MotionEvent::class.java)))

    }

    /**
     * Test the onDown() method
     */
    @Test
    fun onDown() {

        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onDown(null))
        //assertTrue(gesturesDemoListener.onDown(Mockito.mock(MotionEvent::class.java)))

        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onDown(null))
        //assertTrue(gesturesDemoListener.onDown(Mockito.mock(MotionEvent::class.java)))

    }

    /**
     * Test the onLongPress() method
     */
    @Test
    fun onLongPress() {

        var gesturesDemoListener = createUnobservedDetector()
        gesturesDemoListener.onLongPress(null)
        //gesturesDemoListener.onLongPress(Mockito.mock(MotionEvent::class.java))

        gesturesDemoListener = createObservedDetector()
        gesturesDemoListener.onLongPress(null)
        //gesturesDemoListener.onLongPress(Mockito.mock(MotionEvent::class.java))

    }

    /**
     * Test the onFling() method
     */
    @Test
    fun onFling() {

        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onFling(null, null, Float.MIN_VALUE, Float.MAX_VALUE))
        /*
        assertTrue(gesturesDemoListener.onFling(
            Mockito.mock(MotionEvent::class.java),
            Mockito.mock(MotionEvent::class.java),
            0f,
            0.00000001f))
        assertTrue(gesturesDemoListener.onFling(
            Mockito.mock(MotionEvent::class.java),
            Mockito.mock(MotionEvent::class.java),
            3.14f,
            0.42f))
        */
        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onFling(null, null, Float.MIN_VALUE, Float.MAX_VALUE))
        /*
        assertTrue(gesturesDemoListener.onFling(
            Mockito.mock(MotionEvent::class.java),
            Mockito.mock(MotionEvent::class.java),
            0f,
            0.00000001f))
        assertTrue(gesturesDemoListener.onFling(
            Mockito.mock(MotionEvent::class.java),
            Mockito.mock(MotionEvent::class.java),
            3.14f,
            0.42f))
        */
    }

    /**
     * Test the onScroll() method
     */
    @Test
    fun onScroll() {

        var gesturesDemoListener = createUnobservedDetector()
        assertTrue(gesturesDemoListener.onScroll(
            null,
            null,
            Float.MIN_VALUE,
            Float.MAX_VALUE
        ))

        gesturesDemoListener = createObservedDetector()
        assertTrue(gesturesDemoListener.onScroll(
            null,
            null,
            Float.MIN_VALUE,
            Float.MAX_VALUE
        ))

        /*
        val testUsingMock: (GesturesDemoListener) -> Unit = { gesturesDemoListener ->

            val testWithMotionEvents: (Pair<Float, Float>, Pair<Float, Float>) -> Unit = {
                    fake1, fake2 ->
                val mockMotionEvent1 = createMockMotionEvent(fake1.first, fake1.second)
                val mockMotionEvent2 = createMockMotionEvent(fake2.first, fake2.second)
                assertTrue(gesturesDemoListener.onScroll(
                    mockMotionEvent1,
                    mockMotionEvent2,
                    Float.MIN_VALUE,
                    Float.MAX_VALUE
                ))
            }

            testWithMotionEvents(Pair(0f, 0f), Pair(0f, 0f))
            testWithMotionEvents(Pair(Float.MIN_VALUE, Float.MIN_VALUE), Pair(Float.MAX_VALUE, Float.MAX_VALUE))
            testWithMotionEvents(Pair(Float.MAX_VALUE, Float.MAX_VALUE), Pair(Float.MIN_VALUE, Float.MIN_VALUE))
            testWithMotionEvents(Pair(0f, 0f), Pair(100f, 100f))
            testWithMotionEvents(Pair(100f, 100f), Pair(0f, 0f))

        }

        testUsingMock(createObservedDetector())
        testUsingMock(createUnobservedDetector())
        */

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
        val appContext =  InstrumentationRegistry.getTargetContext()
        val observer = Observer<Int> {}
        appContext!!.runOnUiThread {
            sensorA.observe(mockLifecycleOwner(), observer)
            sensorB.observe(mockLifecycleOwner(), observer)
        }
        return GesturesDemoListener(sensorA, sensorB)
    }

    /**
     * Creates a mock [MotionEvent] which returns the given value for x and y when called
     *
     * @param fakeX The value to return for X property
     * @param fakeY The value to return for Y property
     * @return MotionEvent The mock object
     */
     private fun createMockMotionEvent(fakeX: Float, fakeY: Float): MotionEvent {
        val mockMotionEvent = Mockito.mock(MotionEvent::class.java)
        Mockito.`when`(mockMotionEvent.x).thenReturn(fakeX)
        Mockito.`when`(mockMotionEvent.y).thenReturn(fakeY)
        return mockMotionEvent
    }

}