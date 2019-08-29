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
package com.orange.labs.orangetrainingbox.utils.properties

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


/**
 * To test [PropertiesReader] class.
 * See [testing documentation](http://d.android.com/tools/testing).
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/08/2019
 * @version 1.2.0
 */

// TODO hard-coded value in test cases, need to sue properties load Context legacy function and comapre results to extension functions
@RunWith(AndroidJUnit4::class)
class TestPropertiesReader {

    // Needed to play with properties files
    private var appContext: Context? = null

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

    /**
     * Test the loastProperties() Context extension.
     */
    @Test(expected = java.io.FileNotFoundException::class)
    fun loadProperties() {

        val props = appContext!!.loadProperties()
        assertNotNull(props)
        appContext!!.loadProperties("foo-bar-wizz.properties")
        // Must throw java.io.FileNotFoundException:

    }

    /**
     * Test the readBleSensorsConfiguration() Context extension
     */
    @Test
    fun readBleSensorsConfiguration() {

        val bleConfiguration = appContext!!.readBleSensorsConfiguration()

        assertEquals(bleConfiguration.sensorCharDescriptorUUID, "00002902-0000-1000-8000-00805f9b34fb")
        assertEquals(bleConfiguration.sensorsCharUUID, "6e400003-b5a3-f393-e0a9-e50e24dcca9e")
        assertEquals(bleConfiguration.serviceUUID, "6E400001-B5A3-F393-E0A9-E50E24DCCA9E")

    }

    /**
     * Test the readAppGamesConfiguration() Context extension
     */
    @Test
    fun readAppGamesConfiguration() {

        val appGamesConfig = appContext!!.readAppGamesConfiguration()

        assertTrue(appGamesConfig.enableStarGame)
        assertTrue(appGamesConfig.enableBalloonGame)
        assertTrue(appGamesConfig.enableSheepGame)
        assertFalse(appGamesConfig.enableSpaceGame)
        assertFalse(appGamesConfig.enableToadGame)

    }

    /**
     * Test the readDifficultyDetailsConfiguration() Context extension
     */
    @Test
    fun readDifficultyDetailsConfiguration() {

        val difficultyDetailsConfig = appContext!!.readDifficultyDetailsConfiguration()

        assertTrue(difficultyDetailsConfig.difficultyFactorLow == 1.5)
        assertTrue(difficultyDetailsConfig.difficultyFactorMedium == 0.9)
        assertTrue(difficultyDetailsConfig.difficultyFactorHigh == 0.7)

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

        val balloonAdditionalConfig = appContext!!.readBalloonAdditionalConfiguration()

        assertTrue(balloonAdditionalConfig == 600L)

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
        val defaultSpeed = appContext!!.loadProperties().getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_SPEED_VALUE.key)

        val sheepDefaultGameConfiguration = appContext!!.readSheepDefaultConfiguration()

        assertTrue(defaultFencesNumber == sheepDefaultGameConfiguration.defaultFencesCount)
        assertTrue(defaultSpeed == sheepDefaultGameConfiguration.defaultSpeed)

    }

    /**
     * Test the readSensorDataSeriesConfiguration() Context extension
     */
    @Test
    fun readSensorDataSeriesConfiguration() {

        val sensorDataSeriesConfig = appContext!!.readSensorDataSeriesConfiguration()

        assertTrue(sensorDataSeriesConfig.queueSize == 15)
        assertTrue(sensorDataSeriesConfig.intervalForUpdate == 5)
        assertTrue(sensorDataSeriesConfig.trendThreshold == 5)

    }

}
