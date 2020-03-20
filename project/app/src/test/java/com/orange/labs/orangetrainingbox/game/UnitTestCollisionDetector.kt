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

import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockView
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * To test [CollisionDetector] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 05/09/2019
 * @version 1.0.0
 */
class UnitTestCollisionDetector {

    /**
     * Test Hitbox objects creations.
     * Nothing wrong should happen :)
     */
    @Test
    fun hitbox() {

        val testHitbox: (Int, Int, Int, Int) -> Unit = { minX, maxX, minY, maxY ->
            val hitbox = Hitbox(minX, maxX, minY, maxY)
            assertTrue(minX == hitbox.minX)
            assertTrue(maxX == hitbox.maxX)
            assertTrue(minY == hitbox.minY)
            assertTrue(maxY == hitbox.maxY)
        }

        testHitbox(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        testHitbox(-1, -1, -1, -1)
        testHitbox(0, 0, 0, 0)
        testHitbox(1, 1, 1, 1)
        testHitbox(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

    }

    /**
     * Test the computeHitbox() extension functions of View class.
     * The hitbox of a view must be computed from its 2D location, its width and height following:
     <pre>
        minX = x
        maxX = x + width
        minY = y
        maxY = y + height
     </pre>
     */
    @Test
    fun `hitbox must be computed from view coordinates, width and height`() {

        // Closure to compute an expected hitbox
        val defineExpectedHitbox: (Int, Int, Int, Int) -> Hitbox = { x, y, width, height ->
            Hitbox (
                // See [View.computeHitbox(): Hitbox in CollisionDetector v1.0.0
                x,
                x + width,
                y,
                y + height
            )
        }

        // Closure which will computed expected hitbox and view's hitbox defined with parameters
        val testComputeHitbox: (Int, Int, Int, Int) -> Unit = { x, y, width, height ->
            val view = mockView(x, y, width, height)
            val expectedHitbox = defineExpectedHitbox(x, y, width, height)
            val actualHitbox = view.computeHitbox()
            assertTrue("${expectedHitbox.minX} != ${actualHitbox.minX}",
                expectedHitbox.minX == actualHitbox.minX)
            assertTrue("${expectedHitbox.maxX } != ${actualHitbox.maxX}",
                expectedHitbox.maxX == actualHitbox.maxX)
            assertTrue("${expectedHitbox.minY } != ${actualHitbox.minY}",
                expectedHitbox.minY == actualHitbox.minY)
            assertTrue("${expectedHitbox.maxY} != ${actualHitbox.maxY}",
                expectedHitbox.maxY == actualHitbox.maxY)
        }

        testComputeHitbox(128, 128, 50, 50)
        testComputeHitbox(234, 1028, 42, 1337)
        testComputeHitbox(Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE, Int.MIN_VALUE)
        testComputeHitbox(-1, -1, -1, -1)
        testComputeHitbox(0, 0, 0, 0)
        testComputeHitbox(1, 1, 1, 1)
        testComputeHitbox(Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE, Int.MAX_VALUE)

    }

    /**
     * Checks collision with two views: the second far from the first
     */
    @Test
    fun `two distant views must not be in collision`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(500, 600, 10, 10)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertFalse("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Checks where second hits first in its top right corner
     */
    @Test
    fun `must collide if second hits first's top right corner`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(110, 50, 20, 20)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Checks where second hits first in its bottom right corner
     */
    @Test
    fun `must collide if second hits first's bottom right corner`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(110, 90, 30, 30)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Checks where second hits first in its bottom left corner
     */
    @Test
    fun `must collide if second hits first in its bottom left corner`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(60, 10, 30, 70)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Checks where second hits first at only one point (bottom right corner)
     */
    @Test
    fun `must collide if second hits first at only one point in its bottom left corner`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(130, 110, 10, 10)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Checks where second is embedded in first
     */
    @Test
    fun `must collide if second is embedded in first`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(90, 80, 10, 10)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Case when second touches first in one of its side without entering in it
     */
    @Test
    fun `must collide if second touches first in one of its side without going further`() {
        // Given
        val first = mockView(80, 60, 50, 50)
        val second = mockView(130, 70, 90, 10)
        // When
        val collides = CollisionDetector(first, second).isCollision()
        // Then
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}", collides)
    }

    /**
     * Test the startDetection() method
     */
    @Test
    fun startDetection() {

        val first = mockView(80, 60, 50, 50)
        val second = mockView(500, 600, 10, 10)

        // Simple call
        var detector = CollisionDetector(first, second)
        detector.startDetection()

        // Spamming
        detector = CollisionDetector(first, second)
        for (i in 0..1000) {
            detector.startDetection()
        }

    }

    /**
     * Test the stopDetection() method
     */
    @Test
    fun stopDetection() {

        val first = mockView(80, 60, 50, 50)
        val second = mockView(500, 600, 10, 10)

        // Simple call
        var detector = CollisionDetector(first, second)
        detector.stopDetection()

        // Spamming
        detector = CollisionDetector(first, second)
        for (i in 0..1000) {
            detector.stopDetection()
        }

        // Spamming again :)
        detector = CollisionDetector(first, second)
        for (i in 0..1000) {
            detector.startDetection()
            detector.stopDetection()
        }

    }

}