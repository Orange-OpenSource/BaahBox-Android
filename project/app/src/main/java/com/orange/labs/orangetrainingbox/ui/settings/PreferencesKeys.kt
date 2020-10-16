package com.orange.labs.orangetrainingbox.ui.settings

/**
 * Enumeration defining objects for preferences management.
 *
 * These keys are related to the data saved using [androidx.preference.PreferenceManager] for example.
 * Each value saved using the preference manager should have its key defined here.
 *
 * @param key - The key used in the as en entry to get preferences value
 *
 * @since 16/10/2020
 * @version 1.0.0
 */
enum class PreferencesKeys(val key: String) {

    /**
     * Demo mode is enabled or not through the settings of the app
     */
    ENABLE_DEMO_MODE("enable_demo_mode"),

    /**
     * The difficulty factor to apply
     */
    DIFFICULTY_FACTOR("difficulty_factor"),

    /**
     * The number of fences the sheep should jump over
     */
    SHEEP_GAME_FENCES_NUMBER("number_of_fences_to_jump"),

    /**
     * The speed of the fences animation
     */
    SHEEP_GAME_FENCES_SPEED("speed_of_fences_to_jump")

}