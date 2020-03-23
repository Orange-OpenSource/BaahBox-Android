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

import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.btle.TrainingBoxViewModel
import com.orange.labs.orangetrainingbox.ui.animations.IconAnimator
import kotlinx.android.synthetic.main.fragment_game_star_intro.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.support.v4.find
import android.util.TypedValue
import android.util.DisplayMetrics
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceManager
import com.orange.labs.orangetrainingbox.game.CollisionDetector
import com.orange.labs.orangetrainingbox.ui.demo.GesturesDemo
import com.orange.labs.orangetrainingbox.utils.structures.SensorDataSeries
import com.orange.labs.orangetrainingbox.utils.properties.*
import com.orange.labs.orangetrainingbox.utils.structures.SensorTrends


// *******
// Classes
// *******

/**
 * A fragment which embeds the logic of the sheep game and the GUI part.
 * Possesses animators to make the sheep walk, jump and fall, and move the fences.
 * Receives data from the Baah Box through the model, parse them and make the sheep move.
 * Use configuration defined by the user in the preferences and also in the app config.
 *
 * <b>The animations are not efficient and the move of the sheep should be improved</b>
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 23/10/2018
 * @version 2.3.0
 * @see [AbstractGameFragment]
 */
class GameSheepFragment : AbstractGameFragment() {


    // **********
    // Properties
    // **********

    /**
     * Permits to play some kind of animations for the game icon, i.e. here move the legs of the sheep.
     */
    private lateinit var gameIconAnimator: IconAnimator

    /**
     * Permits to play animations for fences, i.e. make them move on the floor.
     */
    private lateinit var fencesAnimator: ObjectAnimator

    /**
     * Initial position on Y-axis of the sheep game icon.
     */
    private var sheepInitialYPosition = -1

    /**
     * Flag indicating if the sheep was still in its lowest position.
     * Used when sensor data are received, and a LOWEST trend is computed again.
     */
    private var wasAlreadyInLowestPosition: Boolean = false

    /**
     * The difficulty factor to apply.
     */
    private var difficultyFactor: Double = 0.0

    /**
     * The registry for sensor data records.
     */
    private lateinit var lastPoints: SensorDataSeries

    /**
     * The object which looks periodically for collisions between the sheep and the fences.
     */
    private lateinit var collisionDetector: CollisionDetector

    /**
     * The default configuration for this game.
     */
    private lateinit var defaultGameConfiguration: SheepGameDefaultConfiguration

    /**
     * The global configuration for this game.
     */
    private lateinit var gameConfiguration: SheepGameConfiguration

    /**
     * The total number of fences to jump over.
     */
    private val totalNumberOfFences: Int by lazy  {
        PreferenceManager.getDefaultSharedPreferences(activity).getInt("pref_key_settings_game_sheep_fences_number", 0)
    }

    /**
     * The remaining number of fences to jump over.
     * If changed makes the GUI updated with the new value.
     */
    private var remainingNumberOfFences: Int = 0
        set (value) {
            field = value
            updateRemainingFencesMessage()
        }


    // ***********************************
    // Inherited from AbstractGameFragment
    // ***********************************

    /**
     * Identifier of the layout introducing the game.
     */
    override val introductionLayout: Int
        get() = R.layout.fragment_game_sheep_intro

    /**
     * Identifier of the layout with the game itself, where the user can play.
     */
    override val playingLayout: Int
        get() = R.layout.fragment_game_sheep_playing

    /**
     * Identifier fo the restart / outro layout with e.g. the final score, a button to restart...
     */
    override val restartLayout: Int
        get() = R.layout.fragment_game_sheep_outro

    /**
     * Identifier of the game's theming color.
     */
    override val themingColor: Int
        get() = R.color.pink

    /**
     * Identifier of the game's title.
     */
    override val screenTitle: Int
        get() = R.string.title_game_sheep

    /**
     * An action defined in the navigation graph to use to go from the introduction screen to the
     * playing screen.
     */
    override val actionFromIntroductionToPlaying: Int
        get() = R.id.action_gameSheepFragment_to_gameSheepPlayingFragment

    /**
     * An action to trigger to go back to the playing screen, e.g. for restart of the game after a win
     * or a loose.
     */
    override val actionGoToPlaying: Int
        get() = R.id.action_global_gameSheepPlayingFragment

    /**
     * Loads from the SafeArgs the argument "playing".
     */
    override fun loadFromArgsFlagPlaying(): Boolean {
        return GameSheepFragmentArgs.fromBundle(arguments).playing
    }

