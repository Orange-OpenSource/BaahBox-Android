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
package com.orange.labs.orangetrainingbox.utils.structures

import org.junit.Assert.*
import org.junit.Test
import java.lang.IllegalArgumentException

/**
 * To test [Queue] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/08/2019
 * @version 1.0.0
 */
class TestQueue {


    /**
     * Test the init of the queue with a bad size
     */
    @Test(expected = IllegalArgumentException::class)
    fun initWithBadSize() {
        Queue<Int>(-1)
    }

    /**
     * Test the init of the queue with several values for the size parameter
     */
    @Test
    fun initWithSizes(){
        Queue<Int>(0)
        Queue<Int>(1)
        Queue<Int>(Int.MAX_VALUE)
        try {
            Queue<Int>(-1)
        } catch (e: IllegalArgumentException) {
            // Expected :)
        }
        try {
            Queue<Int>(Int.MIN_VALUE)
        } catch (e: IllegalArgumentException) {
            // Expected :)
        }
    }

    /**
     * Test the isLimitless flag
     */
    @Test
    fun isLimitless() {
        var queue = Queue<Int>(20)
        assertFalse(queue.isLimitless)
        queue = Queue(0)
        assertTrue(queue.isLimitless)
    }

    /**
     * Test the isEmpty() method
     */
    @Test
    fun isEmpty(){

        val queue = Queue<Int>(50)
        assertTrue(queue.isEmpty())

        val list = mutableListOf(8, 1, 6, 8, 0, 0, 8, 5)
        queue.enqueueAll(list)
        assertFalse(queue.isEmpty())

        queue.dequeue()
        assertFalse(queue.isEmpty())

        for (i in 0..queue.count()) {
         queue.dequeue()
        }
        assertTrue(queue.isEmpty())

    }

    /**
     * Test the count() method
     */
    @Test
    fun count(){

        val queue = Queue<Int>(50)
        assertTrue(queue.count() == 0)

        val list = mutableListOf(8, 1, 6, 8, 0, 0, 8, 5)
        queue.enqueueAll(list)
        assertTrue(queue.count() == list.count())

        queue.dequeue()
        assertTrue(queue.count() == list.count() - 1)

        for (i in 0..queue.count()) {
            queue.dequeue()
        }
        assertTrue(queue.count() == 0)

    }

    /**
     * Test the enqueue() method
     */
    @Test
    fun enqueue() {

        // Limitless queue
        var queue = Queue<Int>(0)
        for (i in 0..100){
            queue.enqueue(i)
            assertEquals(queue.elements.last(), i)
        }

        // Limited queue - limit not reached
        queue = Queue(10)
        for (i in 0..9){
            queue.enqueue(i)
            assertEquals(queue.elements.last(), i)
        }

        // Limited queue - reached limit
        queue.enqueue(1337)
        assertEquals(queue.elements.last(), 1337)
        assertEquals(queue.elements.first(), 1) // 0 has been dequeue

    }

    /**
     * Test the enqueueAll() method
     */
    @Test
    fun enqueueAll() {

        // Limitless queue
        var queue = Queue<Int>(0)
        queue.enqueueAll(mutableListOf())
        assertTrue(queue.isEmpty())
        queue.enqueueAll(mutableListOf(3,1,1,3,0,8,3,5,3))
        assertTrue(queue.count() == 9)

        // Limited queue - limit not reached
        queue = Queue(10)
        queue.enqueueAll(mutableListOf())
        assertTrue(queue.isEmpty())
        queue.enqueueAll(mutableListOf(0,0,7))
        assertTrue(queue.count() == 3)

        // Limited queue - reached limit
        queue = Queue(3)
        queue.enqueueAll(mutableListOf())
        assertTrue(queue.isEmpty())
        queue.enqueueAll(mutableListOf(1,2,3))
        assertTrue(queue.count() == 3)
        queue.enqueueAll(mutableListOf(4,5,6))
        assertTrue(queue.count() == 3)
        assertEquals(queue.elements.last(), 6)
        assertEquals(queue.elements.first(), 4)
        queue.enqueueAll(mutableListOf(7,8,9,10,11))
        assertTrue(queue.count() == 3)
        assertEquals(queue.elements.last(), 11)
        assertEquals(queue.elements.first(), 9)

    }

    /**
     * Test the dequeue() method
     */
    @Test
    fun dequeue() {

        // Limitless queue
        var queue = Queue<Int>(0)
        queue.enqueueAll(mutableListOf(0,1,2,3,4,5,6))
        assertTrue(queue.count() == 7)
        assertTrue(queue.dequeue() == 0)
        assertTrue(queue.count() == 6)

        // Limited queue - limit not reached
        queue = Queue(10)
        assertTrue(queue.count() == 0)
        assertTrue(queue.dequeue() == null)

        // Limited queue - reached limit
        queue = Queue(2)
        assertTrue(queue.count() == 0)
        assertTrue(queue.dequeue() == null)
        queue.enqueueAll(mutableListOf(1,2))
        assertTrue(queue.dequeue() == 1)
        assertTrue(queue.dequeue() == 2)
        assertTrue(queue.dequeue() == null)

    }

    /**
     * Test the peek() method
     */
    @Test
    fun peek() {

        // Limitless queue
        var queue = Queue<Int>(0)
        queue.enqueueAll(mutableListOf(0,1,2,3,4,5,6))
        assertTrue(queue.count() == 7)
        assertTrue(queue.peek() == 0)
        assertTrue(queue.count() == 7)
        assertTrue(queue.elements.first() == 0)

        // Limited queue - limit not reached
        queue = Queue(10)
        queue.enqueueAll(mutableListOf(0,1,2,3,4,5,6))
        assertTrue(queue.count() == 7)
        assertTrue(queue.peek() == 0)
        assertTrue(queue.count() == 7)
        assertTrue(queue.elements.first() == 0)

        // Limited queue - reached limit
        queue = Queue(3)
        queue.enqueueAll(mutableListOf(0,1,2,3,4,5,6))
        assertTrue(queue.count() == 3)
        assertTrue(queue.peek() == 4)
        assertTrue(queue.count() == 3)
        assertTrue(queue.elements.first() == 4)

    }

    /**
     * Test the average() method
     */
    @Test
    fun average() {

        val list = mutableListOf(0,1,2,3,4,5,6)
        val average = list.average().toInt()

        // Limitless queue
        var queue = Queue<Int>(0)
        queue.enqueueAll(list)
        assertTrue(queue.average<Int>() == average)

        // Limited queue - limit not reached
        queue = Queue(10)
        queue.enqueueAll(list)
        assertTrue(queue.average<Int>() == average)

        // Limited queue - reached limit
        queue = Queue(3)
        queue.enqueueAll(list)
        assertTrue(queue.average<Int>() == 5) // average(4, 5, 6) = 5

    }

}