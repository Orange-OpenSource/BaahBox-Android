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
package com.orange.labs.orangetrainingbox.ui.animations

import android.app.Activity
import android.widget.ImageView
import androidx.test.runner.AndroidJUnit4
import com.orange.labs.orangetrainingbox.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock

/**
 * To test [IconAnimator] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 28/08/2019
 * @version 1.0.0
 */
@RunWith(AndroidJUnit4::class)
class InstrumentedTestIconAnimator {


    /**
     * The object to test
     */
    private var iconAnimator: IconAnimator? = null


    /**
     * Init
     */
    @Before
    fun setup(){
        iconAnimator = IconAnimator()
    }

    /**
     * Deinit
     */
    @After
    fun tearDown(){
        iconAnimator = null
    }

    /**
     * Test the animateGameIcon() method
     */
    @Test
    fun animateGameIcon() {

        // First call
        val activity: Activity = mock(Activity::class.java)
        val imageView: ImageView = mock(ImageView::class.java)
        iconAnimator?.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))

        // Stressing
        for (i in 0..100){
            iconAnimator?.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
        }

        // Empty array of images
        iconAnimator?.animateGameIcon(activity, imageView, 500, arrayOf())

    }

    /**
     * Test the animateGameIcon() with negative period
     */
    @Test (expected = IllegalArgumentException::class)
    fun animateIconWithNegativePeriod() {
        val activity: Activity = mock(Activity::class.java)
        val imageView: ImageView = mock(ImageView::class.java)
        iconAnimator?.animateGameIcon(activity, imageView, -1, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
    }

    /**
     * Test the stopAnimateGameIcon() method
     */
    @Test
    fun stopAnimateGameIcon() {

        // First call
        iconAnimator?.stopAnimateGameIcon()

        // Call after started animation
        val activity: Activity = mock(Activity::class.java)
        val imageView: ImageView = mock(ImageView::class.java)
        iconAnimator?.animateGameIcon(activity, imageView, 500, arrayOf(R.mipmap.ic_sheep_moving_1, R.mipmap.ic_sheep_moving_2))
        iconAnimator?.stopAnimateGameIcon()

    }

}