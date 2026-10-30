package com.cyrillrx.rpg.dice.domain

import kotlin.test.Test
import kotlin.test.assertFailsWith

class DiceCompositionTest {

    @Test
    fun `a die composed zero times is rejected`() {
        assertFailsWith<IllegalArgumentException> { DiceComposition(countByDie = mapOf(Die.D6 to 0)) }
    }

    @Test
    fun `more dice than the limit are rejected`() {
        assertFailsWith<IllegalArgumentException> {
            DiceComposition(countByDie = mapOf(Die.D6 to MAX_DICE_COUNT, Die.D8 to 1))
        }
    }

    @Test
    fun `a modifier beyond the limit is rejected`() {
        assertFailsWith<IllegalArgumentException> { DiceComposition(modifier = MAX_MODIFIER + 1) }
    }
}