    /**
     * Loads from the SafeArgs the argument "introducing".
     */
    override fun loadFromArgsFlagIntroducing(): Boolean {
        return GameSheepFragmentArgs.fromBundle(arguments).introducing
    }

    /**
     * Uses an [IconAnimator] to display several images in the game icon widget.
     */
    override fun startIntroductionAnimation() {
        stopIntroductionAnimation()
        gameIconAnimator = IconAnimator()
        val period = context!!.readSheepGameConfiguration().walkAnimationPeriod
        gameIconAnimator.animateGameIcon((activity as AppCompatActivity), gameIcon, period,
            arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
    }

    /**
     * Stops the [IconAnimator] which makes the sheep "walk".
     */
    override fun stopIntroductionAnimation() {
        if (::gameIconAnimator.isInitialized) gameIconAnimator.stopAnimateGameIcon()
    }

    /**
     * Prepare the Bluetooth LE sensor observer.
     * Defines the behaviour of the observer using the sensor value, and thus the logic of the game.
     * Get from properties the configuration details of the sheep game like difficulty factor or numeric values for examples.
     * Use the value returned by the sensor and process the game logic.
     */
    override fun prepareSensorObserver() {

        // Get the ViewModel
        model = activity?.run {
            ViewModelProviders.of(this).get(TrainingBoxViewModel::class.java)
        } ?: throw Exception("Invalid Activity")

        defaultGameConfiguration = context!!.readSheepDefaultConfiguration()
        gameConfiguration = context!!.readSheepGameConfiguration()
        difficultyFactor = getDifficultyNumericValue()

        val sensorDataSeriesConfiguration = context!!.readSensorDataSeriesConfiguration()
        lastPoints = SensorDataSeries(sensorDataSeriesConfiguration.queueSize,
                                        sensorDataSeriesConfiguration.intervalForUpdate,
                                        sensorDataSeriesConfiguration.trendThreshold)

        // Define the observer
        val sensorBObserver = Observer<Int> { sensorValue ->
            processBaahBoxData(sensorValue)
        }

        // Observe the LiveData, passing in this activity as the LifecycleOwner and the observer
        model.sensorB.observe(this, sensorBObserver)

    }

    /**
     * Defines, if defined in app config, a gesture listener with [GesturesDemo] to fake
     * sensors and sends data from manual gestures.
     * Prepares the inheriting classes for the game logic, the animations and other logic for the game.
     * The game animations start here (sheep icon, fences).
     * This method is triggered from the super-class when the activity is resuming.
     */
    override fun prepareGameLayout() {
        if (isDemoModeActivated()) {
            // Reset layout to default state
            model.sensorA.postValue(0)
            model.sensorB.postValue(0)
            // Define listeners
            GesturesDemo(model.sensorA, model.sensorB).addGestureListeners(
                find<ConstraintLayout>(R.id.clSheepGamePlaying),
                context!!
            )
        }
        updateRemainingFencesMessage()
        startIntroductionAnimation()
        moveFences()
    }


    // *****************
    // Lifecycle methods
    // *****************

    /**
     * Fragment lifecycle.
     * Stops animations (walking sheep and sliding fences).
     */
    override fun onPause() {
        super.onPause()
        if (::gameIconAnimator.isInitialized) gameIconAnimator.stopAnimateGameIcon()
        if (::fencesAnimator.isInitialized) fencesAnimator.pause()
    }

    /**
     * Firstly calls the super.onResume() method so as to inflate layouts, prepare observers...
     * Then checks if the game is ended, and in this case update the widgets with details.
     */
    override fun onResume() {

        super.onResume()

        // If game ended
        if (!loadFromArgsFlagIntroducing()
            && !loadFromArgsFlagPlaying()) {

            // If the player has won, display victory image
            if (GameSheepFragmentArgs.fromBundle(arguments).victory) {

                find<ImageView>(R.id.ivGameSheepFloor).visibility = View.INVISIBLE
                val gameIcon = find<ImageView>(R.id.ivGameSheepVictory)
                gameIcon.visibility = View.VISIBLE

                find<TextView>(R.id.tvLine1).text = getString(R.string.game_sheep_congratulations)
                find<TextView>(R.id.tvLine2).text = getString(R.string.game_sheep_score_success)

                gameIconAnimator = IconAnimator()
                gameIconAnimator.animateGameIcon((activity as AppCompatActivity), gameIcon, 1000,
                    arrayOf(R.mipmap.ic_sheep_welcome_1, R.mipmap.ic_sheep_welcome_2))

            // Else the player has lost, display score and bang image
            } else {

                find<ImageView>(R.id.ivGameSheepFloor).visibility = View.VISIBLE
                find<ImageView>(R.id.ivGameSheepBang).visibility = View.VISIBLE

                val jumpedFences = GameSheepFragmentArgs.fromBundle(arguments).numberOfJumpedFences
                val totalFences = GameSheepFragmentArgs.fromBundle(arguments).totalNumberOfFences
                find<TextView>(R.id.tvLine1).text = getString(R.string.game_sheep_comforting)
                find<TextView>(R.id.tvLine2).text = resources.getQuantityString(R.plurals.game_sheep_score_details,
                    totalFences, jumpedFences, totalFences)

            }

        }

    }

    // **********
    // Game logic
    // **********

    /**
     * The logic of this game.
     * Records the new input sent by the Baah Box (or the gesture listeners in demo mode).
     * Defines then a trend for the sheep (rising, falling, staying at its coordinates).
     * Moves the sheep following this trend.
     *
     * @param userInput The data given by the Baah box, i.e. the sensor value
     */
    private fun processBaahBoxData(userInput: Int) {
        lastPoints.addRecord(userInput)
        val trend = lastPoints.trendOfRecordedData()
        //Logger.d("Sheep game - sensor data: received $userInput, trend is $trend ")
        moveSheep(trend)
    }

    /**
     * Triggered when a collision has been detected by the [CollisionDetector] between the sheep view and
     * a fence view.
     */
    private fun processCollision() {
        stopIntroductionAnimation()
        fencesAnimator.cancel()
        find<ImageView>(R.id.gameIcon).imageResource = R.mipmap.ic_sheep_bump
        val bundle = Bundle()
        bundle.putBoolean("introducing", false)
        bundle.putBoolean("playing", false)
        bundle.putBoolean("victory", false)
        bundle.putInt("totalNumberOfFences", totalNumberOfFences)
        bundle.putInt("numberOfJumpedFences", totalNumberOfFences - remainingNumberOfFences)
        findNavController().navigate(actionGoToPlaying, bundle)
    }

    /**
     * Triggered when all fences have been jumped by the sheep.
     */
    private fun processVictory() {
        stopIntroductionAnimation()
        fencesAnimator.cancel()
        val bundle = Bundle()
        bundle.putBoolean("introducing", false)
        bundle.putBoolean("playing", false)
        bundle.putBoolean("victory", true)
        findNavController().navigate(actionGoToPlaying, bundle)
    }

    /**
     * Moves the sheep with an Y-axis translation using an offset based on user input / sensor value.
     * Modifies thus the top margin of the game icon.
     *
     * @param trend The value saying if the sheep should rise (INCREASE), stay (EQUAL) or fall (DECREASE),
     * or if the sheep is as its HIGHEST or LOWEST position.
     */
    private fun moveSheep(trend: SensorTrends) {

        if (trend == SensorTrends.EQUAL) return

        val offsetY = when (trend) {
            SensorTrends.INCREASE -> gameConfiguration.moveOffset * -1
            SensorTrends.DECREASE -> gameConfiguration.moveOffset
            else -> 0
        }

        val sheepView = find<ImageView>(R.id.gameIcon)
        val layoutParams = sheepView.layoutParams as ConstraintLayout.LayoutParams
        if (sheepInitialYPosition == -1) sheepInitialYPosition = layoutParams.topMargin

        when (trend) {
            SensorTrends.LOWEST -> {
                if (!wasAlreadyInLowestPosition) {
                    wasAlreadyInLowestPosition = true
                    startIntroductionAnimation()
                }
                layoutParams.setMargins(layoutParams.leftMargin, sheepInitialYPosition, layoutParams.rightMargin, layoutParams.bottomMargin)
            }
            SensorTrends.HIGHEST -> {
                wasAlreadyInLowestPosition = false
                layoutParams.setMargins(layoutParams.leftMargin, 0, layoutParams.rightMargin, layoutParams.bottomMargin)
            }
            else -> {
                wasAlreadyInLowestPosition = false
                stopIntroductionAnimation()
                sheepView.imageResource = R.mipmap.ic_sheep_jump
                val newMarginTop = layoutParams.topMargin + offsetY
                if (newMarginTop < 0 || newMarginTop > sheepInitialYPosition) return
                layoutParams.setMargins(layoutParams.leftMargin, newMarginTop, layoutParams.rightMargin, layoutParams.bottomMargin)
            }
        }

        sheepView.layoutParams = layoutParams

    }

    /**
     * Makes the fences images move on the floor from the right to the left.
     */
    private fun moveFences() {

        // Resume animations if paused (e.g. when fragment paused)
        if (::fencesAnimator.isInitialized && fencesAnimator.isPaused) {
            fencesAnimator.resume()
        // Prepare animation of the fence, which will be repeated
        } else {
            // Loads and adds the fence asset in a dedicated image view
            val parentLayout = find<ConstraintLayout>(R.id.clSheepGamePlaying)
            val fenceImageView = addNewFenceView(parentLayout)
            fencesAnimator = createFenceAnimator(fenceImageView, parentLayout)
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

        val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, toDp(100f))
        val marginTop = resources.getDimension(R.dimen.game_sheep_fence_margin_top).toInt()
        layoutParams.setMargins(toDp(0f), marginTop, toDp(0f), toDp(0f))
        fenceImageView.layoutParams = layoutParams

        // Add the new view to parent layout with constraints
        parent.addView(fenceImageView)
        val constraintSet = ConstraintSet()
        constraintSet.clone(parent)
        constraintSet.connect(fenceImageView.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(fenceImageView.id, ConstraintSet.END, R.id.ivGameSheepFloor, ConstraintSet.END)
        constraintSet.applyTo(parent)

        return fenceImageView

    }

    /**
     * Creates and returns an animator which will make X-axis translations to animate an image view (the fence)
     * using its parent layout. The parent layout is used to remove the view once animation ended.
     * Uses configuration details picked from preferences or in-app config.
     * Calls logic parts within animator so as to deal with collisions, repetition, UI update etc.
     *
     * @param fence The image view to animate
     * @return [ObjectAnimator] The animator dealing with the view
     */
    private fun createFenceAnimator(fence: ImageView, parent: ConstraintLayout): ObjectAnimator {

        val metrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(metrics)

        fencesAnimator = ObjectAnimator.ofFloat(fence, "translationX",
            metrics.widthPixels.toFloat(), metrics.widthPixels * -1f)
        fencesAnimator.duration = computeFencesSpeed()
        fencesAnimator.repeatCount = totalNumberOfFences - 1 // For k fences, repeat k-1 times
        fencesAnimator.repeatMode = ValueAnimator.RESTART

        // The listener dealing with fences views
        fencesAnimator.addListener(object : Animator.AnimatorListener {

            // If animation of fences is started, fences may be added, thus we can detect collisions
            override fun onAnimationStart(animation: Animator) {
                remainingNumberOfFences = totalNumberOfFences
                collisionDetector = CollisionDetector(find<ImageView>(R.id.gameIcon), fence, context!!.readCollisionDetectionInterval())
                collisionDetector.isCollisionDetected.observe(this@GameSheepFragment,
                    Observer<Boolean> { t -> if (t == true) processCollision() }
                )
                collisionDetector.startDetection()
            }

            // Remove views and load final view once animations completed
            override fun onAnimationEnd(animation: Animator) {
                collisionDetector.stopDetection()
                parent.removeView(view)
                processVictory()
            }

            override fun onAnimationCancel(animation: Animator) { }

            // Update GUI with score
            override fun onAnimationRepeat(animation: Animator) {
                remainingNumberOfFences--
            }

        })

        return fencesAnimator

    }

    /**
     * Changes the message indicating the number of remaining fences to jump over.
     */
    private fun updateRemainingFencesMessage() {
        val tvLine0 = find<TextView>(R.id.tvLine0)
        // Just started, no fences jumped
        if (remainingNumberOfFences == totalNumberOfFences) {
            tvLine0.text = resources.getQuantityString(R.plurals.game_sheep_instructions_line_0_start,
                remainingNumberOfFences, remainingNumberOfFences)
        // Already jumped at least one fence
        } else {
            val jumped = totalNumberOfFences - remainingNumberOfFences
            tvLine0.text = resources.getQuantityString(R.plurals.game_sheep_instructions_line_0_started,
                jumped, jumped, remainingNumberOfFences)
        }
    }

    /**
     * Reads from preferences the speed of fences.
     * Then get from app config the values to apply to compute this speed, i.e. the default speed
     * and the speed factor
     *
     * @return Long The speed for the fences
     */
    private fun computeFencesSpeed(): Long {
        val speedFactor = when(PreferenceManager.getDefaultSharedPreferences(activity)
            .getInt("pref_key_settings_game_sheep_fences_speed", 0)) {
            1 -> defaultGameConfiguration.speedFactors.second
            2 -> defaultGameConfiguration.speedFactors.third
            else /* including 0 */  -> defaultGameConfiguration.speedFactors.first
        }
        return (defaultGameConfiguration.defaultSpeed * speedFactor).toLong()

    }

}
