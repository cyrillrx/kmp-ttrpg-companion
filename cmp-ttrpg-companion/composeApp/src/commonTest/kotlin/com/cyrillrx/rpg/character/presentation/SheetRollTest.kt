package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SheetRollTest {

    @Test
    fun `a plain d20 has no keep rule`() {
        assertNull(sheetRoll(SimpleRoll(Die.D20, 12)).keepRule())
    }

    @Test
    fun `a roll with advantage keeps the higher`() {
        val advantage = DoubleRoll(SimpleRoll(Die.D20, 15), SimpleRoll(Die.D20, 8), KeepRule.HIGHER)

        assertEquals(KeepRule.HIGHER, sheetRoll(advantage).keepRule())
    }

    @Test
    fun `a roll with disadvantage keeps the lower`() {
        val disadvantage = DoubleRoll(SimpleRoll(Die.D20, 8), SimpleRoll(Die.D20, 15), KeepRule.LOWER)

        assertEquals(KeepRule.LOWER, sheetRoll(disadvantage).keepRule())
    }

    private fun sheetRoll(roll: DieRoll) = SheetRoll(D20Check.Initiative, RollPool(listOf(roll), modifier = 2), 1)
}
