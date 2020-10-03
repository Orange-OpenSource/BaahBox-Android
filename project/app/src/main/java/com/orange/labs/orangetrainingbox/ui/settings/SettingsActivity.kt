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

package com.orange.labs.orangetrainingbox.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.orange.labs.orangetrainingbox.utils.properties.PropertiesKeys
import android.app.Activity
import android.content.Intent
import androidx.preference.*
import com.orange.labs.orangetrainingbox.utils.properties.isDemoFeatureEnabled
import com.orange.labs.orangetrainingbox.utils.properties.readSheepDefaultConfiguration

/**
 * Activity dedicated to preferences
 *
 * @since 24/05/2019
 * @version 1.5.0
 */
open class SettingsActivity : AppCompatActivity() {

    /**
     * A simple companion object
     */
    companion object {

        /**
         * The release
         */
        private var versionRelease: String? = null

    }

    /**
     * Activity lifecycle.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pi = packageManager.getPackageInfo(packageName, 0)
        versionRelease = buildReleaseString(pi.versionName, pi.versionCode)
        supportFragmentManager
            .beginTransaction()
            .replace(android.R.id.content, SettingsFragment())
            .commit()
    }

    /**
     * Activity lifecycle.
     */
    override fun onBackPressed() {
        setResult(Activity.RESULT_OK, Intent())
        finish()
    }

    /**
     * Procedure which will build a user-friendly string describing the version of the app
     *
     * @param versionName - The name of the version
     * @param versionCode - The build code
     * @return String A string with version name and version code
     */
    fun buildReleaseString(versionName: String, versionCode: Int): String {
        val sb = StringBuilder()
        sb.append("Version ").append(versionName)
        sb.append(" - Build ").append(versionCode)
        return sb.toString()
    }


    /**
     * Fragment dedicated to the preferences
     *
     * @since 24/05/2019
     * @version 1.1.0
     */
    class SettingsFragment : PreferenceFragmentCompat() {

        /**
         * Fragment lifecycle
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(com.orange.labs.orangetrainingbox.R.xml.preferences, rootKey)
            prepareDemoPreference()
            prepareVersionPreference()
            prepare3rdPartyPreference()
            prepareDifficultyFactorPreference()
            prepareSheepGameFencesPreference()
            prepareSheepGameSpeedPreference()
        }

        /**
         * If enabled in config file, display the demo switch
         */
        private fun prepareDemoPreference() {
            val demoPreference: SwitchPreference = findPreference("preferences_demo_mode_enabled")!!
            if (activity?.isDemoFeatureEnabled() == false) {
                demoPreference.isVisible = false
            } else {
                demoPreference.setOnPreferenceChangeListener { _, newValue ->
                    val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                    val editor = preferences.edit()
                    editor.putBoolean(PropertiesKeys.ENABLE_DEMO_FEATURE.key, newValue as Boolean)
                    editor.apply()
                    true
                }
            }
        }

        /**
         *
         */
        private fun prepareVersionPreference() {
            val versionPreference: Preference = findPreference("pref_key_about_app")!!
            versionPreference.summary = versionRelease
        }

        /**
         * Prepares the widget related to 3rd party licenses
         */
        private fun prepare3rdPartyPreference() {
            val licensesPreference: Preference = findPreference("pref_key_about_licenses")!!
            licensesPreference.setOnPreferenceClickListener {
                val displayer = LicensesDisplayer()
                val notices = displayer.prepareNotices(requireActivity())
                displayer.displayNotices(requireActivity(), notices)
                true
            }
        }

        /**
         *
         */
        private fun prepareDifficultyFactorPreference(){
            val difficultyPreference: Preference = findPreference("pref_key_settings_sensors_difficulty")!!
            // TODO If never defined use default value from properties
            difficultyPreference.setOnPreferenceChangeListener { _, newValue ->
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.putInt(PropertiesKeys.DIFFICULTY_FACTOR.key, newValue as Int)
                editor.apply()
                true
            }
        }

        /**
         * Defines minimum, maximum and defaults values for the slider
         */
        private fun prepareSheepGameFencesPreference() {
            val sheepGameDefaultConfiguration = requireActivity().readSheepDefaultConfiguration()
            val numberOfFencesPreferences: SeekBarPreference = findPreference("pref_key_settings_game_sheep_fences_number")!!
            // TODO If never defined use default value from properties
            numberOfFencesPreferences.setOnPreferenceChangeListener { _, newValue ->
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.putInt("pref_key_settings_game_sheep_fences_number", newValue as Int)
                editor.apply()
                true
            }
            numberOfFencesPreferences.min = 1
            numberOfFencesPreferences.max = sheepGameDefaultConfiguration.defaultMaxFencesCount
            numberOfFencesPreferences.setDefaultValue(sheepGameDefaultConfiguration.defaultFencesCount)
        }

        /**
         * Defines minimum, maximum and defaults values for the slider
         */
        private fun prepareSheepGameSpeedPreference() {
            val speedPreference: SeekBarPreference = findPreference("pref_key_settings_game_sheep_fences_speed")!!
            speedPreference.setOnPreferenceChangeListener { _, newValue ->
                val preferences = PreferenceManager.getDefaultSharedPreferences(context)
                val editor = preferences.edit()
                editor.putInt("pref_key_settings_game_sheep_fences_speed", newValue as Int)
                editor.apply()
                true
            }
        }

    } // End of class SettingsFragment

} // End of class SettingsActivity : AppCompatActivity()

