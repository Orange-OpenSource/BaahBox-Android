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
package com.orange.labs.orangetrainingbox.utils.properties

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * To test configuration elements for games.
 *
 * @since 23/08/2019
 * @version 1.1.0
 */
class UnitTestPropertiesReader {

    // *********
    // Constants
    // *********

    /**
     * Test the [DEFAULT_FILENAME] constant
     */
    @Test
    fun `default file name should be equal to`() {
        assertEquals("app_configuration.properties", DEFAULT_FILENAME)
    }

    /**
     * Test the [DELIMITER] constant
     */
    @Test
    fun `delimiter should be equal to`() {
        assertEquals(";", DELIMITER)
    }

    // *******************
    // Game configurations
    // *******************

    /**
     *
     */
    @Test
    fun `ble configuration can have empty service UUID`() {
        assertNotNull(BleConfiguration("", "foo", "bar"))
    }

    /**
     *
     */
    @Test
    fun `ble configuration can have empty sensor char UUID`() {
        assertNotNull(BleConfiguration("foo", "", "bar"))
    }

    /**
     *
     */
    @Test
    fun `ble configuration can have empty descriptor`() {
        assertNotNull(BleConfiguration("foo", "bar", ""))
    }

    /**
     *
     */
    @Test
    fun `app games configuration can have all flags to false`() {
        assertNotNull(AppGamesConfiguration(
            enableStarGame = false,
            enableBalloonGame = false,
            enableSheepGame = false,
            enableSpaceGame = false,
            enableToadGame = false
        ))
    }

    /**
     *
     */
    @Test
    fun `app games configuration can have all flags to true`() {
        assertNotNull(AppGamesConfiguration(
            enableStarGame = true,
            enableBalloonGame = true,
            enableSheepGame = true,
            enableSpaceGame = true,
            enableToadGame = true
        ))
    }

    /**
     *
     */
    @Test
    fun `difficulty details configuration can have negative factors`() {
        assertNotNull(DifficultyDetailsConfiguration(
            -1.0,
            -1.0,
            -1.0))
    }

    /**
     *
     */
    @Test
    fun `difficulty details configuration can have null factors`() {
        assertNotNull(DifficultyDetailsConfiguration(
            0.0,
            0.0,
            0.0))
    }

    /**
     *
     */
    @Test
    fun `difficulty details configuration can have positive factors`() {
        assertNotNull(DifficultyDetailsConfiguration(
            1.0,
            1.0,
            1.0))
    }

    /**
     *
     */
    @Test
    fun `star game can have negative thresholds`() {
        assertNotNull(StarGameConfiguration(
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
        ))
    }

    /**
     *
     */
    @Test
    fun `star game can have null thresholds`() {
        assertNotNull(StarGameConfiguration(
            0,
            0,
            0,
            0,
            0,
            0
        ))
    }

    /**
     *
     */
    @Test
    fun `star game can have positive thresholds`() {
        assertNotNull(StarGameConfiguration(
            1,
            1,
            1,
            1,
            1,
            1
        ))
    }

    @Test
    fun `balloon game can have negative thresholds`() {
        assertNotNull(BalloonGameConfiguration(
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1,
            -1
        ))
    }

    /**
     *
     */
    @Test
    fun `balloon game can have null thresholds`() {
        assertNotNull(BalloonGameConfiguration(
            0,
            0,
            0,
            0,
            0,
            0,
            0,
            0
        ))
    }

    /**
     *
     */
    @Test
    fun `balloon game can have positive thresholds`() {
        assertNotNull(BalloonGameConfiguration(
            1,
            1,
            1,
            1,
            1,
            1,
            1,
            1
        ))
    }

    /**
     *
     */
    @Test
    fun `sheep game configuration can have negative values`() {
        assertNotNull(SheepGameConfiguration(-1, -1, -1))
    }

    /**
     *
     */
    @Test
    fun `sheep game configuration can have null values`() {
        assertNotNull(SheepGameConfiguration(0, 0, 0))
    }

    /**
     *
     */
    @Test
    fun `sheep game configuration can have positive values`() {
        assertNotNull(SheepGameConfiguration(1, 1, 1))
    }

    /**
     *
     */
    @Test
    fun `sheep game default configuration can have negative values`() {
        assertNotNull(SheepGameDefaultConfiguration(
            -1,
            -1,
            -1,
            Triple(-1f, -1f, -1f)
        ))
    }

    /**
     *
     */
    @Test
    fun `sheep game default configuration can have null values`() {
        assertNotNull(SheepGameDefaultConfiguration(
            0,
            0,
            0,
            Triple(0f, 0f, 0f)))
    }

    /**
     *
     */
    @Test
    fun `sheep game default configuration can have positive values`() {
        assertNotNull(SheepGameDefaultConfiguration(
            1,
            1,
            1,
            Triple(1f, 1f, 1f)))
    }

    /**
     *
     */
    @Test
    fun `sheep game default configuration can have factors between 0 and 1`() {
        assertNotNull(SheepGameDefaultConfiguration(
            1,
            1,
            1,
            Triple(0.42f, 0.666f, 0.00001f)))
    }

    /**
     *
     */
    @Test
    fun `sheep game default configuration can have factors greater than 1`() {
        assertNotNull(SheepGameDefaultConfiguration(
            1,
            1,
            1,
            Triple(Float.MAX_VALUE, 42f, 123456789f)))
    }

    /**
     *
     */
    @Test
    fun `sensor data series configuration can have negative factors`() {
        assertNotNull(SensorDataSeriesConfiguration(
            -1,
            -1,
            -1))
    }

    /**
     *
     */
    @Test
    fun `sensor data series configuration can have null factors`() {
        assertNotNull(SensorDataSeriesConfiguration(
            0,
            0,
            0))
    }

    /**
     *
     */
    @Test
    fun `sensor data series configuration can have positive factors`() {
        assertNotNull(SensorDataSeriesConfiguration(
            1,
            1,
            1))
    }

    // *******************
    // BLE frames strategy
    // *******************

    /**
     *
     */
    @Test
    fun `valueOf string should return instances of BleFrameStrategy`() {
        assertEquals(BleFrameStrategy.MOCK, BleFrameStrategy.convertStringToValue("mock"))
        assertEquals(BleFrameStrategy.BOX, BleFrameStrategy.convertStringToValue("box"))
    }

    /**
     *
     */
    @Test
    fun `valueOf should return an UNDEFINED value if no string matches`() {
        assertEquals(BleFrameStrategy.UNDEFINED, BleFrameStrategy.convertStringToValue(""))
        assertEquals(BleFrameStrategy.UNDEFINED, BleFrameStrategy.convertStringToValue("."))
        assertEquals(BleFrameStrategy.UNDEFINED, BleFrameStrategy.convertStringToValue("FOO-BAR-WIZZ"))
    }

}