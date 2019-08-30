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

import android.content.Context
import android.widget.TextView
import androidx.test.InstrumentationRegistry
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.ui.MainActivity
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.core.IsInstanceOf.instanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * To test [GameSheepFragment] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 30/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestGameSheepFragment {


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
        goToGame()
    }

    /**
     * Test the layout elements
     */
    @Test
    fun layoutTextContents(){

        // Test the app bar

        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.toolbar))))
            .check(matches(withText(appContext!!.getString(R.string.title_game_sheep))))

        // Test the text contents
        onView(withId(R.id.tvLine1)).check(matches(withText(appContext!!.getString(R.string.game_sheep_instructions_line_1))))
        onView(withId(R.id.tvLine2)).check(matches(withText(appContext!!.getString(R.string.game_sheep_instructions_line_2))))

        // Test the button
        onView(withId(R.id.btnPlay)).check(matches(withText(appContext!!.getString(R.string.btn_start))))

    }

    /**
     * Test the action on the play button: should go from intro screen to playing screen.
     */
    @Test
    fun playButton() {
        onView(withId(R.id.btnPlay)).perform(click())
        onView(withId(R.id.clSheepGamePlaying)).check(matches(isDisplayed()))
    }

    // ****************
    // Helper functions
    // ****************

    /**
     * Goes to star game screen
     */
    private fun goToGame(){
        onView(withText(appContext!!.getString(R.string.title_game_sheep))).perform(click())
    }

}