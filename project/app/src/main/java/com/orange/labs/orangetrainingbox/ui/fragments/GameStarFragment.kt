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
package com.orange.labs.orangetrainingbox.ui.fragments

import android.graphics.drawable.Animatable
import android.os.Bundle
import android.os.Handler
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.ui.demo.GesturesDemo
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import com.orange.labs.orangetrainingbox.utils.properties.StarGameConfiguration
import com.orange.labs.orangetrainingbox.utils.properties.readStarGameConfiguration
import kotlinx.android.synthetic.main.fragment_game_star_intro.*
import kotlinx.android.synthetic.main.fragment_game_star_playing.*


/**
 * A subclass of [AbstractGameFragment] implementing the star game.
 * Player should contract one muscle stronger and stronger so as to make a star shine.
 *
 * @since 23/10/2018
 * @version 2.8.0
 * @see [AbstractGameFragment]
 */
class GameStarFragment : AbstractGameFragment() {

    // ***********************************
    // Inherited from AbstractGameFragment
    // ***********************************

    /**
     * Identifier of the layout introducing the game
     */
    override val introductionLayout: Int
        get() = R.layout.fragment_game_star_intro

    /**
     * Identifier of the layout with the game itself, where the user can play
     */
    override val playingLayout: Int
        get() = R.layout.fragment_game_star_playing

    /**
     * Identifier fo the restart / outro layout with e.g. the final score, a button to restart...
     */
    override val restartLayout: Int
        get() = R.layout.fragment_game_star_outro

    /**
     * Identifier of the game's title
     */
    override val screenTitle: Int
        get() = R.string.title_game_star

    /**
     * Identifier of the game's theming color
     */
    override val themingColor: Int
        get() = R.color.purple

    /**
     * An action define in the navigation graph to use to go from the introduction screen to the
     * playing screen.
     */
    override val actionFromIntroductionToPlaying: Int
        get() = R.id.action_gameStarFragment_to_gameStarPlayingFragment

    /**
     * An action to trigger to go back to the playing scree, e.g. for restart of the game
     */
    override val actionGoToPlaying: Int
        get() = R.id.action_global_gameStarPlayingFragment

    /**
     * Loads from the SafeArgs the argument "playing"
     */
    override fun loadFromArgsFlagPlaying(): Boolean {
        return GameStarFragmentArgs.fromBundle(arguments).playing
    }

    /**
     * Loads from the SafeArgs the argument "introducing"
     */
    override fun loadFromArgsFlagIntroducing(): Boolean {
        return GameStarFragmentArgs.fromBundle(arguments).introducing
    }

    /**
     * Get the drawable content of the game icon widget and makes it start its animation
     */
    override fun startIntroductionAnimation() {
        (gameIcon.drawable as Animatable).start()
    }

    /**
     * Nothing is done here
     */
    override fun stopIntroductionAnimation() {
        // Nothing to do
    }

    /**
     * Prepare the Bluetooth LE sensor observer.
     * Defines the behaviour of the observer using the sensor value, and thus the logic of the game.
     */
    override fun prepareSensorObserver() {

        // Get the ViewModel.
        model = activity?.run {
            ViewModelProvider(this).get(TrainingBoxViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val gameConfiguration = requireContext().readStarGameConfiguration()
        val difficultyFactor = getDifficultyNumericValue()

        // Define the observer
        val sensorBObserver = Observer<Int> { sensorValue ->

            processBaahBoxData(gameConfiguration, sensorValue, difficultyFactor)

        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.sensorB.observe(this, sensorBObserver)

    }

    /**
     * Defines, if demo feature enabled (project settings) and demo mode activated (app preferences)
     * a gesture listener with [GesturesDemo] to fake sensors and sends data from manual tests.
     */
    override fun prepareGameLayout() {
        if (isDemoModeActivated()) {
            // Reset layout to default state
            model.sensorA.postValue(0)
            model.sensorB.postValue(0)
            // Define listeners
            GesturesDemo(model.sensorA, model.sensorB).addGestureListeners(
                requireView().findViewById<ConstraintLayout>(R.id.clStarGamePlaying),
                requireContext()
            )
        }
    }

    // **********
    // Game logic
    // **********

    /**
     * The logic of this game.
     * Get from properties the thresholds to apply for the game.
     * According to the gotten sensor value, will display the suitable congratulation message and change the star transparency
     * to make it shine.
     *
     * @param configuration The game configuration
     * @param userInput The data given by the Baah box
     * @param difficultyFactor The numeric value to apply for inputs to create a kind of difficulty
     */
    private fun processBaahBoxData(
        configuration: StarGameConfiguration, userInput: Int,
        difficultyFactor: Double,
    ) {

        val parsedSensorValue = inputsParser.prepareValue(userInput, difficultyFactor)
        val (a, b, c, d, e, f) = configuration
        starPlaying.alpha = parsedSensorValue.toFloat() / f.toFloat()

        when (parsedSensorValue) {
            in a.toFloat()..b.toFloat() -> {
                tv_congratulations.text = getString(R.string.game_star_congratulations_level_1)
            }
            in c.toFloat()..d.toFloat() -> {
                tv_congratulations.text = getString(R.string.game_star_congratulations_level_2)
            }
            in e.toFloat()..f.toFloat() -> {
                tv_congratulations.text = getString(R.string.game_star_congratulations_level_3)
            }
            else -> { // Should be only >= 60
                val bundle = Bundle()
                bundle.putBoolean("introducing", false)
                bundle.putBoolean("playing", false)
                findNavController().navigate(actionGoToPlaying, bundle)
            }
        }

    }

}
