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
package com.orange.labs.orangetrainingbox.utils.logs

import org.junit.Test

/**
 * To test [Logger] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/08/2019
 * @version 1.0.0
 */
class TestLogger {

    /**
     * Test the v() static method with several kinds of inputs
     */
    @Test
    fun v(){
        Logger.v("")
        Logger.v("Foo")
        Logger.v("✏️️")
    }

    /**
     * Test the d() static method with several kinds of inputs
     */
    @Test
    fun d(){
        Logger.d("")
        Logger.d("Foo")
        Logger.d("✏️️")
    }

    /**
     * Test the i() static method with several kinds of inputs
     */
    @Test
    fun i(){
        Logger.i("")
        Logger.i("Foo")
        Logger.i("✏️️")
    }

    /**
     * Test the v() static method with several kinds of inputs
     */
    @Test
    fun w(){
        Logger.w("")
        Logger.w("Foo")
        Logger.w("✏️️")
    }

    /**
     * Test the v() static method with several kinds of inputs
     */
    @Test
    fun e(){
        Logger.e("")
        Logger.e("Foo")
        Logger.e("✏️️")
    }

    /**
     * Test the v() static method with several kinds of inputs
     */
    @Test
    fun wtf(){
        Logger.wtf("")
        Logger.wtf("Foo")
        Logger.wtf("✏️️")
    }

}