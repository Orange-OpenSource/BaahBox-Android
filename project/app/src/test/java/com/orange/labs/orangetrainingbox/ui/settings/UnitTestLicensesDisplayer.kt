package com.orange.labs.orangetrainingbox.ui.settings

import com.orange.labs.orangetrainingbox.MockUtils.Companion.mockActivityWithGoodLicenses
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * To test [LicensesDisplayer] class.
 *
 * @since 23/03/2019
 * @version 1.0.0
 */
class UnitTestLicensesDisplayer {

    /**
     * Test the prepareNotices() method.
     * Expects with the mock data to have 4 notices all managed.
     */
    @Test
    fun `should contain all expected entries`() {
        // Given
        val activity = mockActivityWithGoodLicenses(false)
        val displayer = LicensesDisplayer()
        // When
        val notices = displayer.prepareNotices(activity)
        // Then
        assertNotNull(notices)
        assertTrue(notices.notices.size == 4)
    }

    /**
     * Test the prepareNotices() method.
     * Expects with the mock data to have 5 notices with one not managed.
     */
    @Test (expected = LicensesDisplayer.LicenseNotFoundException::class)
    fun `should throw LicenseNotFoundException if one license is not managed`() {
        // Given
        val activity = mockActivityWithGoodLicenses(true)
        val displayer = LicensesDisplayer()
        // When - Then
        val notices = displayer.prepareNotices(activity)
    }

}