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

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.lang.IllegalArgumentException

/**
 * To test [Context] extensions to use for properties loading.
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * @since 23/08/2019
 * @version 2.2.0
 */
@RunWith(AndroidJUnit4ClassRunner::class)
class InstrumentedTestPropertiesReader {

    // Properties

    /**
     * Needed to play with properties files
     */
    private var appContext: Context? = null

    // Configuration

    /**
     * Get context from device
     */
    @Before
    fun setUp(){
        appContext = InstrumentationRegistry.getTargetContext()
    }

    /**
     * Deletes context
     */
    @After
    fun tearDown(){
        appContext = null
    }

    // Tests

    /**
     * Test the loadProperties() Context extension.
     */
    @Test
    fun loadProperties() {
        val props = appContext!!.loadProperties()
        assertNotNull(props)
    }

    /**
     * The loadProperties() method must throw a [java.io.FileNotFoundException] if the properties
     * file name does not point to anything existing.
     */
    @Test (expected=java.io.FileNotFoundException::class)
    fun loadPropertiesWithBadFileName() {
        appContext!!.loadProperties("foo-bar-wizz.properties")
    }

    /**
     * Test the readBleSensorsConfiguration() Context extension
     */
    @Test
    fun readBleSensorsConfiguration() {

        val properties = appContext!!.loadProperties()
        val expectedDescriptor = properties.getProperty(PropertiesKeys.BLE_CHAR_DESCRIPTOR_UUID.key)
        val uuid = properties.getProperty(PropertiesKeys.BLE_SENSORS_CHAR_UUID.key)
        val service = properties.getProperty(PropertiesKeys.BLE_SERVICE_UUID.key)

        val bleConfiguration = appContext!!.readBleSensorsConfiguration()

        assertEquals(expectedDescriptor, bleConfiguration.sensorCharDescriptorUUID)
        assertEquals(uuid, bleConfiguration.sensorsCharUUID)
        assertEquals(service, bleConfiguration.serviceUUID)

    }

