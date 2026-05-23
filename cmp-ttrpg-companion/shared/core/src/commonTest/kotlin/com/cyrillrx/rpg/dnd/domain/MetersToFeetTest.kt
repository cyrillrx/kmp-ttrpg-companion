package com.cyrillrx.rpg.dnd.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class MetersToFeetTest {

    @Test
    fun `converts valid meter distances to the correct feet values`() {
        assertEquals(25, 7.5f.metersToFeet())
        assertEquals(30, 9f.metersToFeet())
        assertEquals(35, 10.5f.metersToFeet())
        assertEquals(40, 12f.metersToFeet())
        assertEquals(45, 13.5f.metersToFeet())
        assertEquals(50, 15f.metersToFeet())
        assertEquals(60, 18f.metersToFeet())
        assertEquals(90, 27f.metersToFeet())
        assertEquals(120, 36f.metersToFeet())
    }
}
