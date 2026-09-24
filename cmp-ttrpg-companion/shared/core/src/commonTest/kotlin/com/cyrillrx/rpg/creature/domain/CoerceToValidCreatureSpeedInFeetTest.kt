package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidCreatureSpeedInFeetTest {

    @Test
    fun `clamps negative values to 0`() {
        assertEquals(0, (-5).coerceToValidCreatureSpeedInFeet())
        assertEquals(0, (-100).coerceToValidCreatureSpeedInFeet())
    }

    @Test
    fun `clamps values above 200 to 200`() {
        assertEquals(200, 205.coerceToValidCreatureSpeedInFeet())
        assertEquals(200, 9999.coerceToValidCreatureSpeedInFeet())
    }

    @Test
    fun `rounds to the nearest five-foot step`() {
        assertEquals(35, 33.coerceToValidCreatureSpeedInFeet())
        assertEquals(30, 32.coerceToValidCreatureSpeedInFeet())
        assertEquals(5, 3.coerceToValidCreatureSpeedInFeet())
    }

    @Test
    fun `leaves the range the bestiary actually uses unchanged`() {
        assertEquals(5, 5.coerceToValidCreatureSpeedInFeet())
        assertEquals(30, 30.coerceToValidCreatureSpeedInFeet())
        assertEquals(150, 150.coerceToValidCreatureSpeedInFeet())
    }
}
