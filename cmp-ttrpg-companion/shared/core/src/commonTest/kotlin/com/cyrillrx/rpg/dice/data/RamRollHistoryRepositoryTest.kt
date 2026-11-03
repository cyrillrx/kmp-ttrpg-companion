package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.MAX_RECORDED_ROLLS
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class RamRollHistoryRepositoryTest {

    @Test
    fun `the history reads newest first`() = runTest {
        val repository = RamRollHistoryRepository()

        repository.record(d4Pool(value = 1))
        repository.record(d4Pool(value = 2))

        assertEquals(listOf(d4Pool(value = 2), d4Pool(value = 1)), repository.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `loading keeps the rolls already recorded`() = runTest {
        val repository = RamRollHistoryRepository()
        repository.record(d4Pool(value = 1))

        repository.load()

        assertEquals(listOf(d4Pool(value = 1)), repository.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `recording beyond the limit forgets the oldest rolls`() = runTest {
        val repository = RamRollHistoryRepository()

        repeat(MAX_RECORDED_ROLLS + 1) { repository.record(d4Pool(value = 1 + it % 4)) }

        val history = repository.rolls.value
        assertEquals(MAX_RECORDED_ROLLS, history.size)
        assertEquals(2L, history.last().id)
    }

    @Test
    fun `clearing up to a roll keeps only the newer ones`() = runTest {
        val repository = RamRollHistoryRepository()
        repeat(3) { repository.record(d4Pool(value = it + 1)) }

        repository.clearUpTo(id = 2)

        assertEquals(listOf(3L), repository.rolls.value.map(RecordedRoll::id))
    }

    private fun d4Pool(value: Int) = RollPool(listOf(SimpleRoll(Die.D4, value)))
}
