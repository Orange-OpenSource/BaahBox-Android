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
package com.orange.labs.orangetrainingbox.game

import android.view.View
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.doAnswer


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
     * Test the computeHitbox() extension functions of View class
     */
    @Test
    fun computeHitbox() {

        val defineExpectedHitbox: (Int, Int, Int, Int) -> Hitbox = { x, y, width, height ->
            Hitbox (
                // See [View.computeHitbox(): Hitbox in CollisionDetector v1.0.0
                x,
                x + width,
                y,
                y + height
            )
        }

        val testComputeHitbox: (Int, Int, Int, Int) -> Unit = { x, y, width, height ->
            val view = createMockView(x, y, width, height)
            val expectedHitbox = defineExpectedHitbox(x, y, width, height)
            val actualHitbox = view.computeHitbox()
            assertTrue("${expectedHitbox.minX} != ${actualHitbox.minX}", expectedHitbox.minX == actualHitbox.minX)
            assertTrue("${expectedHitbox.maxX } != ${actualHitbox.maxX}", expectedHitbox.maxX == actualHitbox.maxX)
            assertTrue("${expectedHitbox.minY } != ${actualHitbox.minY}", expectedHitbox.minY == actualHitbox.minY)
            assertTrue("${expectedHitbox.maxY} != ${actualHitbox.maxY}", expectedHitbox.maxY == actualHitbox.maxY)
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
     * Test the isCollision() method
     */
    @Test
    fun isCollision() {

        val first = createMockView(80, 60, 50, 50)
        var second = createMockView(500, 600, 10, 10)

        // Case where second is too far from first
        assertFalse("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        /*
        // Case where second hits first in its top left corner
        second = createMockView(40, 30, 60, 100)
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())
        */
        // Case where second hits first in its top right corner
        second = createMockView(110, 50, 20, 20)
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        // Case where second hits first in its bottom right corner
        second = createMockView(110, 90, 30, 30)
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        // Case where second hits first in its bottom left corner
        second = createMockView(60, 10, 30, 70 )
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        // Case where second hits first at only one point (bottom right corner)
        second = createMockView(130, 110, 10, 10 )
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        // Case where second is embedded in first
        second = createMockView(90, 80, 10, 10 )
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

        // Case when second touches first in one of its side without entering in it
        // No collisions because not entered (yep, we are cool with sheeps and space ships)
        second = createMockView(130, 70, 90, 10 )
        assertTrue("${first.computeHitbox()} vs ${second.computeHitbox()}",
            CollisionDetector(first, second).isCollision())

    }

    /**
     * Test the startDetection() method
     */
    @Test
    fun startDetection() {

        val first = createMockView(80, 60, 50, 50)
        val second = createMockView(500, 600, 10, 10)

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

        val first = createMockView(80, 60, 50, 50)
        val second = createMockView(500, 600, 10, 10)

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

    // Helper functions

    /**
     * Creates a mock view
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @return View
     */
    private fun createMockView(x: Int, y: Int, width: Int, height: Int): View {
        val view = mock(View::class.java)
        // Mock getLocationOnScreen() because it does not return anything and modify array in param
        doAnswer { invocation ->
            val args = invocation.arguments[0] as IntArray
            args[0] = x
            args[1] = y
            args
        }.`when`(view).getLocationOnScreen(any(IntArray::class.java))
        // Mock properties
        `when`(view.width).thenReturn(width)
        `when`(view.height).thenReturn(height)
        return view
    }

}