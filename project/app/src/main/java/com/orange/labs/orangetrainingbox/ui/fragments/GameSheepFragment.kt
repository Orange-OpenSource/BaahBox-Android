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

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.tools.properties.SheepGameConfiguration
import com.orange.labs.orangetrainingbox.tools.properties.readSheepAdditionalConfiguration
import com.orange.labs.orangetrainingbox.tools.properties.readSheepGameConfiguration
import com.orange.labs.orangetrainingbox.ui.animations.IconAnimator
import kotlinx.android.synthetic.main.fragment_game_star_intro.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.find
import android.util.TypedValue
import android.util.DisplayMetrics
import com.orange.labs.orangetrainingbox.tools.logs.Logger


// *******
// Classes
// *******

// TODO Enrich this doc
/**
 * A simple [Fragment] subclass.
 * Use the [GameSheepFragment.newInstance] factory method to create an instance of this fragment.
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 23/10/2018
 * @version 2.0.0
 * @see [AbstractGameFragment]
 */
class GameSheepFragment : AbstractGameFragment() {


    // **********
    // Properties
    // **********

    /**
     * Permits to play some kind of animations for the game icon
     */
    private var gameIconAnimator: IconAnimator? = null

    /**
     * Permits to play animations for fences
     */
    private lateinit var fencesAnimator: ObjectAnimator

    // ***********************************
    // Inherited from AbstractGameFragment
    // ***********************************

    /**
     * Identifier of the layout introducing the game
     */
    override val introductionLayout: Int
        get() = R.layout.fragment_game_sheep_intro

    /**
     * Identifier of the layout with the game itself, where the user can play
     */
    override val playingLayout: Int
        get() = R.layout.fragment_game_sheep_playing

    /**
     * Identifier fo the restart / outro layout with e.g. the final score, a button to restart...
     */
    override val restartLayout: Int
        get() = R.layout.fragment_game_sheep_outro

    /**
     * Identifier of the game's theming color
     */
    override val themingColor: Int
        get() = R.color.pink

    /**
     * Identifier of the game's title
     */
    override val screenTitle: Int
        get() = R.string.title_game_sheep

    /**
     * An action define in the navigation graph to use to go from the introduction screen to the
     * playing screen.
     */
    override val actionFromIntroductionToPlaying: Int
        get() = R.id.action_gameSheepFragment_to_gameSheepPlayingFragment

    /**
     * An action to trigger to go back to the playing screen, e.g. for restart of the game
     */
    override val actionGoToPlaying: Int
        get() = R.id.action_global_gameSheepPlayingFragment

    /**
     * Loads from the SafeArgs the argument "playing"
     */
    override fun loadFromArgsFlagPlaying(): Boolean {
        return GameSheepFragmentArgs.fromBundle(arguments).playing
    }

    /**
     * Loads from the SafeArgs the argument "introducing"
     */
    override fun loadFromArgsFlagIntroducing(): Boolean {
        return GameSheepFragmentArgs.fromBundle(arguments).introducing
    }

