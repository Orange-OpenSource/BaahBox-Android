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

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.utils.properties.BalloonGameConfiguration
import com.orange.labs.orangetrainingbox.utils.properties.readBalloonAdditionalConfiguration
import com.orange.labs.orangetrainingbox.utils.properties.readBalloonGameConfiguration
import com.orange.labs.orangetrainingbox.ui.animations.IconAnimator
import com.orange.labs.orangetrainingbox.ui.demo.GesturesDemo
import kotlinx.android.synthetic.main.fragment_game_balloon_playing.*
import kotlinx.android.synthetic.main.fragment_game_star_intro.gameIcon
import kotlinx.android.synthetic.main.fragment_game_star_playing.tv_congratulations
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.find


/**
 * A subclass of [AbstractGameFragment] for the balloon game.
 * Player must contract one muscle so as to blow up a balloon and make it explode.
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 23/10/2018
 * @version 2.5.0
 * @see [AbstractGameFragment]
 */
class GameBalloonFragment : AbstractGameFragment() {


    // **********
    // Properties
    // **********

    /**
     * Permits to play some kind of animations for the game icon
     */
    private var gameIconAnimator: IconAnimator? = null


    // ***********************************
    // Inherited from AbstractGameFragment
    // ***********************************

    /**
     * Identifier of the layout introducing the game
     */
    override val introductionLayout: Int
        get() = R.layout.fragment_game_balloon_intro

    /**
     * Identifier of the layout with the game itself, where the user can play
     */
    override val playingLayout: Int
        get() = R.layout.fragment_game_balloon_playing

    /**
     * Identifier fo the restart / outro layout with e.g. the final score, a button to restart...
     */
    override val restartLayout: Int
        get() = R.layout.fragment_game_balloon_outro

    /**
     * Identifier of the game's title
     */
    override val screenTitle: Int
        get() = R.string.title_game_balloon

    /**
     * Identifier of the game's theming color
     */
    override val themingColor: Int
        get() = R.color.orange

    /**
     * An action define in the navigation graph to use to go from the introduction screen to the
     * playing screen.
     */
    override val actionFromIntroductionToPlaying: Int
        get() = R.id.action_gameBalloonFragment_to_gameBalloonPlayingFragment

    /**
     * An action to trigger to go back to the playing screen, e.g. for restart of the game
     */
    override val actionGoToPlaying: Int
        get() = R.id.action_global_gameBalloonPlayingFragment

    /**
     * Loads from the SafeArgs the argument "playing"
     */
    override fun loadFromArgsFlagPlaying(): Boolean {
        return GameBalloonFragmentArgs.fromBundle(arguments).playing
    }

    /**
     * Loads from the SafeArgs the argument "introducing"
     */
    override fun loadFromArgsFlagIntroducing(): Boolean {
        return GameBalloonFragmentArgs.fromBundle(arguments).introducing
    }

    /**
     * Uses an [IconAnimator] to display several images in the game icon widget
     */
    override fun startIntroductionAnimation() {
        gameIconAnimator = IconAnimator()
        val period = context!!.readBalloonAdditionalConfiguration()
        gameIconAnimator!!.animateGameIcon((activity as AppCompatActivity), gameIcon, period,
            arrayOf(R.mipmap.ic_balloon_0, R.mipmap.ic_balloon_1, R.mipmap.ic_balloon_2, R.mipmap.ic_balloon_3, R.mipmap.ic_balloon_4))
    }

    /**
     * Stops the [IconAnimator]
     */
    override fun stopIntroductionAnimation() {
        gameIconAnimator!!.stopAnimateGameIcon()
    }


    // *******
    // Methods
    // *******

    /**
     * Fragment lifecycle
     */
    override fun onPause() {
        super.onPause()
        gameIconAnimator?.stopAnimateGameIcon()
    }

    /**
     * Prepare the Bluetooth LE sensor observer.
     * Defines the behaviour of the observer using the sensor value, and thus the logic of the game.
     * Get from properties the thresholds of the balloon game.
     * Use the value returned by the sensor. Using this value, displays the suitable congratulation message
     * and the image matching the state of the balloon.
     */
    override fun prepareSensorObserver() {

        // Get the ViewModel.
        model = activity?.run {
            ViewModelProviders.of(this).get(TrainingBoxViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val gameConfiguration = context!!.readBalloonGameConfiguration()
        val difficultyFactor = getDifficultyNumericValue()

        // Define the observer
        val sensorBObserver = Observer<Int> { sensorValue ->

            processBaahBoxData(gameConfiguration, sensorValue, difficultyFactor)

        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.sensorB.observe(this, sensorBObserver)

    }

    /**
     * Defines, if defined in app config, a gesture listener with [GesturesDemo] to fake
     * sensors and sends data from manual tests.
     */
    override fun prepareGameLayout() {
        if (isDemoModeActivated()) {
            // Reset layout to default state
            model.sensorA.postValue(0)
            model.sensorB.postValue(0)
            // Define listeners
            GesturesDemo(model.sensorA, model.sensorB).addGestureListeners(
                find<ConstraintLayout>(R.id.clBalloonGamePlaying),
                context!!
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
    private fun processBaahBoxData(configuration: BalloonGameConfiguration, userInput: Int,
                                   difficultyFactor: Double) {

        val (a, b, c, d, e, f, g, h) = configuration

        when (inputsParser.prepareValue(userInput, difficultyFactor)) {
            in a..b -> {
                tv_congratulations.text = getString(R.string.game_balloon_congratulations_level_1)
                balloonPlaying.imageResource = R.mipmap.ic_balloon_0
            }
            in c..d -> {
                tv_congratulations.text = getString(R.string.game_balloon_congratulations_level_2)
                balloonPlaying.imageResource = R.mipmap.ic_balloon_1
            }
            in e..f -> {
                tv_congratulations.text = getString(R.string.game_balloon_congratulations_level_3)
                balloonPlaying.imageResource = R.mipmap.ic_balloon_2
            }
            in g until h -> {
                tv_congratulations.text = getString(R.string.game_balloon_congratulations_level_4)
                balloonPlaying.imageResource = R.mipmap.ic_balloon_3
            }
            else -> {
                val bundle = Bundle()
                bundle.putBoolean("introducing", false)
                bundle.putBoolean("playing", false)
                findNavController().navigate(actionGoToPlaying, bundle)
            }
        }

    }

}
