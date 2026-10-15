package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DieRollTest {
    @Test
    fun `the kept result is the one at the kept index`() {
        assertEquals(18, DieRoll(Die.D20, listOf(5, 18), keptIndex = 1).kept)
    }

    @Test
    fun `a kept index outside the results is refused`() {
        assertFailsWith<IllegalArgumentException> { DieRoll(Die.D4, listOf(2), keptIndex = 1) }
    }
}
