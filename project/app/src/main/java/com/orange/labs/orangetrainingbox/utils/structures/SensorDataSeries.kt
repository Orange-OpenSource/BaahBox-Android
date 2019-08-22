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

import android.annotation.SuppressLint
import com.orange.labs.orangetrainingbox.utils.structures.Queue
import com.orange.labs.orangetrainingbox.utils.structures.average


// **********************
// Compile-time constants
// **********************

/**
 * All INTERVAL_FOR_UPDATEth items, compute a new average and store it.
 */
private const val INTERVAL_FOR_UPDATE: Int = 20

/**
 * The trend threshold defining if trend is increasing, freezing or decreasing
 */
private const val TREND_THRESHOLD: Int = 20


// *******
// Classes
// *******

/**
 * Class containing a series of sensor data stord in a [Queue], and providing methods for averages and trends.
 *
 * @param historySize Higher is this value, bigger is the number of sensor data records
 *
 * @author Pierre-Yves Lapersonne
 * @since 22/08/2019
 * @version 1.0.0
 */
class SensorDataSeries(val historySize: Int) {


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
    private var countDownForAverage = INTERVAL_FOR_UPDATE


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
            countDownForAverage = INTERVAL_FOR_UPDATE
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
     *
     * <b>Considering here sensor records are integers, where the lowest value is 1 and the highest value
     * is 1002 (i.e. full contraction / work of muscles / ...)</b>
     *
     * <b>Arbitrary decided for now to consider a variation in [-100 ; 100] is not enough significant.</b>
     *
     * @return SensorTrends
     */
    fun trendOfRecordedData(): SensorTrends {
        return when (computeAverage() - lastComputedAverage) {
            // FIXME Hard-coded value! Should be in .properties config file
            in Int.MIN_VALUE..-TREND_THRESHOLD -> SensorTrends.DECREASE
            in -TREND_THRESHOLD..TREND_THRESHOLD -> SensorTrends.EQUAL
            in TREND_THRESHOLD..Int.MAX_VALUE -> SensorTrends.INCREASE
            else -> SensorTrends.EQUAL
        }
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