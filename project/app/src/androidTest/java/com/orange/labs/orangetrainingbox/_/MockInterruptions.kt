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

package com.orange.labs.orangetrainingbox.`_`

import android.content.Context
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import java.lang.Exception

// Enums and data classes

/**
 * Models the sensors of the Baah Box which can be mapped for example to the [TrainingBoxViewModel].
 */
enum class Sensor {

    // Sensors

    /**
     * One of the sensors of the Baah Box, seen a _sensorA_ in [TrainingBoxViewModel]
     */
    SENSOR_A,

    /**
     * One of the sensors of the Baah Box, seen a _sensorB_ in [TrainingBoxViewModel]
     */
    SENSOR_B,

    /**
     * Dedicated to the joystick
     */
    JOYSTICK,

    /**
     * Unknown sensor
     */
    UNKNOWN;

    // Utils

    companion object  {

        /**
         * Allows to give the matching [Sensor] with the given string
         * @param value A simple string used in mock files to identify the sensor
         * @return Sensor
         */
        fun fromString(value: String): Sensor {
            return when (value) {
                "A" -> SENSOR_A
                "B" -> SENSOR_B
                "J" -> JOYSTICK
                else -> UNKNOWN
            }
        }
    }

} // End of enum class Sensor

/**
 * Models a fake interruption with mock values
 *
 * @param sensor The sensor of the Baah Box (A or B or Joystick)
 * @param payload The value to broadcast
 */
data class MockInterruption(val sensor: Sensor, val payload: Int)

/**
 * Defines a bundle of interruptions which must be processed at the same time like if we have several
 * sensors signals sent together.
 *
 * @param sensorAInterruption Value of sensor A
 * @param sensorBInterruption Value of sensor B
 * @param joystickInterruption Value for the joystick
 */
data class MockInterruptionsFrame(val sensorAInterruption: MockInterruption?,
                                  val sensorBInterruption: MockInterruption?,
                                  val joystickInterruption: MockInterruption?)

// Utils

/**
 * Defines the symbol to use at the beginning of a line in mock files to ignore this line
 */
private const val MOCK_FILE_COMMENT_LINE = "#"

/**
 * The regular expression to follow for a mock frame.
 * For example:
<pre>
    A=100;B=42;J=666
</pre>
 */
private const val MOCK_FILE_LINE_FRAME_REGEX = "A=[0-9]+;B=[0-9]+;J=[0-9]+"

/**
 * The symbol to use to split a line to get sensor configuration
 */
private const val MOCK_FILE_SENSORS_SPLIT = ";"

/**
 * The symbol to use to split a sensor configuration so a to get its identifier an the value
 */
private const val MOCK_FILE_SENSOR_SPLIT = "="

/**
 * Reads from a text file the mock events to process
 * Empty and blank lines will be ignored.
 * Comments lines (i.e. starting with #) will be ignored.
 * Lines which do not match the pattern bellow will be ignored.
 *
 * Let's use the following example:
 *
 <pre>
    # Comment line, is ignore, and the line bellow will be ignored too

    A=100;B=200;J=300
    A=400;B=500;J=600
    A=0;B=700;J=0
 </pre>
 *
 * It will return a lit of 3 [MockInterruptionsFrame] and each frame as 3 [MockInterruption] objects.
 * The objects have the [Sensor] and its value.
 *
 * @param name The file name
 * @param context The context to use to load the mock file from assets
 * @return MutableList<MockInterruptionsFrame> The list of fake interruptions to process
 * @throws UnknownMockSensorException If something bad occurred with the mock file and the identifiers of sensors
 */
fun readMockEventsFromFile(name: String, context: Context) : MutableList<MockInterruptionsFrame> {

    val mockBleEvents = mutableListOf<MockInterruptionsFrame>()

    context.assets.open(name).bufferedReader().forEachLine cursor@{

        // Get the line
        val line = it.trim()

        // Ignore empty, nil or blank lines
        if (line.isBlank()) return@cursor

        // Ignore comments lines
        if (line.startsWith(MOCK_FILE_COMMENT_LINE)) return@cursor

        // Ignore lines which do not match regex
        val mockFrameRegex = MOCK_FILE_LINE_FRAME_REGEX.toRegex()
        if (!line.matches(mockFrameRegex)) return@cursor

        // Convert line to mock interruptions
        var sensorAInterruption: MockInterruption? = null
        var sensorBInterruption: MockInterruption? = null
        var joystickInterruption: MockInterruption? = null

        line.split(MOCK_FILE_SENSORS_SPLIT).forEach { lineSplit ->

            // Process each mock sensor interruption
            val sensorSplit = lineSplit.split(MOCK_FILE_SENSOR_SPLIT)
            val sensor = Sensor.fromString(sensorSplit[0])
            val sensorValue = sensorSplit[1].toInt()
            when (sensor) {
                Sensor.SENSOR_A -> sensorAInterruption = MockInterruption(sensor, sensorValue)
                Sensor.SENSOR_B -> sensorBInterruption = MockInterruption(sensor, sensorValue)
                Sensor.JOYSTICK -> joystickInterruption = MockInterruption(sensor, sensorValue)
                Sensor.UNKNOWN -> throw UnknownMockSensorException("${sensorSplit[0]} cannot be converted to Sensor")
            }

        }

        // Update the list of frames
        mockBleEvents.add(MockInterruptionsFrame(sensorAInterruption, sensorBInterruption, joystickInterruption))

    }

    return mockBleEvents
}

/**
 * Exception thrown if an error occurred with the mock files
 *
 * @param message The message to use for details
 */
data class UnknownMockSensorException(override val message: String): Exception(message)