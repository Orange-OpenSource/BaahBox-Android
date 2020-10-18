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

import android.view.View

// *******
// Classes
// *******

/**
 * Class which allows to make assertions on objects so as to check if there is a collision
 * including them or not.
 * Computes hitboxes using locations and dimensions, and compare.
 *
 * @since 05/09/2019
 * @version 2.0.0
 */
class CollisionDetector {

    // *******
    // Methods
    // *******

    /**
     * Returns true if _first_ and _second_ collides using their hitboxes, false otherwise.
     * Compares min and max values for X and Y axis on objects.
     *
     * Four cases are managed, and compares first min and max X and Y values to second's.
     *
     * Note this method words well for the sheep game, but not updated (if needed) for other games.
     *
     * @param first One of the views to test, e.g. a sheep
     * @param second One of the views to test, e.g. a fence
     * @return Boolean
     */
    fun isCollision(first: View, second:View): Boolean {

        val (firstMinX, firstMaxX, firstMinY, firstMaxY) = first.computeHitbox()
        val (secondMinX, secondMaxX, secondMinY, secondMaxY) = second.computeHitbox()

        if (secondMinX == 0 && secondMaxX == 0) return false

        // Case where object hits the other in one of its corners
        if (secondMinX in firstMinX..firstMaxX && secondMaxY in firstMinY..firstMaxY) return true
        if (secondMaxX in firstMinX..firstMaxX && secondMaxY in firstMinY..firstMaxY) return true
        if (secondMinX in firstMinX..firstMaxX && secondMinY in firstMinY..firstMaxY) return true
        if (secondMaxX in firstMinX..firstMaxX && secondMinY in firstMinY..firstMaxY) return true

        return false

    }

}

/**
 * Models an hitbox, i.e. a range of coordinates where an object can be touched by another.
 *
 * @param minX The start of the hitbox in X axis
 * @param maxX The end of the hitbox in X axis
 * @param minY The start of the hitbox in Y axis
 * @param maxY The end of the hitbox in Y axis
 */
data class Hitbox(val minX: Int, val maxX: Int, val minY: Int, val maxY: Int)

// **********
// Extensions
// **********

/**
 * Computes the hitbox of a view.
 * Will get the coordinates of the view in the device landmark (location on screen),
 * and use its width and height.
 *
 * minX = this.x
 * maxX = this.x + width
 * minY = this.y
 * maxY = this.y + height
 *
 */
fun View.computeHitbox(): Hitbox {
    val positionOnScreen = intArrayOf(0, 0)
    getLocationOnScreen(positionOnScreen)
    return Hitbox (
        positionOnScreen[0],
        positionOnScreen[0] + width,
        positionOnScreen[1],
        positionOnScreen[1] + height
    )
}