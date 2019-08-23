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
 * @version 1.0.0
 */
class TestSensorDataSeries {


    /**
     * Test the addRecord() method
     */
    @Test
    fun addRecord() {

        val records = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        var sensorDataSeries = SensorDataSeries(10, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(0, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(1, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

        sensorDataSeries = SensorDataSeries(2, 2, 5)
        records.forEach { sensorDataSeries.addRecord(it) }

    }

    /**
     * Test the computeAverage() method
     */
    @Test
    fun computeAverage() {

        val records = mutableListOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)

        var sensorDataSeries = SensorDataSeries(0, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == records.average().toInt())

        sensorDataSeries = SensorDataSeries(10, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == 5) // average(1, 2, 3, 4, 5, 6, 7, 8, 9) = 45

        sensorDataSeries = SensorDataSeries(3, 2, 5)
        assertTrue(sensorDataSeries.computeAverage() == 0)
        records.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.computeAverage() == 9) // average (8, 9, 10) = 9

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

        var equalTrend = mutableListOf(350, 400, 350, 300, 310, 330, 380)
        equalTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.EQUAL)

        // With parasites

        sensorDataSeries = SensorDataSeries(20, 2, 5)

        increasingTrend = mutableListOf(20, 40, 100, 1 /* <--- parasite */, 200, 500, 600, 650, 680, 800, 810)
        increasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.INCREASE)

        sensorDataSeries = SensorDataSeries(20, 2, 5)

        decreasingTrend = mutableListOf(600, 500, 550, 400, 320, 300, 200, 1024 /* <--- parasite */, 200, 195, 5)
        decreasingTrend.forEach { sensorDataSeries.addRecord(it) }
        assertTrue(sensorDataSeries.trendOfRecordedData() == SensorTrends.DECREASE)

        sensorDataSeries = SensorDataSeries(20, 2, 100)

    }

}