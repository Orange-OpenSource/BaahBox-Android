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
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import java.util.*

/**
 * Extension functions to use so as to parse a configuration file in order to define features of configuration elements for the app.
 *
 * @author Pierre-Yves Lapersonne
 * @author Marc Poppleton
 * @since 20/05/2019
 * @version 2.2.0
 */


private const val filename = "app_configuration.properties"
private const val delimiter = ";"
private const val difficulty_low = "low"
private const val difficulty_medium = "medium"
private const val difficulty_high = "high"

/**
 * Load the properties in the reader
 *
 * @return Properties The read data
 */
private fun Context.loadProperties(): Properties {
    val assetManager = this.assets
    val inputStream = assetManager.open(filename)
    val properties = Properties()
    properties.load(inputStream)
    return properties
}

/**
 * Reads from properties file in assets configuration elements for BLE things
 *
 * @return BleConfiguration
 */
fun Context.readBleSensorsConfiguration(): BleConfiguration {
    val properties = loadProperties()
    val serviceUUID = properties.getProperty(PropertiesKeys.BLE_SERVICE_UUID.key)
    val sensorsCharUUID = properties.getProperty(PropertiesKeys.BLE_SENSORS_CHAR_UUID.key)
    val sensorCharDescriptorUUID = properties.getProperty(PropertiesKeys.BLE_CHAR_DESCRIPTOR_UUID.key)
    return BleConfiguration(serviceUUID, sensorsCharUUID, sensorCharDescriptorUUID)
}

/**
 * Reads from properties file in assets flags to use so as to know if a g   me can be enabled / added in the app
 *
 * @return BleConfiguration
 */
fun Context.readAppGamesConfiguration(): AppGamesConfiguration {
    val properties = loadProperties()
    val enableStarGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_STAR.key)?.toBoolean() ?: false
    val enableBalloonGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_BALLOON.key)?.toBoolean() ?: false
    val enableSheepGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_SHEEP.key)?.toBoolean() ?: false
    val enableSpaceGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_SPACE.key)?.toBoolean() ?: false
    val enableToadGame = properties.getProperty(PropertiesKeys.ENABLE_GAME_TOAD.key)?.toBoolean() ?: false
    return AppGamesConfiguration(enableStarGame, enableBalloonGame,enableSheepGame, enableSpaceGame, enableToadGame)
}

/**
 * Reads from properties file assets.
 * If the entry for "difficulty_factor" is not defined or does not match "low", "medium" or "high", will be defined
 * to DifficultyFactor.MEDIUM.
 *
 * @return GameDetailsConfiguration The configuration
 */
fun Context.readGameDetailsConfiguration(): GameDetailsConfiguration {
    val properties = loadProperties()
    val rawDifficultyFactor = properties.getProperty(PropertiesKeys.DIFFICULTY_FACTOR.key)
    val difficultyFactor = when (rawDifficultyFactor.toLowerCase()) {
        difficulty_low -> DifficultyFactor.LOW
        difficulty_medium -> DifficultyFactor.MEDIUM
        difficulty_high -> DifficultyFactor.HIGH
        else -> DifficultyFactor.MEDIUM
    }
    return GameDetailsConfiguration(difficultyFactor)
}


/**
 * Reads from properties file assets configuration elements for the star game.
 *
 * @return DifficultyDetailsConfiguration The configuration
 */
fun Context.readDifficultyDetailsConfiguration(): DifficultyDetailsConfiguration {
    val properties = loadProperties()
    val difficultyConfiguration = properties.getProperty(PropertiesKeys.DIFFICULTY_NUMERIC_VALUES.key)
    val configurationItems = difficultyConfiguration.split(delimiter)
    if (configurationItems.size != 3) throw InvalidConfigurationException("Found ${configurationItems.size} values, expected 3")
    return DifficultyDetailsConfiguration(
        configurationItems[0].toDouble(), configurationItems[1].toDouble(),
        configurationItems[2].toDouble()
    )
}

/**
 * Reads from properties file assets configuration elements for the star game.
 *
 * @return StarGameConfiguration The configuration
 */
fun Context.readStarGameConfiguration(): StarGameConfiguration {
    val properties = loadProperties()
    val starGameRawConfiguration = properties.getProperty(PropertiesKeys.GAME_STAR_THRESHOLD.key)
    val configurationItems = starGameRawConfiguration.split(delimiter)
    if (configurationItems.size != 4) throw InvalidConfigurationException("Found ${configurationItems.size} values, expected 4")
    return StarGameConfiguration(
        configurationItems[0].toInt(), configurationItems[1].toInt() - 1,
        configurationItems[1].toInt(), configurationItems[2].toInt() - 1,
        configurationItems[2].toInt(), configurationItems[3].toInt()
    )
}

