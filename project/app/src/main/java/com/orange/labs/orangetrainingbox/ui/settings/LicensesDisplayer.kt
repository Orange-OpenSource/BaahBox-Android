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

package com.orange.labs.orangetrainingbox.ui.settings

import android.app.Activity
import com.orange.labs.orangetrainingbox.R
import de.psdev.licensesdialog.LicensesDialog
import de.psdev.licensesdialog.licenses.*
import de.psdev.licensesdialog.model.Notice
import de.psdev.licensesdialog.model.Notices
import java.lang.Exception

/**
 * Displays using the LicensesDialog library (https://github.com/PSDev/LicensesDialog) the third party licenses we use.
 *
 * @author Pierre-Yves Lapersonne
 * @since 23/05/2019
 * @version 2.0.0
 */
class LicensesDisplayer {

    /**
     * Displays the licenses in use
     *
     * @param context - The context to use, the Activity where the display starts
     */
    fun displayLicenses(context: Activity) {

        // The data to display

        val names = context.resources.getStringArray(R.array.credits_names)
        val copyrights = context.resources.getStringArray(R.array.credits_copyrights)
        val licenses = context.resources.getStringArray(R.array.credits_licenses)
        val urls = context.resources.getStringArray(R.array.credits_url)

        // The dialog

        val notices = Notices()

        // Assuming the developer of this feature is not a jackass and has written the same
        // amount of entries in each array
        for (i in 0 until names.size) {
            val licence: License = when (licenses[i]) {
                "Apache 2.0" -> ApacheSoftwareLicense20()
                "EPL 1.0" -> EclipsePublicLicense10()
                "GPL 3.0" -> GnuGeneralPublicLicense30()
                "MIT" -> MITLicense()
                else -> throw LicenseNotFoundException("Not found license for ${licenses[i]}")
            }
            notices.addNotice(Notice(names[i], urls[i], copyrights[i], licence))
        }

        // Display them

        LicensesDialog.Builder(context)
            .setNotices(notices)
            .setIncludeOwnLicense(false)
            .setThemeResourceId(R.style.LicensesDialogTheme)
            .build()
            .show()

    }

    /**
     * To throw if the license is not managed
     *
     * @param message Details to add to the exception
     */
    class LicenseNotFoundException(message: String) : Exception(message)

}
