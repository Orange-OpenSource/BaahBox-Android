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

import android.graphics.PorterDuff
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.forEach
import androidx.fragment.app.Fragment

/**
 * Class which models a fragment with a customizable theme.
 *
 * @author Marc Poppleton
 * @author Pierre-Yves Lapersonne
 * @since 27/05/2019
 * @version 1.1.0
 * @see [Fragment]
 */
abstract class AbstractThemedFragment : Fragment() {

    /**
     * The identifier of the color to use to theme the toolbar and the icons
     */
    abstract val themingColor: Int

    /**
     * The identifier of the string resource pointing to the title to display
     */
    abstract val screenTitle: Int

    /**
     * Fragment lifecycle.
     * Defines the action bar, the title and the color to use.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        //(activity as AppCompatActivity).setSupportActionBar(toolbar)
        val title = getString(screenTitle)
        val coloredTitle = SpannableString(title)
        coloredTitle.setSpan(ForegroundColorSpan(resources.getColor(themingColor)), 0, title.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        (activity as AppCompatActivity).supportActionBar?.title = coloredTitle
    }

    /**
     * Deals with the option menu, and applies to menu items the colors.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.forEach { menuItem ->
            menuItem.icon?.let {
                it.mutate()
                it.setColorFilter(resources.getColor(themingColor), PorterDuff.Mode.SRC_ATOP)
            }
        }
    }

}