/**
 * Reads from properties file assets configuration elements for the balloon game.
 *
 * @return BalloonGameConfiguration The configuration
 */
fun Context.readBalloonGameConfiguration(): BalloonGameConfiguration {
    val properties = loadProperties()
    val starGameRawConfiguration = properties.getProperty(PropertiesKeys.GAME_BALLOON_THRESHOLD.key)
    val configurationItems = starGameRawConfiguration.split(delimiter)
    if (configurationItems.size != 5) throw InvalidConfigurationException("Found ${configurationItems.size} values, expected 5")
    return BalloonGameConfiguration(
        configurationItems[0].toInt(), configurationItems[1].toInt() - 1,
        configurationItems[1].toInt(), configurationItems[2].toInt() - 1,
        configurationItems[2].toInt(), configurationItems[3].toInt() - 1,
        configurationItems[3].toInt(), configurationItems[4].toInt()
    )
}

/**
 * Reads from properties file assets additional configuration elements for the balloon game.
 *
 * @return Long Just the period for animations of the game icon in the introduction screen
 */
fun Context.readBalloonAdditionalConfiguration(): Long {
    val properties = loadProperties()
    return properties.getProperty(PropertiesKeys.GAME_BALLOON_INTRODUCTION_ANIMATION_PERIOD.key).toLong()
}

/**
 * Reads from properties file assets configuration elements for the sheep game.
 * Return configuration details with:
 *
 <ul>
    <li>The offset to use for each sheep move</li>
    <li>The period to use to animate the sheep (in walking mode, for introduction screen...)</li>
    <li>The duration for sheep move animation</li>
 </ul>
 *
 * @return SheepGameConfiguration The configuration
 */
fun Context.readSheepGameConfiguration(): SheepGameConfiguration {
    val properties = loadProperties()
    val sheepMoveOffset = properties.getProperty(PropertiesKeys.GAME_SHEEP_MOVE_OFFSET.key).toInt()
    if (sheepMoveOffset <= 0) throw InvalidConfigurationException("Sheep move offset value must be an integer greater than 0")
    val animationPeriod = properties.getProperty(PropertiesKeys.GAME_SHEEP_INTRODUCTION_ANIMATION_PERIOD.key).toLong()
    if (animationPeriod <= 0) throw InvalidConfigurationException("Sheep animation period value must be a long greater than 0")
    val moveDuration = properties.getProperty(PropertiesKeys.GAME_SHEEP_MOVE_DURATION.key).toLong()
    if (moveDuration <= 0) throw InvalidConfigurationException("Sheep move duration value must be a long greater than 0")
    return SheepGameConfiguration(0, animationPeriod, moveDuration)
}

/**
 * Reads from properties file assets additional configuration elements for the sheep game.
 *
 * @return SheepGameDefaultConfiguration A bundle with default fences number and speed
 */
fun Context.readSheepDefaultConfiguration(): SheepGameDefaultConfiguration {
    val properties = loadProperties()
    val defaultFencesNumber = properties.getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_FENCES_NUMBER.key).toInt()
    val defaultSpeed = properties.getProperty(PropertiesKeys.GAME_SHEEP_DEFAULT_SPEED_VALUE.key)
    return SheepGameDefaultConfiguration(defaultFencesNumber, defaultSpeed)
}

/**
 * Reads from properties file assets configuration details for recorded sensor data.
 *
 * @return SensorDataSeriesConfiguration A bundle with details about how recorded data can be used
 */
fun Context.readSensorDataSeriesConfiguration(): SensorDataSeriesConfiguration {
    val properties = loadProperties()
    val intervalForUpdate = properties.getProperty(PropertiesKeys.SENSOR_DATA_SERIES_INTERVAL_FOR_UPDATE.key).toInt()
    val trendThreshold = properties.getProperty(PropertiesKeys.SENSOR_DATA_SERIES_TREND_THRESHOLD.key).toInt()
    return SensorDataSeriesConfiguration(intervalForUpdate, trendThreshold)
}

/**
 * Exception to throw if a configuration is not suitable in the properties file
 */
class InvalidConfigurationException(override var message:String): Exception(message)


// ************
// Data classes
// ************

/***
 * Models a bundle data storing configuration elements for BLE sensors / boxes / devices
 *
 * @param serviceUUID The service UUID to use to find the suitable devices
 * @param sensorsCharUUID The UUID of the sensor providing data
 * @param sensorCharDescriptorUUID The descriptor of the sensor providing data
 */
