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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import com.orange.labs.orangetrainingbox.R
import com.orange.labs.orangetrainingbox.utils.logs.Logger
import com.orange.labs.orangetrainingbox.utils.properties.readAppGamesConfiguration
import kotlinx.android.synthetic.main.fragment_main.*

/**
 * A placeholder fragment containing a simple view.
 *
 * @since 23/10/2018
 * @version 1.3.0
 */
class MainActivityFragment : AbstractThemedFragment() {

    /**
     * Identifier of the fragment's theming color
     */
    override val themingColor: Int
        get() = R.color.pink

    /**
     * Identifier of the fragment's title
     */
    override val screenTitle: Int
        get() = R.string.title_activity_main

    /**
     * Fragment lifecycle.
     * Inflates the main view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    /**
     * Fragment lifecycle.
     * Defines the listeners for the menu items who moves navigation to the dedicated fragments.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val (enableStar, enableBalloon, enableSheep, enableSpace, enableToad) = requireContext().readAppGamesConfiguration()

        if (enableStar) {
            menu_star.setOnClickListener { it!!.findNavController().navigate(R.id.action_mainActivityFragment_to_gameStarFragment) }
        } else {
            Logger.i("Star game is not enabled")
            menu_star.alpha = 0.1f
        }

        if (enableBalloon) {
            menu_balloon.setOnClickListener { it!!.findNavController().navigate(R.id.action_mainActivityFragment_to_gameBalloonFragment) }
        } else {
            Logger.i("Balloon game is not enabled")
            menu_balloon.alpha = 0.1f
        }

        if (enableSheep) {
            menu_sheep.setOnClickListener { it!!.findNavController().navigate(R.id.action_mainActivityFragment_to_gameSheepFragment) }
        } else {
            Logger.i("Sheep game is not enabled")
            menu_sheep.alpha = 0.1f
        }

        if (enableSpace) {
            menu_space.setOnClickListener { it!!.findNavController().navigate(R.id.action_mainActivityFragment_to_gameSpaceFragment) }
        } else {
            Logger.i("Space game is not enabled")
            menu_space.alpha = 0.1f
        }

        if (enableToad) {
            menu_toad.setOnClickListener { it!!.findNavController().navigate(R.id.action_mainActivityFragment_to_gameToadFragment) }
        } else {
            Logger.i("Toad game is not enabled")
            menu_toad.alpha = 0.1f
        }

        super.onViewCreated(view, savedInstanceState)

    }

}
