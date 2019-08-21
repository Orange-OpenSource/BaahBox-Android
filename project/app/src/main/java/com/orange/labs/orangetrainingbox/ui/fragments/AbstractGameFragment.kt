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

import android.os.Bundle
import androidx.navigation.findNavController
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.game.DifficultyFactor
import com.orange.labs.orangetrainingbox.utils.properties.readDifficultyDetailsConfiguration
import kotlinx.android.synthetic.main.fragment_game_star_intro.*
import kotlinx.android.synthetic.main.fragment_game_star_outro.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import android.view.*


/**
 * Class which models a fragment to use in the navigation graph which embed a game.
 * Factorizes a lot of behaviours about the navigation or the Bluetooth data.
 * Each new game fragment must inherit from this class.
 *
 * @author Pierre-Yves Lapersonne
 * @author Marc Poppleton
 * @since 23/05/2019
 * @version 2.1.1
 * @see [AbstractThemedFragment], [GameWith3Screens], [NavigableGame]
 */
abstract class AbstractGameFragment : AbstractThemedFragment(), GameWith3Screens, NavigableGame {


    // **********
    // Properties
    // **********

    /**
     * Flag indicating the game is running. Permits to display the suitable layout.
     */
    protected var playing: Boolean = false

    /**
     * Flag indicating if the game has not been started or has been ended. Permits to display the suitable layout.
     */
    protected var introducing: Boolean = false

    /**
     * A reference to the model of the app (using the BLE devices)
     */
    protected lateinit var model: TrainingBoxViewModel


    // *******
    // Methods
    // *******

    /**
     *  Fragment lifecycle.
     *  Inflates the suitable layout according to the flags in use (playing and introducing).
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        playing = loadFromArgsFlagPlaying()
        return when (playing) {
            false -> {
                introducing = loadFromArgsFlagIntroducing()
                if (introducing) {
                    inflater.inflate(introductionLayout, container, false)
                } else {
                    inflater.inflate(restartLayout, container, false)
                }
            }
            else -> inflater.inflate(playingLayout, container, false)
        }
    }

    /**
     * Fragment lifecycle.
     * Using "playing" and "introducing" flags, will define navigation details, look for data for the game
     * or play animations.
     */
    override fun onResume() {

        super.onResume()

        // If not playing, start animation and prepare play button if introduction screen, else prepare for restart
        if (!playing) {

            // First start of the game
            if (introducing) {

                startIntroductionAnimation()

                btnPlay.onClick {
                    it!!.findNavController().navigate(actionFromIntroductionToPlaying)
                    stopIntroductionAnimation()
                }

            // Restart the game
            } else {
                btnRestart.onClick {
                    val bundle = Bundle()
                    bundle.putBoolean("introducing", false)
                    bundle.putBoolean("playing", true)
                    it!!.findNavController().navigate(actionGoToPlaying, bundle)
                }
            }

        // Otherwise define the model and the sensor observer for the game
        } else {
            prepareSensorObserver()
            prepareGameLayout()
        }

    }

    /**
     * Loads from properties file the numeric values to apply according to difficulty factor, and return
     * the value matching the factor.
     *
     * @return Double The value to apply to calculations
     */
    protected fun getDifficultyNumericValue(): Double {
        val difficultyConfigurationValues = context!!.readDifficultyDetailsConfiguration()
        return when (model.difficultyFactor) {
            DifficultyFactor.LOW -> difficultyConfigurationValues.difficultyFactorLow
            DifficultyFactor.MEDIUM -> difficultyConfigurationValues.difficultyFactorMedium
            DifficultyFactor.HIGH -> difficultyConfigurationValues.difficultyFactorHigh
        }
    }

    /**
     * Loads from SafeArgs the value for the "playing" flag.
     * This method must be implemented using the subclass suitable Args class.
     *
     * @return Boolean The boolean value of the "playing" argument
     */
    abstract fun loadFromArgsFlagPlaying(): Boolean

    /**
     * Loads from SafeArgs the value for the "introducing" flag.
     * This method must be implemented using the subclass suitable Args class.
     *
     * @return Boolean The boolean value of the "playing" argument
     */
    abstract fun loadFromArgsFlagIntroducing(): Boolean

    /**
     * Starts the animation of game icons or layouts for the introduction screen
     */
    abstract fun startIntroductionAnimation()

    /**
     * Stops the animation of game icons or layouts for the introduction screen
     */
    abstract fun stopIntroductionAnimation()

    /**
     * Prepares the observer for the sensor which will receive the data broadcast by the Baah Box sensors.
     * The game logic starts there.
     * This method is called when the activity is resuming.
     * Called before _prepareGameLayout_.
     */
    abstract fun prepareSensorObserver()

    /**
     * Prepares the inheriting classes for the game logic, the animations and other logic for the game.
     * The game animations start here.
     * This method is called when the activity is resuming.
     * Called after _prepareSensorObserver_.
     */
    abstract fun prepareGameLayout()

}

/**
 * Interface to use to get attributes of a game fragment which contains several layouts
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/05/2019
 * @version 1.0.0
 */
interface GameWith3Screens {

    /**
     * The identifier of the layout to inflate containing the introduction screen
     */
    val introductionLayout: Int

    /**
     * The identifier of the layout to inflate containing the playing screen
     */
    val playingLayout: Int

    /**
     * The identifier of the layout to inflate containing the restart screen
     */
    val restartLayout: Int

}

/**
 * Interface to use to get attributes to use for navigation between fragments using the Navigation graph pattern.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/05/2019
 * @version 1.0.0
 */
interface NavigableGame {

    /**
     * The identifier of the global action making the move from the introduction screen to the play screen
     */
    val actionFromIntroductionToPlaying: Int

    /**
     * The identifier of the global action used to go back to the play screen (e.g. from restart of the game)
     */
    val actionGoToPlaying: Int

}