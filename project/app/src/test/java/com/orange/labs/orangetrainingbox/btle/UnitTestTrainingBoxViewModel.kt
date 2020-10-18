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

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockBluetoothDevice
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

/**
 * To test [TrainingBoxViewModel] class.
 *
 * @since 20/03/2020
 * @version 1.0.0
 */
class UnitTestTrainingBoxViewModel {

    // **********
    // Properties
    // **********

    /**
     * The object to test
     */
    private lateinit var trainingBoxViewModel: TrainingBoxViewModel

    /**
     * Rule to use to deal with MutableLiveData objects.
     */
    @Rule @JvmField
    var instantTaskExecutorRule = InstantTaskExecutorRule()


    // **********
    // Test cases
    // **********

    /**
     * Check the difficulty factor once the [TrainingBoxViewModel] is created
     */
    @Test
    fun `should have difficulty factor set to medium`() {
        // Given
        trainingBoxViewModel = TrainingBoxViewModel()
        // When - Then
        assertTrue("Should be MEDIUM", trainingBoxViewModel.difficultyFactor
                                                                        == DifficultyFactor.MEDIUM)
    }

    /**
     * Once [TrainingBoxViewModel] is created and one box added, must contain only one box
     */
    @Test
    fun `should contain a bluetooth device when added`() {
        // Given
        trainingBoxViewModel = TrainingBoxViewModel()
        // When
        trainingBoxViewModel.addBox(mockBluetoothDevice())
        // Then
        trainingBoxViewModel.getBoxes().value?.let {
            assertTrue("Should contain only 1 box", it.count() == 1)
        } ?: run {
            fail("No box added, had to contain 1 object")
        }
    }

    /**
     * Once [TrainingBoxViewModel] is created and several boxes added, must contain all the boxes
     */
    @Test
    fun `should contain all bluetooth devices when added`() {
        // Given
        trainingBoxViewModel = TrainingBoxViewModel()
        // When
        trainingBoxViewModel.addBox(mockBluetoothDevice("Foo"))
        trainingBoxViewModel.addBox(mockBluetoothDevice("Bar"))
        // Then
        trainingBoxViewModel.getBoxes().value?.let {
            assertTrue("Should contain only 2 box", it.count() == 2)
            assertEquals(it.elementAt(0).name, "Foo")
            assertEquals(it.elementAt(1).name, "Bar")
        } ?: run {
            fail("Not expected number of boxes")
        }
    }

    /**
     * Once [TrainingBoxViewModel] is created should not contain boxes
     */
    @Test
    fun `should not contain boxes once created`() {
        // Given
        trainingBoxViewModel = TrainingBoxViewModel()
        // When - Then
        trainingBoxViewModel.getBoxes().value?.let {
            assertTrue("Once created a TrainingBoxViewModel should not have boxes",
                it.isEmpty()
            )
        }
    }

}