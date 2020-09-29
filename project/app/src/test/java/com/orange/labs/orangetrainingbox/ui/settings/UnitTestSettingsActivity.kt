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

package com.orange.labs.orangetrainingbox.ui.settings

import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockSettingsActivity
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * To test [SettingsActivity] class.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/03/2019
 * @version 1.0.0
 */
class UnitTestSettingsActivity {

    /**
     * Checks how is built the version details
     */
    @Test
    fun `version details should contain name and code`() {
        // Given
        val name = "happy-sheep"
        val code = 8
        val activity = mockSettingsActivity(name, code)
        // When
        val versionDetails = activity.buildReleaseString(name, code)
        // Then
        assertEquals(versionDetails, "Version $name - Build $code")
    }

}