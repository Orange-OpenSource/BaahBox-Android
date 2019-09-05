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
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.ui.MainActivity
import org.hamcrest.CoreMatchers
import org.hamcrest.core.IsInstanceOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * To test simple fragment games like [GameStarFragment], [GameBalloonFragment]
 * or [GameSheepFragment] classes.
 * Allows to factorize instrumented tests for very similar games.
 *
 * @author Pierre-Yves Lapersonne
 * @since 30/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
abstract class AbstractInstrumentedTestSimpleGameFragment : InstrumentedTestSimpleGameFragment {


    /**
     * Activity to play with
     */
    @Rule
    @JvmField var activityActivityTestRule = ActivityTestRule(MainActivity::class.java)

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
     * Test the layout elements.
     * Will look for the toolbar and its text, and also for the instructions lines in 2 fields.
     * Will check if the play button exists.
     */
    @Test
    fun layoutTextContents(){

        // Test the app bar

        Espresso.onView(
            CoreMatchers.allOf(
                IsInstanceOf.instanceOf(TextView::class.java),
                ViewMatchers.withParent(ViewMatchers.withId(R.id.toolbar))
            )
        )
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(rStringGameTitle))))

        // Test the text contents

        Espresso.onView(ViewMatchers.withId(R.id.tvLine1))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(rStringGameInstructionLine1))))

        Espresso.onView(ViewMatchers.withId(R.id.tvLine2))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(rStringGameInstructionLine2))))

        // Test the button

        Espresso.onView(ViewMatchers.withId(R.id.btnPlay))
            .check(ViewAssertions.matches(ViewMatchers.withText(appContext!!.getString(R.string.btn_start))))

    }

    /**
     * Test the action on the play button: should go from intro screen to playing screen.
     */
    @Test
    fun playButton() {
        Espresso.onView(ViewMatchers.withId(R.id.btnPlay)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(playingModeLayoutId))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }


    // ****************
    // Helper functions
    // ****************

    /**
     * Goes to star game screen
     */
    private fun goToGame(){
        Espresso.onView(ViewMatchers.withText(appContext!!.getString(rStringGameTitle)))
            .perform(ViewActions.click())
    }

}

/**
 * Contains some properties which must be defined by classes dedicated to instrumented tests.
 */
interface InstrumentedTestSimpleGameFragment {

    /**
     * The title of the game, in R.string
     */
    val rStringGameTitle: Int

    /**
     * The first line for instructions
     */
    val rStringGameInstructionLine1: Int

    /**
     * The second line for instructions
     */
    val rStringGameInstructionLine2: Int

    /**
     * The id of the playing mode layout
     */
    val playingModeLayoutId: Int

}