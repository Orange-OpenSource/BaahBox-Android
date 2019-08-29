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

import org.junit.Test
import org.junit.Assert.assertEquals


/**
 * To test [PropertiesKeys] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/08/2019
 * @version 1.1.0
 */
class TestPropertiesKeys {

    /**
     * Test if values of items of [PropertiesKeys] have not changed
     */
    @Test
    fun checkRawValues() {

        // Game enabling

        assertEquals(PropertiesKeys.ENABLE_GAME_STAR.key, "enable_game_star")
        assertEquals(PropertiesKeys.ENABLE_GAME_BALLOON.key, "enable_game_balloon")
        assertEquals(PropertiesKeys.ENABLE_GAME_SHEEP.key, "enable_game_sheep")
        assertEquals(PropertiesKeys.ENABLE_GAME_SPACE.key, "enable_game_space")
        assertEquals(PropertiesKeys.ENABLE_GAME_TOAD.key, "enable_game_toad")

        // Game configuration

        assertEquals(PropertiesKeys.DIFFICULTY_FACTOR.key, "difficulty_factor")
        assertEquals(PropertiesKeys.DIFFICULTY_NUMERIC_VALUES.key, "difficulty_factor_values")

        // Star game

        assertEquals(PropertiesKeys.GAME_STAR_THRESHOLD.key, "game_star_threshold")

        // Balloon game

        assertEquals(PropertiesKeys.GAME_BALLOON_THRESHOLD.key, "game_balloon_threshold")
        assertEquals(PropertiesKeys.GAME_BALLOON_INTRODUCTION_ANIMATION_PERIOD.key, "game_balloon_introduction_animation_period")

        // Sheep game

        assertEquals(PropertiesKeys.GAME_SHEEP_MOVE_OFFSET.key, "game_sheep_move_offset")
        assertEquals(PropertiesKeys.GAME_SHEEP_MOVE_DURATION.key, "game_sheep_move_duration")
        assertEquals(PropertiesKeys.GAME_SHEEP_INTRODUCTION_ANIMATION_PERIOD.key, "game_sheep_introduction_animation_period")
        assertEquals(PropertiesKeys.GAME_SHEEP_DEFAULT_SPEED_VALUE.key, "game_sheep_fences_default_speed")
        assertEquals(PropertiesKeys.GAME_SHEEP_DEFAULT_FENCES_NUMBER.key, "game_sheep_fences_default_number")

        // Sensor data series

        assertEquals(PropertiesKeys.SENSOR_DATA_SERIES_INTERVAL_FOR_UPDATE.key, "sensor_series_interval_for_update")
        assertEquals(PropertiesKeys.SENSOR_DATA_SERIES_TREND_THRESHOLD.key, "sensor_series_trend_threshold")
        assertEquals(PropertiesKeys.SENSOR_DATA_SERIES_QUEUE_SIZE.key, "sensor_series_queue_size")

        // BLE configuration

        assertEquals(PropertiesKeys.BLE_SERVICE_UUID.key, "ble_service_uuid")
        assertEquals(PropertiesKeys.BLE_SENSORS_CHAR_UUID.key, "ble_sensors_char_uuid")
        assertEquals(PropertiesKeys.BLE_CHAR_DESCRIPTOR_UUID.key, "ble_sensors_char_descriptor_uuid")

    }

}