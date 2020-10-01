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
package com.orange.labs.orangetrainingbox.btle

import android.content.Context
import androidx.test.InstrumentationRegistry
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleRegistry
import androidx.lifecycle.LifecycleOwner
import org.junit.After


/**
 * To test [TrainingBoxViewModel] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 28/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestTrainingBoxViewModel {


    /**
     * The object to test
     */
    private var trainingBoxViewModel: TrainingBoxViewModel? = null

    /**
     * To get UI thread
     */
    private var appContext: Context? = null


    /**
     * Init the object to test
     */
    @Before
    fun setup(){
        trainingBoxViewModel = TrainingBoxViewModel()
        appContext = InstrumentationRegistry.getTargetContext()
    }

    /**
     * Init the object to test
     */
    @After
    fun tearDown(){
        trainingBoxViewModel = null
        appContext = null
    }

    /**
     * Test the sensors A and B properties of the [TrainingBoxViewModel] with limit values
     */
    @Test
    fun sensorsAtLimit() {

        val testAtLimits: (MutableLiveData<Int>) -> Unit = { sensor ->
            sensor.postValue(Int.MIN_VALUE)
            sensor.postValue(Int.MAX_VALUE)
            for (i in 0..1024) {
                sensor.postValue(i)
            }
        }

        testAtLimits(trainingBoxViewModel?.sensorA!!)
        testAtLimits(trainingBoxViewModel?.sensorB!!)

    }

    /**
     * Test observable/observer pattern for sensors properties of the [TrainingBoxViewModel]
     */
    @Test
    fun sensorsObserving() {

        val testObserving: (MutableLiveData<Int>) -> Unit = { sensor ->
            val expected = 2048
            val observer = Observer<Int> { sensorValue ->
                assertTrue(sensorValue == expected)
            }
            appContext!!.runOnUiThread {
                sensor.observe(mockLifecycleOwner(), observer)
            }
            sensor.postValue(expected)
        }

        testObserving(trainingBoxViewModel?.sensorA!!)
        testObserving(trainingBoxViewModel?.sensorB!!)

    }

    /**
     * Test the getBoxes() method of [TrainingBoxViewModel]
     */
    @Test
    fun getBoxes() {
        trainingBoxViewModel?.getBoxes()
    }

    /**
     * Helper function creating a mock object of [LifecycleOwner] for live data observers
     */
    private fun mockLifecycleOwner(): LifecycleOwner {
        val owner = mock(LifecycleOwner::class.java)
        val lifecycle = LifecycleRegistry(owner)
        lifecycle.handleLifecycleEvent(Lifecycle.Event.ON_RESUME)
        `when`(owner.lifecycle).thenReturn(lifecycle)
        return owner
    }

}
