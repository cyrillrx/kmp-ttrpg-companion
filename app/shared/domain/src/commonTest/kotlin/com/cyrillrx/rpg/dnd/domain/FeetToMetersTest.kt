package com.cyrillrx.rpg.dnd.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class FeetToMetersTest {

    @Test
    fun `converts valid feet distances to the correct meter values`() {
        assertEquals(7.5f, 25.feetToMeters())
        assertEquals(9f, 30.feetToMeters())
        assertEquals(10.5f, 35.feetToMeters())
        assertEquals(12f, 40.feetToMeters())
        assertEquals(13.5f, 45.feetToMeters())
        assertEquals(15f, 50.feetToMeters())
        assertEquals(18f, 60.feetToMeters())
        assertEquals(27f, 90.feetToMeters())
        assertEquals(36f, 120.feetToMeters())
    }
}
