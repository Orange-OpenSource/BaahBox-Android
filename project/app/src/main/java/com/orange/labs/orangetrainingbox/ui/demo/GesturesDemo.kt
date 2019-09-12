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
package com.orange.labs.orangetrainingbox.ui.demo

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.MutableLiveData

/**
 * Class providing method to detec gestures so as to fake sensor data, for demo purposes
 *
 * @author Pierre-Yves Lapersonne
 * @since 30/08/2019
 * @version 1.0.0
 *
 * @param sensorA One of the muscle sensor, as a source of data
 * @param sensorB One of the muscle sensor, as a source of data
 */
class GesturesDemo(private val sensorA: MutableLiveData<Int>, private val sensorB: MutableLiveData<Int>) {

    /**
     * Defines a listener to get gestures for this view
     *
     * @param view The view one which gesture listener must be added
     * @param context
     */
    fun addGestureListeners(view: View, context: Context) {
        val gestureDetector = GestureDetector(context, GesturesDemoListener(sensorA, sensorB))
        view.setOnTouchListener { _, e ->
            // If released during gesture
            if (e.action == MotionEvent.ACTION_UP) {
                sensorA.postValue(0)
                sensorB.postValue(0)
            }
            // Process gesture
            gestureDetector.onTouchEvent(e)
        }
    }

}

/**
 * A listener to attach to a view with callbacks triggered when a gesture is done.
 * Once the gesture ( a scroll) is made, will compute the difference between start and end points on X and Y axis
 * and send results to sensors.
 *
 * @version 1.0.0
 *
 * @param sensorA One of the muscle sensor, as a source of data, with delta of points in X axis
 * @param sensorB One of the muscle sensor, as a source of data, with delta of points in Y axis
 */
class GesturesDemoListener(private val sensorA: MutableLiveData<Int>,
                           private val sensorB: MutableLiveData<Int>): GestureDetector.OnGestureListener {

    /**
     * Not used
     */
    override fun onShowPress(e: MotionEvent?) {
        // Does nothing
    }

    /**
     * Not used
     */
    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        // Does nothing
        return true
    }

    /**
     * Not used
     */
    override fun onDown(e: MotionEvent?): Boolean {
        // Does nothing
        return true
    }

    /**
     * Not used
     */
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
        // Does nothing
        return true
    }

    /**
     * Triggered when a scroll or a swipe is done (on Y or X axis).
     *
     * @param e1 The first down motion event that started the scrolling.
     * @param e2 The move motion event that triggered the current onScroll.
     * @param distanceX The distance along the X axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between e1 and e2.
     * @param distanceY The distance along the Y axis that has been scrolled since the last
     *              call to onScroll. This is NOT the distance between e1 and e2.
     * @return Boolean Always true
     */
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {

        var diffX: Int = ((e1?.x ?: 0f) - (e2?.x ?: 0f)).toInt()
        if (diffX < 0) diffX = 0

        var diffY: Int = ((e1?.y ?: 0f) - (e2?.y ?: 0f)).toInt()
        if (diffY < 0) diffY = 0

        sensorA.postValue(diffX)
        sensorB.postValue(diffY)

        return true
    }


    /**
     * Not used
     */
    override fun onLongPress(e: MotionEvent?) {
        // Does nothing
    }

}