data class BleConfiguration(val serviceUUID: String, val sensorsCharUUID: String, val sensorCharDescriptorUUID: String)

/**
 * Models the app configuration for games
 *
 * @param enableStarGame True to enable the game, false to hide it
 * @param enableBalloonGame True to enable the game, false to hide it
 * @param enableSheepGame True to enable the game, false to hide it
 * @param enableSpaceGame True to enable the game, false to hide it
 * @param enableToadGame True to enable the game, false to hide it
 */
data class AppGamesConfiguration(val enableStarGame: Boolean, val enableBalloonGame: Boolean,
                                 val enableSheepGame: Boolean, val enableSpaceGame: Boolean, val enableToadGame: Boolean)

/**
 * Models a bundle of game configuration
 *
 * @param difficultyFactor The hardness value to apply to calculations
 */
data class GameDetailsConfiguration(val difficultyFactor: DifficultyFactor)

/**
 * Models a bundle of game configuration
 *
 * @param difficultyFactorLow The difficulty value to apply to calculations for low level
 * @param difficultyFactorMedium The difficulty value to apply to calculations for medium level
 * @param difficultyFactorHigh The difficulty value to apply to calculations for high level
 */
data class DifficultyDetailsConfiguration(val difficultyFactorLow: Double, val difficultyFactorMedium: Double,
                                          val difficultyFactorHigh: Double)

/**
 * Models a bundle of star game configuration elements.
 * These values defines steps of progression :
 *      - [ minThreshold1 ; maxThreshold1] is the first step
 *      - [ minThreshold2 ; maxThreshold2] is the second step
 *      - [ minThreshold3 ; maxThreshold3[ is the third step, the final before the congratulations!
 *
 * @param minThreshold1 The minimal value of the 1st step of progression
 * @param maxThreshold1 The maximal value of the 1st step of progression
 * @param minThreshold2 The minimal value of the 2nd step of progression
 * @param maxThreshold2 The maximal value of the 2nd step of progression
 * @param minThreshold3 The minimal value of the 3rd step of progression
 * @param maxThreshold3 The maximal value of the 3rd step of progression
 */
data class StarGameConfiguration(val minThreshold1: Int, val maxThreshold1: Int,
                                 val minThreshold2: Int, val maxThreshold2: Int,
                                 val minThreshold3: Int, val maxThreshold3: Int)

/**
 * Models a bundle of balloon game configuration elements.
 * These values defines steps of progression :
 *      - [ minThreshold1 ; maxThreshold1] is the first step
 *      - [ minThreshold2 ; maxThreshold2] is the second step
 *      - [ minThreshold3 ; maxThreshold3] is the third step,
 *      - [ minThreshold4 ; maxThreshold4[ is the fourth step, the final before the congratulations!
 *
 * @param minThreshold1 The minimal value of the 1st step of progression
 * @param maxThreshold1 The maximal value of the 1st step of progression
 * @param minThreshold2 The minimal value of the 2nd step of progression
 * @param maxThreshold2 The maximal value of the 2nd step of progression
 * @param minThreshold3 The minimal value of the 3rd step of progression
 * @param maxThreshold3 The maximal value of the 3rd step of progression
 * @param minThreshold4 The minimal value of the 4th step of progression
 * @param maxThreshold4 The maximal value of the 4th step of progression
 */
data class BalloonGameConfiguration(val minThreshold1: Int, val maxThreshold1: Int,
                                    val minThreshold2: Int, val maxThreshold2: Int,
                                    val minThreshold3: Int, val maxThreshold3: Int,
                                    val minThreshold4: Int, val maxThreshold4: Int)

/**
 * Models a bundle of sheep game configuration elements.
 * @param moveOffset The move the sheep should make for each rise or fall event, in px
 * @param walkAnimationPeriod Each "frame" of the game icon is animated with this period (in ms)
 * @param moveDuration The duration of each move used by the animator in charge of the sheep icon
 */
data class SheepGameConfiguration(val moveOffset: Int, val walkAnimationPeriod: Long, val moveDuration: Long)

/**
 * Models a bundle of sheep game default configuration values..
 * @param defaultFencesCount The default number of fences to display
 * @param defaultSpeed The default speed for the floor and fences
 */
data class SheepGameDefaultConfiguration(val defaultFencesCount: Int, val defaultSpeed: String)

/**
 * Models a bundle of configuration details for sensor data series
 *
 * @param intervalForUpdate Each interval-th items, compute a ne average of recorded sensor data and store it
 * @param trendThreshold The trend threshold defining if trend is increasing, freezing or decreasing
 */
data class SensorDataSeriesConfiguration(val intervalForUpdate: Int, val trendThreshold: Int)