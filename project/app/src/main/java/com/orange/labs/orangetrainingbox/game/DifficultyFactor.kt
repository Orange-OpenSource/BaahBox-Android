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
 * Enumeration defining the factor to apply to sensor inputs.
 * The highest the difficulty is, the lowest should be the difficulty coefficient.
 * For example, a [DifficultyFactor.LOW] implies an high factor.
 *
 * @param preferencesValue - Because difficulty factor is bound to an [androidx.preference.SeekBarPreference],
 * the seekbar values as integers re saved for the enum values
 * @since 16/05/2019
 * @version 4.0.0
 */
enum class DifficultyFactor(val preferencesValue: Int) {

    /**
     * A low factor to apply, making small the difficulty.
     * Is bound to the 1st value of the seekbar.
     */
    LOW(0),

    /**
     * A medium factor to apply, making harder the games thant the LOW level.
     * Is bound to the 2nd value of the seekbar.
     */
    MEDIUM(1),

    /**
     * The highest factor to apply, without help ;-)
     * Is bound to the 3rd value of the seekbar.
     */
    HIGH(2);

    companion object {

        /**
         * Using the given _value_, converts to a value of [DifficultyFactor] based on their
         * _preferencesValue_.
         *
         * @param value
         * @return The matching enum of null if nothing matches
         */
        fun fromIntToValue(value: Int) : DifficultyFactor? {
            return when(value) {
                LOW.preferencesValue -> LOW
                MEDIUM.preferencesValue -> MEDIUM
                HIGH.preferencesValue -> HIGH
                else -> null
            }
        }
    }

}
