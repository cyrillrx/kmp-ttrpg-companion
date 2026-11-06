package com.cyrillrx.rpg.dice.data

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_RECORDED_ROLLS
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollContext
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class SQLDelightRollHistoryRepositoryTest {

    private val driver: SqlDriver = TestDatabaseDriverFactory().createDriver()
    private val repository = newRepository()

    @Test
    fun `nothing recorded yet reads an empty history`() = runTest {
        repository.load()

        assertEquals(emptyList(), repository.rolls.value)
    }

    @Test
    fun `loading reads the rolls recorded before`() = runTest {
        repository.record(d4Pool(value = 1))
        repository.record(d4Pool(value = 2))

        val reopened = newRepository()
        reopened.load()

        assertEquals(listOf(d4Pool(value = 2), d4Pool(value = 1)), reopened.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `a recorded roll reads back with when it was rolled`() = runTest {
        val pool = RollPool(listOf(SimpleRoll(Die.D20, value = 14), SimpleRoll(Die.D6, value = 3)), modifier = -2)

        repository.record(pool)

        assertEquals(listOf(RecordedRoll(id = 1, pool = pool, rolledAt = FixedClock.now())), repository.rolls.value)
    }

    @Test
    fun `a roll recorded for a check reads back with its context`() = runTest {
        val context = RollContext(characterId = "hero", check = D20Check.SkillCheck(Skill.PERCEPTION))

        repository.record(d4Pool(value = 2), context)
        val reopened = newRepository()
        reopened.load()

        assertEquals(listOf(context), reopened.rolls.value.map(RecordedRoll::context))
    }

    @Test
    fun `a roll recorded without a context reads back without one`() = runTest {
        repository.record(d4Pool(value = 2))
        repository.load()

        assertEquals(listOf(null), repository.rolls.value.map(RecordedRoll::context))
    }

    @Test
    fun `a stored check that no longer reads drops the context but keeps the roll`() = runTest {
        storeRow(roll = "1d4(2)", characterId = "hero", checkKey = "skill:juggling")
        repository.load()

        assertEquals(listOf(null), repository.rolls.value.map(RecordedRoll::context))
        assertEquals(listOf(d4Pool(value = 2)), repository.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `a double roll reads back with its discarded roll and its rule`() = runTest {
        val disadvantage =
            DoubleRoll(
                kept = SimpleRoll(Die.D20, value = 9),
                discarded = SimpleRoll(Die.D20, value = 9),
                KeepRule.LOWER,
            )
        val pool = RollPool(listOf(disadvantage), modifier = 5)

        repository.record(pool)

        assertEquals(listOf(pool), repository.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `recording returns the id the roll reads back with`() = runTest {
        repeat(MAX_RECORDED_ROLLS) { repository.record(d4Pool(value = 1)) }

        val id = repository.record(d4Pool(value = 2))

        assertEquals(repository.rolls.value.first().id, id)
    }

    @Test
    fun `the history reads newest first`() = runTest {
        repository.record(d4Pool(value = 1))
        repository.record(d4Pool(value = 2))
        repository.record(d4Pool(value = 3))

        assertEquals(listOf(3, 2, 1), repository.rolls.value.map { it.pool.total() })
    }

    @Test
    fun `recording beyond the limit forgets the oldest rolls`() = runTest {
        repeat(MAX_RECORDED_ROLLS + 2) { repository.record(d4Pool(value = 1 + it % 4)) }

        val history = repository.rolls.value

        assertEquals(MAX_RECORDED_ROLLS, history.size)
        assertEquals(MAX_RECORDED_ROLLS + 2L, history.first().id)
        assertEquals(3L, history.last().id)
    }

    @Test
    fun `a stored roll that no longer reads is skipped`() = runTest {
        repository.record(d4Pool(value = 2))
        storeRow(roll = "1d7(3)")
        storeRow(roll = "1d4(9)")
        repository.load()

        assertEquals(listOf(d4Pool(value = 2)), repository.rolls.value.map(RecordedRoll::pool))
    }

    @Test
    fun `clearing up to a roll forgets it and the older ones`() = runTest {
        repeat(3) { repository.record(d4Pool(value = it + 1)) }

        repository.clearUpTo(id = 2)

        assertEquals(listOf(3L), repository.rolls.value.map(RecordedRoll::id))
    }

    @Test
    fun `a cleared history stays cleared once reloaded`() = runTest {
        repeat(2) { repository.record(d4Pool(value = it + 1)) }

        repository.clearUpTo(id = 2)
        repository.load()

        assertEquals(emptyList(), repository.rolls.value)
    }

    private fun newRepository() = SQLDelightRollHistoryRepository(
        databaseDriverFactory = object : DatabaseDriverFactory {
            override fun createDriver(): SqlDriver = driver
        },
        clock = FixedClock,
    )

    private fun d4Pool(value: Int) = RollPool(listOf(SimpleRoll(Die.D4, value)))

    private fun storeRow(roll: String, characterId: String? = null, checkKey: String? = null) {
        driver.execute(
            identifier = null,
            sql = "INSERT INTO DiceRoll (rolledAt, roll, characterId, checkKey) VALUES (0, ?, ?, ?)",
            parameters = 3,
        ) {
            bindString(index = 0, string = roll)
            bindString(index = 1, string = characterId)
            bindString(index = 2, string = checkKey)
        }
    }

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.fromEpochMilliseconds(epochMilliseconds = 1_700_000_000_000)
    }
}
