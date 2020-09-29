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
package com.orange.labs.orangetrainingbox.utils.structures

import java.lang.IllegalArgumentException

/**
 * Data structure acting as a FIFO structure.
 * The first element saved in this list will be the first out.
 * Contains a maximum size. If a new element should be added and the queue is full, the first item will be removed.
 *
 * @param maxSize The highest number of items this queue can have. If 0, no maximal size is defined.
 *
 * @author Pierre-Yves Lapersonne
 * @since 22/08/2019
 * @version 1.0.0
 */
class Queue<T>(private val maxSize: Int) {


    // **********
    // Properties
    // **********

    /**
     * The structure where elements are really stored
     */
    internal val elements: MutableList<T> = mutableListOf()

    /**
     * Flag saying if the structure is a limitless queue (true) or not (false)
     */
    val isLimitless = (maxSize == 0)


    // ****
    // Init
    // ****

    init {
        if (maxSize < 0) throw IllegalArgumentException("The size must be >= 0")
    }


    // *******
    // Methods
    // *******

    /**
     * Returns a boolean flag saying if the queue is empty (true) or not (false)
     *
     * @return Boolean
     */
    fun isEmpty() = elements.isEmpty()

    /**
     * Returns the number of items stored in this queue
     *
     * @return Int
     */
    fun count() = elements.size

    /**
     * Enqueue the item. It will thus be added in the last position.
     * If the queue has reached its maximal size, will before dequeue the first item and add the new.
     *
     * @param item The object to add
     */
    fun enqueue(item: T) {
        if (!isLimitless && count() >= maxSize) {
            dequeue()
        }
        elements.add(item)
    }

    /**
     * Enqueue all items. It will thus be added in the last position.
     * If the queue has reached its maximal size, will before dequeue the first item and add the new.
     *
     * @param items The objects to add
     */
    fun enqueueAll(items: MutableList<T>) {
        items.forEach {
            enqueue(it)
        }
    }

    /**
     * Dequeue the first element of the structure, i.e. element at index 0, of course if
     * the structure is not empty
     *
     * @returns T The removed element or null if structure is empty
     */
    fun dequeue() = if (!isEmpty()) elements.removeAt(0) else null

    /**
     * Returns without dequeueing the first element of the queue if the struct is not empty, otherwise
     * returns null
     *
     * @return T The first element of the structure
     */
    fun peek() = if (!isEmpty()) elements[0] else null


    /**
     *
     * @return String The stringified version of the queue
     */
    override fun toString(): String = elements.toString()

}

// **********************************
// Extensions of this Queue structure
// **********************************

/**
 * Computes an average for a queue of integers.
 *
 * @return Int The average of items in this list
 */
fun Queue<Int>.average(): Int {
    val count = count()
    if (count <= 0) return 0
    var sum = 0
    elements.forEach { sum += it }
    return sum / count
}