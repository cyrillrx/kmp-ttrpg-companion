package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Instant

class DiceRollerStateTest {

    @Test
    fun `the roll on the card is left out once recorded`() {
        val state = DiceRollerState(lastRoll = d20(value = 3), lastRollId = 3, recentRolls = recorded(3, 2, 1))

        assertEquals(listOf(2, 1), state.previousRolls().map(::totalOf))
    }

    @Test
    fun `the newest recorded roll precedes the roll on the card until that one is recorded`() {
        val state = DiceRollerState(lastRoll = d20(value = 4), recentRolls = recorded(3, 2, 1))

        assertEquals(listOf(3, 2), state.previousRolls().map(::totalOf))
    }

    @Test
    fun `an earlier roll with the same result is not mistaken for the recorded roll on the card`() {
        val state = DiceRollerState(lastRoll = d20(value = 3), lastRollId = 9, recentRolls = recorded(3, 2, 1))

        assertEquals(listOf(3, 2), state.previousRolls().map(::totalOf))
    }

    @Test
    fun `with no roll on the card the newest recorded rolls are shown`() {
        val state = DiceRollerState(recentRolls = recorded(3, 2, 1))

        assertEquals(listOf(3, 2), state.previousRolls().map(::totalOf))
    }

    @Test
    fun `with no recorded roll there is nothing to show`() {
        val state = DiceRollerState(lastRoll = d20(value = 3))

        assertEquals(emptyList(), state.previousRolls())
    }

    @Test
    fun `the roll on the card leaves it once cleared from the history`() {
        val state =
            DiceRollerState(lastRoll = d20(value = 3), rollCount = 1, lastRollId = 3, recentRolls = recorded(3, 2))

        val cleared = state.withHistory(emptyList())

        assertNull(cleared.lastRoll)
        assertNull(cleared.lastRollId)
    }

    @Test
    fun `the roll on the card stays while it is still in the history`() {
        val state = DiceRollerState(lastRoll = d20(value = 3), rollCount = 1, lastRollId = 3)

        assertEquals(d20(value = 3), state.withHistory(recorded(3, 2)).lastRoll)
    }

    @Test
    fun `a roll not recorded yet stays on the card whatever the history reads`() {
        val state = DiceRollerState(lastRoll = d20(value = 3), rollCount = 1)

        assertEquals(d20(value = 3), state.withHistory(emptyList()).lastRoll)
    }

    @Test
    fun `an id recorded for an earlier roll is not given to the one on the card`() {
        val state = DiceRollerState(lastRoll = d20(value = 3), rollCount = 2)

        assertNull(state.withLastRollRecorded(rollCount = 1, id = 7).lastRollId)
    }

    private fun totalOf(recorded: RecordedRoll): Int = recorded.pool.total()

    private fun d20(value: Int) = RollPool(listOf(SimpleRoll(Die.D20, value)))

    private fun recorded(vararg values: Int): List<RecordedRoll> = values.map { value ->
        RecordedRoll(
            id = value.toLong(),
            pool = d20(value),
            rolledAt = Instant.fromEpochMilliseconds(epochMilliseconds = 0),
        )
    }
}
