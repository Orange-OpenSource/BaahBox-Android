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

import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * To test [SensorDataSeries] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/08/2019
 * @version 2.0.0
 */
class TestSensorDataSeries {


    /**
     * Test the addRecord() method
     */
    @Test
    fun addRecord() {

        // Test if add operations are successful depending to size of series

        val records = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        var sensorDataSeries = SensorDataSeries(10, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(0, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(1, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(2, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        // Test the use of the swap (size of 2)

        sensorDataSeries = SensorDataSeries(10, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)

        sensorDataSeries.addRecord(10) // In swap
        assertTrue(sensorDataSeries.computeAverage() == 0)
        sensorDataSeries.addRecord(20) // In swap with 10
        assertTrue(sensorDataSeries.computeAverage() == 0)

        sensorDataSeries.addRecord(30) // Queue: 10, swap: 20, 30
        assertTrue(sensorDataSeries.computeAverage() == 10)
        sensorDataSeries.addRecord(40) // Queue: 10, 20, swap: 30, 40
        assertTrue(sensorDataSeries.computeAverage() == 15)

        sensorDataSeries.addRecord(50) // Queue: 10, 20, 30,  swap: 40, 50
        assertTrue(sensorDataSeries.computeAverage() == 20)
        sensorDataSeries.addRecord(60) // Queue: 10, 20, 30, 40, swap: 50, 60
        assertTrue(sensorDataSeries.computeAverage() == 25)

    }

    /**
     * Test the computeAverage() method
     */
    @Test
    fun computeAverage() {

        val records = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val recordsWithSwap = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8) // 9 and 10 in swap

        var sensorDataSeries = SensorDataSeries(0, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == recordsWithSwap.average().toInt())

        sensorDataSeries = SensorDataSeries(10, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == recordsWithSwap.average().toInt())

        sensorDataSeries = SensorDataSeries(3, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == 7)

        /*
        With a series of size 3, which embeds a swap of 2, at the end 9 and 10 will be swapped,
        and 6, 7, 8 stored.
        Thus average(6, 7, 8) = 7
        */

    }

    /**
     * Test the trendOfRecordedData() method
     */
    @Test
    fun trendOfRecordedData() {

        // Without parasites
        var sensorDataSeries = SensorDataSeries(20, 2, 5)
        var increasingTrend = mutableListOf(20, 40, 100, 200, 500, 600, 650, 680, 800, 810)
        increasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.INCREASE)

        sensorDataSeries = SensorDataSeries(20, 2, 5)
        var decreasingTrend = mutableListOf(1024, 1000, 900, 800, 700, 400, 50, 10, 1)
        decreasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.DECREASE)

        sensorDataSeries = SensorDataSeries(20, 2, 5)
        var equalTrend = mutableListOf(403, 400, 402, 400, 405, 400, 402)
        equalTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.EQUAL)

        // With parasites

        sensorDataSeries = SensorDataSeries(20, 2, 5)
        increasingTrend = mutableListOf(20, 40, 100, 1 /* <--- parasite */, 200, 500, 600, 650, 680, 800, 810)
        increasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.INCREASE)

        sensorDataSeries = SensorDataSeries(20, 2, 5)
        equalTrend = mutableListOf(403, 400, 2088 /* <--- parasite */, 402, 400, 405, 400, 402)
        equalTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.EQUAL)

        sensorDataSeries = SensorDataSeries(20, 2, 5)
        decreasingTrend = mutableListOf(800, 750, 700, 600, 620, 2088 /* <--- parasite */, 400, 300, 250, 200)
        decreasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.DECREASE)

    }

}