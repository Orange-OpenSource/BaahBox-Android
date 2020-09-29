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

import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockActivity
import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockImageView
import com.orange.labs.orangetrainingbox.R
import org.junit.Test


/**
 * To test [IconAnimator] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/03/2020
 * @version 1.0.0
 */
class UnitTestIconAnimator {

    /**
     * The object to test
     */
    private var iconAnimator = IconAnimator()

    /**
     * Test the animateGameIcon() with an array of images... without images.
     */
    @Test (expected = IllegalArgumentException::class)
    fun `should throw IllegalArgumentException if an empty list of images is given`() {
        // Given
        val activity = mockActivity()
        val imageView = mockImageView()
        val period = 500L
        val emptyList: Array<Int> = arrayOf()
        // When - Then
        iconAnimator.animateGameIcon(activity, imageView, period, emptyList)
    }

    /**
     * Test the animateGameIcon() with negative period
     */
    @Test (expected = IllegalArgumentException::class)
    fun `should throw IllegalArgumentException if negative period is given`() {
        // Given
        val activity = mockActivity()
        val imageView = mockImageView()
        val period = -1L
        val list = arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2)
        // When - Then
        iconAnimator.animateGameIcon(activity, imageView, period, list)
    }


    /**
     * Test the stopAnimateGameIcon() method
     */
    @Test
    fun stopAnimateGameIcon() {

        // First call
        iconAnimator.stopAnimateGameIcon()

        // Call after started animation
        val activity = mockActivity()
        val imageView = mockImageView()
        iconAnimator.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
        iconAnimator.stopAnimateGameIcon()

    }

    /**
     * Test the animateGameIcon() method
     */
    @Test
    fun animateGameIcon() {

        // First call
        val activity = mockActivity()
        val imageView = mockImageView()
        iconAnimator.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))

        // Stressing
        for (i in 0..100){
            iconAnimator.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
        }

    }

}