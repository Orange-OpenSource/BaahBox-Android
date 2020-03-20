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
package com.orange.labs.orangetrainingbox.ui

import android.content.Context
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.runner.AndroidJUnit4
import org.junit.runner.RunWith
import androidx.test.rule.ActivityTestRule
import com.orange.labs.orangetrainingbox.R
import org.junit.Before
import org.junit.Rule
import org.junit.Test


/**
 * To test [MainActivity] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 30/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestMainActivity {


    /**
     * Activity to play with
     */
    @Rule @JvmField var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

    /**
     * To get UI thread
     */
    private var appContext: Context? = null

    /**
     *
     */
    @Before
    fun setup(){
        appContext = InstrumentationRegistry.getTargetContext()
    }

    /**
     * Test the app bar of the main activity
     */
    @Test
    fun appBar() {
        onView(withText(R.string.app_name)).check(matches(isDisplayed()))
    }

    /**
     * Test the navgraph with fragments entries
     */
    @Test
    fun gameEntries(){

        // Check the navigation fragment

        onView(withId(R.id.nav_fragment)).check(matches(isDisplayed()))

        // Check entries of navgraph

        onView(withText(appContext!!.getString(R.string.title_game_star))).check(matches(isDisplayed()))
        onView(withText(appContext!!.getString(R.string.title_game_balloon))).check(matches(isDisplayed()))
        onView(withText(appContext!!.getString(R.string.title_game_sheep))).check(matches(isDisplayed()))
        onView(withText(appContext!!.getString(R.string.title_game_space))).check(matches(isDisplayed()))
        onView(withText(appContext!!.getString(R.string.title_game_toad))).check(matches(isDisplayed()))

    }

}