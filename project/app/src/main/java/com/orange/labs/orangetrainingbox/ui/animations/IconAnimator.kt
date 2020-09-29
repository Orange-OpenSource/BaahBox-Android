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
package com.orange.labs.orangetrainingbox.ui.animations

import android.app.Activity
import android.widget.ImageView
import java.lang.IllegalArgumentException
import java.util.*
import kotlin.concurrent.schedule

/**
 * Simple utility object dealing with animations of icons
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/05/20190
 * @version 1.2.1
 */
class IconAnimator {


    // **********
    // Properties
    // **********

    /**
     * Just a simple ticker to known which image should be displayed
     */
    private var ticker: Int = 0

    /**
     * The timer scheduling the tasks for animations
     */
    private var timer: Timer? = null


    // *******
    // Methods
    // *******

    /**
     * Animates the game icon in the introduction screen.
     * Displays several bitmap files' contents.
     *
     * @param context The object to use to run animations on UI thread
     * @param imageView The image view where images should be placed
     * @param period The period to wait (in ms) between each frame
     * @param images The images to display in the image view
     */
    fun animateGameIcon(context: Activity, imageView: ImageView, period: Long, images: Array<Int>) {
        period.takeIf { it >= 0 } ?: throw IllegalArgumentException("The period cannot be negative")
        images.takeIf { it.isNotEmpty() } ?: throw IllegalArgumentException("The array of images is empty. What should be displayed? O_o")
        if (timer != null) stopAnimateGameIcon()
        ticker = 0
        timer = Timer()
        timer?.schedule(delay=0, period=period) {
            context.runOnUiThread {
                displaySuitableImage(imageView, images)
            }
        }
    }

    /**
     * Stops the timer and its work
     */
    fun stopAnimateGameIcon() {
        timer?.cancel()
        timer = null
    }

    /**
     * Displays in the image view, using the ticker and the array of images resources, the good image
     *
     * @param imageView The widget to update
     * @param images The assets identifiers to use
     */
    private fun displaySuitableImage(imageView: ImageView, images: Array<Int>)  {
        val maxIndex = images.size - 1
        if (ticker > maxIndex) ticker = 0
        imageView.setImageResource(images[ticker])
        ticker++
    }

}