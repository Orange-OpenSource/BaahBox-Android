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

/**
 * Enumeration defining objects for properties to configure the app.
 *
 * These keys are related to the entries in the assets/app_configuration.properties, and also to
 * the Preferences.
 *
 * @param key - The key used in the properties file which matches this property
 *
 * @since 20/05/2019
 * @version 1.10.0
 */
enum class PropertiesKeys(val key: String) {

    /* Misc. */

    /**
     * Enables demo interaction with swipes faking baah box data
     */
    ENABLE_DEMO_FEATURE("enable_demo_feature"),

    /**
     * The interval in ms where collision detection is done
     */
    COLLISION_DETECTION_INTERVAL("collision_detection_interval"),

    /* Games */

    /**
     * Set to true to enable the star game, false to disable it
     */
    ENABLE_GAME_STAR("enable_game_star"),
    /**
     * Set to true to enable the balloon game, false to disable it
     */
    ENABLE_GAME_BALLOON("enable_game_balloon"),
    /**
     * Set to true to enable the sheep game, false to disable it
     */
    ENABLE_GAME_SHEEP("enable_game_sheep"),
    /**
     * Set to true to enable the space game, false to disable it
     */
    ENABLE_GAME_SPACE("enable_game_space"),
    /**
     * Set to true to enable the toad game, false to disable it
     */
    ENABLE_GAME_TOAD("enable_game_toad"),

    /* Game configuration */

    /**
     * The numeric values of the difficulty factors (low, medium and high).
     */
    DIFFICULTY_NUMERIC_VALUES("difficulty_factor_values"),

    /* Star game */

    /**
     * A set of values defining steps for the star game where congratulations and animations change
     */
    GAME_STAR_THRESHOLD("game_star_threshold"),

    /* Balloon game */

    /**
     * A set of values defining steps for the balloon game where congratulations and animations change
     */
    GAME_BALLOON_THRESHOLD("game_balloon_threshold"),
    /**
     * Defines the period where the game icon of the balloon game in the introduction screen is changed during animation
     */
    GAME_BALLOON_INTRODUCTION_ANIMATION_PERIOD("game_balloon_introduction_animation_period"),

    /* Sheep game */

    /**
     * The distance the sheep should walk for each rise or fall event
     */
    GAME_SHEEP_MOVE_OFFSET("game_sheep_move_offset"),
    /**
     * The duration of animations used by the animator dealing with the sheep icon
     */
    GAME_SHEEP_MOVE_DURATION("game_sheep_move_duration"),
    /**
     * Defines the period where the game icon of the sheep game in the introduction screen is changed during animation
     */
    GAME_SHEEP_INTRODUCTION_ANIMATION_PERIOD("game_sheep_introduction_animation_period"),
    /**
     * Defines the default speed for the sheep / fences, i.e. the speed for which the floor with fences move
     */
    GAME_SHEEP_DEFAULT_SPEED_VALUE("game_sheep_fences_default_speed"),
    /**
     * The numeric values for the factor to apply to the default speed (low, medium and high).
     */
    GAME_SHEEP_DEFAULT_SPEED_FACTOR("game_sheep_fences_default_speed_factor"),
    /**
     * Defines the default number of fences to jump over
     */
    GAME_SHEEP_DEFAULT_FENCES_NUMBER("game_sheep_fences_default_number"),
    /**
     * Defines the max number of fences to jump over
     */
    GAME_SHEEP_MAX_FENCES_NUMBER("game_sheep_fences_max_number"),

    /* Sensor data series */

    /**
     * Each interval-th items, compute a ne average of recorded sensor data and store it
     */
    SENSOR_DATA_SERIES_INTERVAL_FOR_UPDATE("sensor_series_interval_for_update"),

    /**
     *  The trend threshold defining if trend is increasing, freezing or decreasing
     */
    SENSOR_DATA_SERIES_TREND_THRESHOLD("sensor_series_trend_threshold"),

    /**
     * The size of the queue which stores sensor data records. Trends (increase, decrease, equal) are defined
     * with the average of records stored in a queue.
     */
    SENSOR_DATA_SERIES_QUEUE_SIZE("sensor_series_queue_size"),

    /* BLE configuration */

    /**
     * Identifier of the BLE service
     */
    BLE_SERVICE_UUID("ble_service_uuid"),
    /**
     * Identifier to use to get Baah Box frames
     */
    BLE_SENSORS_CHAR_UUID("ble_sensors_char_uuid"),
    /**
     * Descriptor to use to get Baah Box frames
     */
    BLE_CHAR_DESCRIPTOR_UUID("ble_sensors_char_descriptor_uuid")

}