package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DiceHistoryStateTest {

    private val today = LocalDate(year = 2026, month = 11, day = 5)
    private val yesterday = LocalDate(year = 2026, month = 11, day = 4)

    @Test
    fun `the roll is taken out of its day`() {
        val state =
            history(RollDay(today, listOf(roll(id = 3), roll(id = 2))), RollDay(yesterday, listOf(roll(id = 1))))

        assertEquals(listOf(listOf(2L), listOf(1L)), state.without(rollId = 3).rollIds())
    }

    @Test
    fun `a day left without rolls is dropped`() {
        val state = history(RollDay(today, listOf(roll(id = 2))), RollDay(yesterday, listOf(roll(id = 1))))

        assertEquals(listOf(yesterday), state.without(rollId = 2).days().map(RollDay::date))
    }

    @Test
    fun `a history emptied this way shows as empty`() {
        val state = history(RollDay(today, listOf(roll(id = 1))))

        assertEquals(DiceHistoryState.Body.Empty, state.without(rollId = 1).body)
    }

    @Test
    fun `a history emptied this way can still be cleared`() {
        val state = history(RollDay(today, listOf(roll(id = 1))))

        assertTrue(state.without(rollId = 1).canClear)
    }

    @Test
    fun `an empty history cannot be cleared`() {
        assertFalse(DiceHistoryState(DiceHistoryState.Body.Empty).canClear)
    }

    @Test
    fun `no roll id leaves the history unchanged`() {
        val state = history(RollDay(today, listOf(roll(id = 1))))

        assertEquals(state, state.without(rollId = null))
    }

    @Test
    fun `a history without data is left unchanged`() {
        val state = DiceHistoryState(DiceHistoryState.Body.Empty)

        assertEquals(state, state.without(rollId = 1))
    }

    private fun history(vararg days: RollDay) = DiceHistoryState(DiceHistoryState.Body.WithData(days.toList(), today))

    private fun roll(id: Long) = DayRoll(
        id,
        RollPool(listOf(SimpleRoll(Die.D6, value = 1))),
        LocalTime(hour = 9, minute = 0),
    )

    private fun DiceHistoryState.days() = (body as DiceHistoryState.Body.WithData).days

    private fun DiceHistoryState.rollIds() = days().map { day -> day.rolls.map(DayRoll::id) }
}
