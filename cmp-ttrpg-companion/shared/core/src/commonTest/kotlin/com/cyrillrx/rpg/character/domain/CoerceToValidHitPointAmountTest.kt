package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class CoerceToValidHitPointAmountTest {

    @Test
    fun `clamps negative amounts to 0`() {
        assertEquals(0, (-1).coerceToValidHitPointAmount())
        assertEquals(0, (-50).coerceToValidHitPointAmount())
    }

    @Test
    fun `clamps amounts above 999 to 999`() {
        assertEquals(999, 1_000.coerceToValidHitPointAmount())
        assertEquals(999, 10_000.coerceToValidHitPointAmount())
    }

    @Test
    fun `returns valid amounts unchanged`() {
        assertEquals(0, 0.coerceToValidHitPointAmount())
        assertEquals(35, 35.coerceToValidHitPointAmount())
        assertEquals(999, 999.coerceToValidHitPointAmount())
    }
}
