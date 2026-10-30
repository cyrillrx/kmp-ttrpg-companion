package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.DiceRollerDraft
import com.cyrillrx.rpg.dice.domain.Die
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RamDiceRollerDraftRepositoryTest {

    @Test
    fun `nothing saved yet loads an empty draft`() = runTest {
        assertEquals(DiceRollerDraft(), RamDiceRollerDraftRepository().load())
    }

    @Test
    fun `a saved draft loads back`() = runTest {
        val repository = RamDiceRollerDraftRepository()
        val draft = DiceRollerDraft(countByDie = mapOf(Die.D12 to 2), modifier = -1)

        repository.save(draft)

        assertEquals(draft, repository.load())
    }
}
