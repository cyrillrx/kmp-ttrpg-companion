package com.cyrillrx.rpg.dice.data

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.DiceRollerDraft
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SQLDelightDiceRollerDraftRepositoryTest {

    private val driver: SqlDriver = TestDatabaseDriverFactory().createDriver()
    private val repository = SQLDelightDiceRollerDraftRepository(
        object : DatabaseDriverFactory {
            override fun createDriver(): SqlDriver = driver
        },
    )

    @Test
    fun `nothing saved yet loads an empty draft`() = runTest {
        assertEquals(DiceRollerDraft(), repository.load())
    }

    @Test
    fun `a saved draft loads back`() = runTest {
        val draft = DiceRollerDraft(countByDie = mapOf(Die.D20 to 1, Die.D6 to 3), modifier = -2)

        repository.save(draft)

        assertEquals(draft, repository.load())
    }

    @Test
    fun `a modifier without dice loads back`() = runTest {
        repository.save(DiceRollerDraft(modifier = 4))

        assertEquals(DiceRollerDraft(modifier = 4), repository.load())
    }

    @Test
    fun `a later save replaces the previous one`() = runTest {
        repository.save(DiceRollerDraft(countByDie = mapOf(Die.D8 to 2), modifier = 1))
        repository.save(DiceRollerDraft(countByDie = mapOf(Die.D4 to 1)))

        assertEquals(DiceRollerDraft(countByDie = mapOf(Die.D4 to 1)), repository.load())
    }

    @Test
    fun `dice the notation no longer reads are dropped but the modifier is kept`() = runTest {
        storeRow(dice = "3d7", modifier = 2)

        assertEquals(DiceRollerDraft(modifier = 2), repository.load())
    }

    @Test
    fun `a modifier beyond the limit is brought back to it`() = runTest {
        storeRow(dice = "1d20", modifier = MAX_MODIFIER + 5L)

        assertEquals(DiceRollerDraft(countByDie = mapOf(Die.D20 to 1), modifier = MAX_MODIFIER), repository.load())
    }

    private fun storeRow(dice: String, modifier: Long) {
        driver.execute(
            identifier = null,
            sql = "INSERT OR REPLACE INTO DiceRollerDraft (id, dice, modifier) VALUES (1, ?, ?)",
            parameters = 2,
        ) {
            bindString(index = 0, string = dice)
            bindLong(index = 1, long = modifier)
        }
    }
}
