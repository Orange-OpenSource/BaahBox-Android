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
 * The highest the value is, the lowest is the difficulty coefficient.
 *
 * @author Pierre-Yves Lapersonne
 * @author Marc Poppleton
 * @since 16/05/2019
 * @version 3.0.0
 */
enum class DifficultyFactor {

    /**
     * A low factor to apply, making small the difficulty
     */
    LOW,

    /**
     * A medium factor to apply, making harder the games thant the LOW level
     */
    MEDIUM,

    /**
     * The highest factor to apply, without help ;-)
     */
    HIGH

}
