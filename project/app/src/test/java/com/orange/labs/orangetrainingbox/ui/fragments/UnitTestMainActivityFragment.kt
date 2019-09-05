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
package com.orange.labs.orangetrainingbox.ui.fragments

import com.orange.labs.orangetrainingbox.R
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

/**
 * To test [MainActivityFragment] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 29/08/2019
 * @version 1.0.0
 */
class UnitTestMainActivityFragment {


    /**
     * The fragment to test
     */
    private var mainActivityFragment: MainActivityFragment? = null

    /**
     * Init
     */
    @Before
    fun setup(){
        mainActivityFragment = MainActivityFragment()
    }

    /**
     * Deinit
     */
    @After
    fun teardown(){
        mainActivityFragment = null
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun screenTitle() {
        assertEquals(R.string.title_activity_main, mainActivityFragment?.screenTitle)
    }

    /**
     * Test the screenTitle property
     */
    @Test
    fun themingColor() {
        assertEquals(R.color.pink, mainActivityFragment?.themingColor)
    }

}