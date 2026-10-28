package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.Die
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RamDiceCompositionRepositoryTest {

    @Test
    fun `nothing saved yet loads an empty composition`() = runTest {
        assertEquals(DiceComposition(), RamDiceCompositionRepository().load())
    }

    @Test
    fun `a saved composition loads back`() = runTest {
        val repository = RamDiceCompositionRepository()
        val composition = DiceComposition(countByDie = mapOf(Die.D12 to 2), modifier = -1)

        repository.save(composition)

        assertEquals(composition, repository.load())
    }
}
