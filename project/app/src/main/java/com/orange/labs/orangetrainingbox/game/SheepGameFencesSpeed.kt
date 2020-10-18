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
package com.orange.labs.orangetrainingbox.game

/**
 * Enumeration defining the types of speed for the fences animations.
 *
 * @param preferencesValue - Because speed is bound to an [androidx.preference.SeekBarPreference],
 * the seekbar values as integers are saved for the enum values
 * @since 14/10/2020
 * @version 1.0.0
 */
enum class SheepGameFencesSpeed(val preferencesValue: Int) {

    /**
     * Fences move slowly.
     */
    LOW(0),

    /**
     * Fences move normally.
     */
    MEDIUM(1),

    /**
     * Fences move quickly.
     */
    HIGH(2);

    companion object {

        /**
         * Using the given _value_, converts to a value of [SheepGameFencesSpeed] based on their
         * _preferencesValue_.
         *
         * @param value
         * @return The matching enum of null if nothing matches
         */
        fun fromIntToValue(value: Int) : SheepGameFencesSpeed? {
            return when(value) {
                LOW.preferencesValue -> LOW
                MEDIUM.preferencesValue -> MEDIUM
                HIGH.preferencesValue -> HIGH
                else -> null
            }
        }
    }

}
