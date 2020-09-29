package com.orange.labs.orangetrainingbox.ui

import org.junit.Assert.assertEquals
import org.junit.Test

/**
* To test [MainActivity] class.
*
* @author Pierre-Yves Lapersonne
* @since 23/03/2019
* @version 1.0.0
*/
class UnitTestMainActivity {

    /**
     *
     */
    @Test
    fun `code for bluetooth enabling should be equal to 42`() {
        assertEquals(42, REQUEST_ENABLE_BT)
    }

    /**
     *
     */
    @Test
    fun `code for bluetooth permission should be equal to 41`() {
        assertEquals(41, REQUEST_BT_PERMISSION)
    }

    /**
     *
     */
    @Test
    fun `code for settings should be equal to 1337`() {
        assertEquals(1337, REQUEST_SETTINGS)
    }

}