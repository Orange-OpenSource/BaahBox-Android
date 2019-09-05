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
package com.orange.labs.orangetrainingbox.btle

import android.bluetooth.BluetoothDevice
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.orange.labs.orangetrainingbox.game.DifficultyFactor

// *******
// Classes
// *******

/**
 * View model for the training box based on the Android ViewModel design pattern
 * (https://developer.android.com/topic/libraries/architecture/viewmodel)
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 20/11/2018
 * @version 2.0.0
 */
class TrainingBoxViewModel : ViewModel() {


    // **********
    // Properties
    // **********

    /**
     * A set of Bluetooth Low Energy (BLE) devices, thus the set of Baah Boxes
     */
    private lateinit var boxes: MutableLiveData<MutableSet<BluetoothDevice>>

    /**
     * One of the two object modeling a BLE sensors data source of the baah box
     */
    val sensorA: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /**
     * One of the two objects modeling a BLE sensors data source of the baah box.
     * This sensor is used for the star game.
     */
    val sensorB: MutableLiveData<Int> by lazy { MutableLiveData<Int>() }

    /**
     * The global hardness factor to apply to computations for games
     */
    var difficultyFactor: DifficultyFactor = DifficultyFactor.MEDIUM


    // *******
    // Methods
    // *******

    /**
     * Adds a new Bluetooth device to use for the app
     *
     * @param box A reference to the Bluetooth device
     * @see [BluetoothDevice]
     */
    fun addBox(box: BluetoothDevice) {
        if (!::boxes.isInitialized) {
            boxes = MutableLiveData()
            boxes.postValue(mutableSetOf(box))
        }
        boxes.value?.add(box)
    }

    /**
     * Returns the list of registered Bluetooth devices
     *
     * @return LiveData<MutableSet<BluetoothDevice>>
     * @see [LiveData] [MutableSet] [BluetoothDevice]
     */
    fun getBoxes(): LiveData<MutableSet<BluetoothDevice>> {
        if (!::boxes.isInitialized) {
            boxes = MutableLiveData()
            boxes.postValue(mutableSetOf())
        }
        return boxes
    }

}
