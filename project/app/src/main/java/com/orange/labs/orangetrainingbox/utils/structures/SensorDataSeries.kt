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
package com.orange.labs.orangetrainingbox.utils.structures

import android.annotation.SuppressLint


// *******
// Classes
// *******

/**
 * Class containing a series of sensor data stord in a [Queue], and providing methods for averages and trends.
 *
 * @param historySize Higher is this value, bigger is the number of sensor data records
 * @param intervalForUpdate All INTERVAL_FOR_UPDATEth items, compute a new average and store it.
 * @param trendThreshold The trend threshold defining if trend is increasing, freezing or decreasing
 *
 * @author Pierre-Yves Lapersonne
 * @since 22/08/2019
 * @version 1.0.0
 */
class SensorDataSeries(private val historySize: Int,
                       private val intervalForUpdate: Int,
                       private val trendThreshold: Int) {


    // **********
    // Properties
    // **********

    /**
     * Sensor data are stored in a FIFO structure
     */
    private val sensorDataQueue: Queue<Int> = Queue(historySize)

    /**
     * The last computed average which will be compared to a newer value so as to define
     * trends
     */
    private var lastComputedAverage: Int = 0

    /**
     * Countdown when, if equal to 0, the last computed average will be updated
     */
    private var countDownForAverage = intervalForUpdate


    // *******
    // Methods
    // *******

    /**
     * Adds a new record of sensor data in the queue.
     * The [Queue] object will do the job of checking the size and cleaning.
     *
     * @param record The new record to record      ┬─┬﻿ ︵ /(.□. \）
     */
    @SuppressLint("ByteOrderMark")
    fun addRecord(record: Int) {
        countDownForAverage--
        if (countDownForAverage <= 0) {
            lastComputedAverage = computeAverage()
            countDownForAverage = intervalForUpdate
        }
        sensorDataQueue.enqueue(record)
    }

    /**
     * Returns the average of all recorded sensor values
     *
     * @return Int
     */
    fun computeAverage(): Int {
        return sensorDataQueue.average<Int>()
    }

    /**
     * The current trend for the current recorded sensor data.
     * Computes the difference between the current average and the last computed.
     * If the result is negligible, will consider a non-moving trend.
     * If the result is significant, will consider an increasing or a decreasing trend.
     * Removes previously parasites.
     *
     * <b>Considering here sensor records are integers, where the lowest value is 1 and the highest value
     * is 1002 (i.e. full contraction / work of muscles / ...)</b>
     *
     * <b>Arbitrary decided for now to consider a variation in [-100 ; 100] is not enough significant.</b>
     *
     * @return SensorTrends
     */
    fun trendOfRecordedData(): SensorTrends {
        removeParasites()
        return when (computeAverage() - lastComputedAverage) {
            in Int.MIN_VALUE..-trendThreshold -> SensorTrends.DECREASE
            in -trendThreshold..trendThreshold -> SensorTrends.EQUAL
            in trendThreshold..Int.MAX_VALUE -> SensorTrends.INCREASE
            else -> SensorTrends.EQUAL
        }
    }

    /**
     * Cleans the series of records so as to remove parasites.
     * Indeed with Baah box Arduino firmware 2.0.0 (https://github.com/Orange-OpenSource/BaahBox-Arduino/releases/tag/v2.0.0)
     * some frames are broadcast with input data even if nothing has been done on the sensors.
     * For example when a slider is plugged to the box, the box sends frames with non-zero data even if the slider has not been touched.
     * Those values are polluting the series and finally the average computation.
     */
    private fun removeParasites() {

        if (sensorDataQueue.isEmpty()) return

        // First case: inputs greater than 1024 which must be the highest value
        // E.g: 10, 20, 10, 30, 2022, 15
        sensorDataQueue.elements.removeAll { it > 1024 }

        // Other case: too high inputs between smaller ones
        // E.g.: 10, 20, 5, 90, 5, 10, 20     <---- 90
        // E.g.: 10, 20, 5, 800, 5, 10, 20    <---- 800
        // For this case, compare the two biggest values. If one is far more great than the other, remove it.
        val notFifoAnymore /* grumph */  = sensorDataQueue.elements.toMutableList()
        notFifoAnymore.sort()
        val count = notFifoAnymore.count()
        if (count <= 2) return

        val max1 = notFifoAnymore[count - 1]
        val max2 = notFifoAnymore[count - 2]

        if (max1 > max2 * 5) sensorDataQueue.elements.remove(max1)

    }

}


// *****
// Enums
// *****

/**
 * Enumeration modeling trends about the computed metrics using sensor data records
 *
 * @version 1.0.0
 */
enum class SensorTrends {

    /**
     * The metrics computed from sensor data records are going higher
     */
    INCREASE,

    /**
     * The metrics computed from sensor data are remaining almost the same
     */
    EQUAL,

    /**
     * The metrics computed from sensor data are going lower
     */
    DECREASE

}