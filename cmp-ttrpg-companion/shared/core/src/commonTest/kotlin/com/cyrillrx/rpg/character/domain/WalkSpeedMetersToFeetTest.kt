package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class WalkSpeedMetersToFeetTest {

    @Test
    fun `converts valid meter speeds to the correct feet values`() {
        assertEquals(25, 7.5f.walkSpeedMetersToFeet())
        assertEquals(30, 9f.walkSpeedMetersToFeet())
        assertEquals(35, 10.5f.walkSpeedMetersToFeet())
        assertEquals(40, 12f.walkSpeedMetersToFeet())
        assertEquals(45, 13.5f.walkSpeedMetersToFeet())
        assertEquals(50, 15f.walkSpeedMetersToFeet())
        assertEquals(60, 18f.walkSpeedMetersToFeet())
        assertEquals(90, 27f.walkSpeedMetersToFeet())
        assertEquals(120, 36f.walkSpeedMetersToFeet())
    }
}
