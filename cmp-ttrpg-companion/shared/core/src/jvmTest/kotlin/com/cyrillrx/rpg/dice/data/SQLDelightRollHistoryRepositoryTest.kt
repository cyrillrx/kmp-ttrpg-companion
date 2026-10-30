package com.cyrillrx.rpg.dice.data

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.MAX_RECORDED_ROLLS
import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Clock
import kotlin.time.Instant

class SQLDelightRollHistoryRepositoryTest {

    private val driver: SqlDriver = TestDatabaseDriverFactory().createDriver()
    private val repository = SQLDelightRollHistoryRepository(
        databaseDriverFactory = object : DatabaseDriverFactory {
            override fun createDriver(): SqlDriver = driver
        },
        clock = FixedClock,
    )

    @Test
    fun `nothing recorded yet reads an empty history`() = runTest {
        assertEquals(emptyList(), repository.getAll())
    }

    @Test
    fun `a recorded roll reads back with when it was rolled`() = runTest {
        val pool = RollPool(listOf(SimpleRoll(Die.D20, 14), SimpleRoll(Die.D6, 3)), modifier = -2)

        repository.record(pool)

        assertEquals(listOf(RecordedRoll(id = 1, pool = pool, rolledAt = FixedClock.now())), repository.getAll())
    }

    @Test
    fun `a double roll reads back with its discarded roll and its rule`() = runTest {
        val advantage = DoubleRoll(kept = SimpleRoll(Die.D20, 9), discarded = SimpleRoll(Die.D20, 9), KeepRule.LOWER)
        val pool = RollPool(listOf(advantage), modifier = 5)

        repository.record(pool)

        assertEquals(listOf(pool), repository.getAll().map(RecordedRoll::pool))
    }

    @Test
    fun `the history reads newest first`() = runTest {
        repository.record(d4Pool(value = 1))
        repository.record(d4Pool(value = 2))
        repository.record(d4Pool(value = 3))

        assertEquals(listOf(3, 2, 1), repository.getAll().map { it.pool.total() })
    }

    @Test
    fun `recording beyond the limit forgets the oldest rolls`() = runTest {
        repeat(MAX_RECORDED_ROLLS + 2) { repository.record(d4Pool(value = 1 + it % 4)) }

        val history = repository.getAll()

        assertEquals(MAX_RECORDED_ROLLS, history.size)
        assertEquals(MAX_RECORDED_ROLLS + 2L, history.first().id)
        assertEquals(3L, history.last().id)
    }

    @Test
    fun `a stored roll that no longer reads is skipped`() = runTest {
        repository.record(d4Pool(value = 2))
        storeRow(rolls = """[{"die":"D7","value":3}]""")
        storeRow(rolls = """[{"die":"D4","value":9}]""")

        assertEquals(listOf(d4Pool(value = 2)), repository.getAll().map(RecordedRoll::pool))
    }

    private fun d4Pool(value: Int) = RollPool(listOf(SimpleRoll(Die.D4, value)))

    private fun storeRow(rolls: String) {
        driver.execute(
            identifier = null,
            sql = "INSERT INTO DiceRoll (rolledAt, modifier, rolls) VALUES (0, 0, ?)",
            parameters = 1,
        ) {
            bindString(index = 0, string = rolls)
        }
    }

    private object FixedClock : Clock {
        override fun now(): Instant = Instant.fromEpochMilliseconds(1_700_000_000_000)
    }
}
