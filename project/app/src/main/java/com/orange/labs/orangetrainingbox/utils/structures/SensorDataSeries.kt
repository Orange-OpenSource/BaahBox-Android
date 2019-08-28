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

import android.annotation.SuppressLint // YOLO
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import kotlin.math.absoluteValue


// **********************
// Compile-time constants
// **********************

/**
 * A factor for parasite computations.
 * Once the data is enqueued, the 2 first highest values will be defined.
 * If the highest of the 2 is FACTOR time greater than the another, thus this value is considered as a parasite.
 * Indeed, the BaahBox sends data like 2022, 1024 or other too-high value. Need to find them because they
 * have an effect on trends and averages (muscles contractions, decontractions, ...)
 */
private const val PARASITE_MAX_FACTOR = 10

/**
 * Sometimes too-high records are not parasites, but a start of a powerful move.
 * Indeed when the user makes a big muscle contraction, or a big release, very high value can occur but are not parasites.
 * So as to check if we are in this case, a difference is made between values in the swap.
 * If the absolute value fo the result is lower than this constant, we are in a powerful move
 */
private const val POWERFUL_MOVE_DIFF = 200


// *******
// Classes
// *******

/**
 * Class containing a series of sensor data stored in a [Queue], and providing methods for averages and trends.
 * It contains also a swap for data stored before being added in the queue.
 * This structure allows to check if data are parasites, powerful move or simple move.
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
     * Sensor data are stored in a FIFO structure so as to deal with order of values
     */
    private val sensorDataQueue: Queue<Int> = Queue(historySize)

    /**
     * The swap is a kind of waiting queue for values before adding them in the queue.
     * It allows to catch parasite values and check if such high values are not for powerful move.
     * Indeed, a parasite in a site is like [10, 12, 12, 15, 800, 12, 15].
     * A powerful move (big contraction or big release of muscle) is more like [10, 12, 12, 15, 800, 810, 850]
     */
    private val swap: Array<Int> = arrayOf(-1, -1)

    /**
     * Check in the swap if a parasite value has been stored.
     * If the swap is not empty (no -1 stored), check if the 1st value is far higher (using PARASITE_MAX_FACTOR)
     * than the 2nd. In this case returns true, otherwise false.
     *
     * <b>Please note that this code works for Arduino firmware v2.0.0 which sends bad values</b>
     */
    private val wasParasite: () -> Boolean = {
        (swap[0] != -1 && swap[1] != -1) && (
                (swap[0] > swap[1] * PARASITE_MAX_FACTOR)
                ||
                (swap[0] > 1024)
        )
    }

    /**
     * Check in the swap if the values stored, which can be higher than these in the queue, can be considered
     * as a powerful move.
     * Indeed sometimes pic values, i.e. very bigger or lower than the others, can be enqueued and be seen as parasite.
     * But if the user makes a powerful move (a big contraction of its muscle or a big release), we will get
     * pic values which must not be seen as parasites.
     */
    private val isPowerfulMove: () -> Boolean = {
        (swap[0] != -1 && swap[1] != -1) && ((swap[0] - swap[1]).absoluteValue < POWERFUL_MOVE_DIFF)
    }

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
            Logger.d("Sensor data series - last computed average $lastComputedAverage")
        }

        when {

            // Swap never used until now
            swap[0] == -1 -> swap[0] = record

            // Only one value has been added in the swap, fill its other cell of the swap
            swap[1] == -1 -> swap[1] = record

            // Swap is full, need to move values
            else -> {

                // Woops, parasite value, delete it
                if (wasParasite()){
                    swap[0] = swap[1]
                    swap[1] = record
                } else {
                    sensorDataQueue.enqueue(swap[0])
                    swap[0] = swap[1]
                    swap[1] = record
                }

            }

        }

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
        return when (computeAverage() - lastComputedAverage) {
            in Int.MIN_VALUE..-trendThreshold -> SensorTrends.DECREASE
            in -trendThreshold..trendThreshold -> SensorTrends.EQUAL
            in trendThreshold..Int.MAX_VALUE -> SensorTrends.INCREASE
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