    /**
     * Uses an [IconAnimator] to display several images in the gameicon widget
     */
    override fun startIntroductionAnimation() {
        gameIconAnimator = IconAnimator()
        val period = context!!.readSheepAdditionalConfiguration().sheepAnimationPeriod
        gameIconAnimator!!.animateGameIcon((activity as AppCompatActivity), gameIcon, period,
            arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
    }

    /**
     * Stops the [IconAnimator]
     */
    override fun stopIntroductionAnimation() {
        gameIconAnimator!!.stopAnimateGameIcon()
    }

    // TODO Enrich this doc
    /**
     * Prepare the Bluetooth LE sensor observer.
     * Defines the behaviour of the observer using the sensor value, and thus the logic of the game.
     * Get from properties the thresholds of the sheep game.
     * Use the value returned by the sensor and process the game logic.
     */
    override fun prepareSensorObserver() {

        // Get the ViewModel.
        model = activity?.run {
            ViewModelProviders.of(this).get(TrainingBoxViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        val gameConfiguration = context!!.readSheepGameConfiguration()
        val difficultyFactor = getDifficultyNumericValue()

        // Define the observer
        val sensorBObserver = Observer<Int> { sensorValue ->
            processBaahBoxData(gameConfiguration, sensorValue, difficultyFactor)
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer.
        model.sensorB.observe(this, sensorBObserver)

    }

    /**
     * Prepares the inheriting classes for the game logic, the animations and other logic for the game.
     * The game animations start here (sheep icon, fences).
     * This method is triggered from the super-class when the activity is resuming.
     */
    override fun prepareGameLayout() {
        startIntroductionAnimation()
        moveFences()
        // TODO Check if collisions
        // TODO Display final animation (collision or all fences jumped)
    }

    // *****************
    // Lifecycle methods
    // *****************

    /**
     * Fragment lifecycle>.
     * Stops animations if running.
     */
    override fun onPause() {
        super.onPause()
        gameIconAnimator?.stopAnimateGameIcon()
        if (::fencesAnimator.isInitialized) fencesAnimator.pause()
    }

    // **********
    // Game logic
    // **********

    // TODO Enrich this doc
    /**
     * The logic of this game.
     * Get from properties the thresholds to apply for the game.
     * According to the gotten sensor value, will do things (display score, congratulation message, ...)
     *
     * @param configuration The game configuration
     * @param userInput The data given by the Baah box
     * @param difficultyFactor The numeric value to apply for inputs to create a kind of difficulty
     */
    private fun processBaahBoxData(configuration: SheepGameConfiguration, userInput: Int,
                                   difficultyFactor: Double) {

        // TODO

    }

    /**
     * Makes the fences images move above the floor from the right to the left
     */
    private fun moveFences() {

        // Resume animations if paused (e.g. when fragment paused)
        if (::fencesAnimator.isInitialized && fencesAnimator.isPaused) {
            fencesAnimator.resume()
        // Prepare animation of the fence, which will be repeated
        } else {
            // Loads and adds the fence asset in a dedicated image view
            val parentLayout = find<ConstraintLayout>(R.id.clGameSheep)
            val fenceImageView = addNewFenceView(parentLayout)
            fencesAnimator = createAnimator(fenceImageView, parentLayout)
            fencesAnimator.start()
        }

    }

    /**
     * Creates a new image view for the fence.
     * Uses [ConstraintLayout] params for configuration.
     * Adds the view to the parent layout.
     *
     * @param parent The parent layout
     * @return [ImageView]  The new view added to the parent layout
     */
    private fun addNewFenceView(parent: ConstraintLayout): ImageView {

        val toDp: (Float) -> Int = {
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, it, resources.displayMetrics).toInt()
        }

        // Create the new image view
        val fenceImageView = ImageView(this@GameSheepFragment.context)
        fenceImageView.id = View.generateViewId()
        fenceImageView.imageResource = R.mipmap.ic_sheep_fence
        val layoutParams = ConstraintLayout.LayoutParams(toDp(100f), toDp(100f))
        layoutParams.setMargins(toDp(0f), toDp(214f), toDp(0f), toDp(0f))
        fenceImageView.layoutParams = layoutParams

        // Add the new view to parent layout with constraints
        parent.addView(fenceImageView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(fenceImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(fenceImageView.id, ConstraintSet.END, R.id.gameSheepFloor, ConstraintSet.END)
        constraintSet.applyTo(parent)

        return fenceImageView

    }

    /**
     * Creates and returns an animator which will make X-axis translations to animate an image view using
     * its parent layout. The parent layout is used to remove the view once animation ended.
     * Uses configuration details picked from preferences or in-app config.
     *
     * @param view The image view to animate
     * @return [ObjectAnimator] The animator dealing with the view
     */
    private fun createAnimator(view: ImageView, parent: ConstraintLayout): ObjectAnimator {

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        fencesAnimator = ObjectAnimator.ofFloat(view, "translationX", 200f, metrics.widthPixels * -1f)
        // TODO: Load duration from config
        fencesAnimator.duration = 2000
        // TODO: Load count from config
        fencesAnimator.repeatCount = 10
        fencesAnimator.repeatMode = ValueAnimator.RESTART

        fencesAnimator.addListener(object : Animator.AnimatorListener {

            override fun onAnimationStart(animation: Animator) {
                Logger.d(">>>>> Fence animation started")
            }

            override fun onAnimationEnd(animation: Animator) {
                Logger.d(">>>>> Fence animation ended")
                parent.removeView(view)
                // TODO: If no fence has been touched, load success view
            }

            override fun onAnimationCancel(animation: Animator) {
                Logger.d(">>>>> Fence animation canceled")
            }

            override fun onAnimationRepeat(animation: Animator) {
                Logger.d(">>>>> Fence animation repeated")
                // TODO: If no collision has been made, update text of UI  about remaining fences to jump over
            }

        })

        return fencesAnimator

        // TODO: Check collisions
    }

}
