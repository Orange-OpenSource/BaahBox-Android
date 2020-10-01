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
package com.orange.labs.orangetrainingbox.utils.logs

import android.util.Log

/**
 * Class dedicated for logs. It provides simple methods with more visible and cooler traces, as a kind of proxy.
 *
 * @since 21/05/2019
 * @version 1.0.0
 */
class Logger {

    /**
     * Cheat to provide Java-like static methods for logging
     */
    companion object {

        /**
         * The ta gin use for logs
         */
        private const val TAG = "BaahBox"

        /**
         * Use the common logger to log a message in the verbose channel, prefixed with ✏️
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun v(message: String) {
            Log.d(TAG, "✏️️️ $message")
        }

        /**
         * Use the common logger to log a message in the debug channel, prefixed with ▫️
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun d(message: String) {
            Log.d(TAG, "▫️️ $message")
        }

        /**
         * Use the common logger to log a message in the info channel, prefixed with ➡
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun i(message: String) {
            Log.d(TAG, "➡️ $message")
        }

        /**
         * Use the common logger to log a message in the warning channel, prefixed with ⚠
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun w(message: String) {
            Log.d(TAG, "⚠️ $message")
        }

        /**
         * Use the common logger to log a message in the warning channel, prefixed with ‼️
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun e(message: String) {
            Log.d(TAG, "‼️️ $message")
        }

        /**
         * Use the common logger to log a message in the WTF channel, prefixed with ☠️
         * @param message The message to log
         */
        @Suppress("unused")
        @JvmStatic
        fun wtf(message: String) {
            Log.d(TAG, "☠️ $message")
        }

    }

}