    /**
     * Test the readAppGamesConfiguration() Context extension
     */
    @Test
    fun readAppGamesConfiguration() {

        val properties = appContext!!.loadProperties()
        val enableStarGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_STAR.key).toBoolean()
        val enableBalloonGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_BALLOON.key).toBoolean()
        val enableSheepGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_SHEEP.key).toBoolean()
        val enableSpaceGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_SPACE.key).toBoolean()
        val enableToadGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_TOAD.key).toBoolean()

        val appGamesConfig = appContext!!.readAppGamesConfiguration()

        assertEquals(enableStarGame, appGamesConfig.enableStarGame)
        assertEquals(enableBalloonGame, appGamesConfig.enableBalloonGame)
        assertEquals(enableSheepGame, appGamesConfig.enableSheepGame)
        assertEquals(enableSpaceGame, appGamesConfig.enableSpaceGame)
        assertEquals(enableToadGame, appGamesConfig.enableToadGame)

    }

    /**
     * Test the readDifficultyDetailsConfiguration() Context extension
     */
    @Test
    fun readDifficultyDetailsConfiguration() {

        val factors = appContext!!.loadProperties()
            .getProperty(PropertiesKeys.DIFFICULTY_NUMERIC_VALUES.key).split(";")
        val expectedLow = factors[0].toDouble()
        val expectedMedium = factors[1].toDouble()
        val expectedHigh = factors[2].toDouble()

        val difficultyDetailsConfig = appContext!!.readDifficultyDetailsConfiguration()

        assertTrue(expectedLow == difficultyDetailsConfig.difficultyFactorLow)
        assertTrue(expectedMedium == difficultyDetailsConfig.difficultyFactorMedium)
        assertTrue(expectedHigh == difficultyDetailsConfig.difficultyFactorHigh)

    }

    /**
     * Test the readStarGameConfiguration() Context extension
     */
    @Test
    fun readStarGameConfiguration() {

        // To compute expected values
        val definedPropertiesRaw = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_STAR_THRESHOLD.key)
        val definedPropertiesSplit = definedPropertiesRaw.split(";")

        // Get values to test
        val starGamesConfiguration = appContext!!.readStarGameConfiguration()
        assertTrue(starGamesConfiguration.minThreshold1 == definedPropertiesSplit[0].toInt())
        assertTrue(starGamesConfiguration.maxThreshold1 == (definedPropertiesSplit[1].toInt() - 1))
        assertTrue(starGamesConfiguration.minThreshold2 == definedPropertiesSplit[1].toInt())
        assertTrue(starGamesConfiguration.maxThreshold2 == (definedPropertiesSplit[2].toInt() - 1))
        assertTrue(starGamesConfiguration.minThreshold3 == definedPropertiesSplit[2].toInt())
        assertTrue(starGamesConfiguration.maxThreshold3 == definedPropertiesSplit[3].toInt())

    }

    /**
     * Test the readBalloonGameConfiguration() Context extension
     */
    @Test
    fun readBalloonGameConfiguration() {

        // To compute expected values
        val definedPropertiesRaw = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_BALLOON_THRESHOLD.key)
        val definedPropertiesSplit = definedPropertiesRaw.split(";")

        // Get values to test
        val balloonGamesConfiguration = appContext!!.readBalloonGameConfiguration()
        assertTrue(balloonGamesConfiguration.minThreshold1 == definedPropertiesSplit[0].toInt())
        assertTrue(balloonGamesConfiguration.maxThreshold1 == (definedPropertiesSplit[1].toInt() - 1))
        assertTrue(balloonGamesConfiguration.minThreshold2 == definedPropertiesSplit[1].toInt())
        assertTrue(balloonGamesConfiguration.maxThreshold2 == (definedPropertiesSplit[2].toInt() - 1))
        assertTrue(balloonGamesConfiguration.minThreshold3 == definedPropertiesSplit[2].toInt())
        assertTrue(balloonGamesConfiguration.maxThreshold3 == (definedPropertiesSplit[3].toInt() - 1))
        assertTrue(balloonGamesConfiguration.minThreshold4 == definedPropertiesSplit[3].toInt())
        assertTrue(balloonGamesConfiguration.maxThreshold4 == definedPropertiesSplit[4].toInt())

    }

    /**
     * Test the readBalloonAdditionalConfiguration() Context extension
     */
    @Test
    fun readBalloonAdditionalConfiguration() {
        val expectedConfig = appContext!!.loadProperties()
            .getProperty(PropertiesKeys.GAME_BALLOON_INTRODUCTION_ANIMATION_PERIOD.key).toLong()
        val balloonAdditionalConfig = appContext!!.readBalloonAdditionalConfiguration()
        assertTrue(expectedConfig == balloonAdditionalConfig)
    }

    /**
     * Test the readSheepGameConfiguration() Context extension
     */
    @Test
    fun readSheepGameConfiguration() {

        val gameSheepMoveOffset = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_MOVE_OFFSET.key).toInt()
        val gameSheepDuration = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_MOVE_DURATION.key).toLong()
        val gameSheepAnimationPeriod = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_INTRODUCTION_ANIMATION_PERIOD.key).toLong()

        val sheepGamesConfiguration = appContext!!.readSheepGameConfiguration()

        assertTrue(gameSheepMoveOffset == sheepGamesConfiguration.moveOffset)
        assertTrue(gameSheepDuration == sheepGamesConfiguration.moveDuration)
        assertTrue(gameSheepAnimationPeriod == sheepGamesConfiguration.walkAnimationPeriod)

    }

    /**
     * Test the readSheepDefaultConfiguration() Context extension
     */
    @Test
    fun readSheepDefaultConfiguration() {

        val defaultFencesNumber = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_FENCES_NUMBER.key).toInt()
        val defaultMaxNumber = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_MAX_FENCES_NUMBER.key).toInt()
        val defaultSpeed = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_SPEED_VALUE.key).toLong()
        val speedFactors = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_SPEED_FACTOR.key).split(";")

        val sheepDefaultGameConfiguration = appContext!!.readSheepDefaultConfiguration()

        assertTrue(defaultFencesNumber == sheepDefaultGameConfiguration.defaultFencesCount)
        assertTrue(defaultMaxNumber == sheepDefaultGameConfiguration.defaultMaxFencesCount)
        assertTrue(defaultSpeed == sheepDefaultGameConfiguration.defaultSpeed)
        assertTrue(sheepDefaultGameConfiguration.speedFactors.first == speedFactors[0].toFloat())
        assertTrue(sheepDefaultGameConfiguration.speedFactors.second == speedFactors[1].toFloat())
        assertTrue(sheepDefaultGameConfiguration.speedFactors.third == speedFactors[2].toFloat())

    }

    /**
     * Test the readSensorDataSeriesConfiguration() Context extension
     */
    @Test
    fun readSensorDataSeriesConfiguration() {

        val properties = appContext!!.loadProperties()
        val expectedQueueSize = properties.getProperty(PropertiesKeys.SENSOR_DATA_SERIES_QUEUE_SIZE.key).toInt()
        val expectedInterval = properties.getProperty(PropertiesKeys.SENSOR_DATA_SERIES_INTERVAL_FOR_UPDATE.key).toInt()
        val expectedThreshold = properties.getProperty(PropertiesKeys.SENSOR_DATA_SERIES_TREND_THRESHOLD.key).toInt()

        val sensorDataSeriesConfig = appContext!!.readSensorDataSeriesConfiguration()

        assertTrue(expectedQueueSize == sensorDataSeriesConfig.queueSize)
        assertTrue(expectedInterval == sensorDataSeriesConfig.intervalForUpdate)
        assertTrue(expectedThreshold == sensorDataSeriesConfig.trendThreshold)

    }

    /**
     * Test the isDemoFeatureEnabled() Context extension
     */
    @Test
    fun isDemoModeActivated() {
        val expectedFlag = appContext!!.loadProperties().getProperty(PropertiesKeys.ENABLE_DEMO_FEATURE.key).toBoolean()
        val config = appContext!!.isDemoFeatureEnabled()
        assertEquals(expectedFlag, config)
    }

    /**
     * Test the readCollisionDetectionInterval() Context extension
     */
    @Test
    fun readCollisionDetectionInterval() {
        val expectedInterval = appContext!!.loadProperties().getProperty(PropertiesKeys.COLLISION_DETECTION_INTERVAL.key).toLong()
        val config = appContext!!.readCollisionDetectionInterval()
        assertTrue(expectedInterval == config)
    }

}
