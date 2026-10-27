package com.cyrillrx.rpg.dice.data

import app.cash.sqldelight.db.SqlDriver
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.dice.domain.DiceComposition
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.MAX_MODIFIER
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class SQLDelightDiceCompositionRepositoryTest {

    private val driver: SqlDriver = TestDatabaseDriverFactory().createDriver()
    private val repository = SQLDelightDiceCompositionRepository(
        object : DatabaseDriverFactory {
            override fun createDriver(): SqlDriver = driver
        },
    )

    @Test
    fun `nothing saved yet loads an empty composition`() = runTest {
        assertEquals(DiceComposition(), repository.load())
    }

    @Test
    fun `a saved composition loads back`() = runTest {
        val composition = DiceComposition(countByDie = mapOf(Die.D20 to 1, Die.D6 to 3), modifier = -2)

        repository.save(composition)

        assertEquals(composition, repository.load())
    }

    @Test
    fun `a modifier without dice loads back`() = runTest {
        repository.save(DiceComposition(modifier = 4))

        assertEquals(DiceComposition(modifier = 4), repository.load())
    }

    @Test
    fun `a later save replaces the previous one`() = runTest {
        repository.save(DiceComposition(countByDie = mapOf(Die.D8 to 2), modifier = 1))
        repository.save(DiceComposition(countByDie = mapOf(Die.D4 to 1)))

        assertEquals(DiceComposition(countByDie = mapOf(Die.D4 to 1)), repository.load())
    }

    @Test
    fun `dice the notation no longer reads are dropped but the modifier is kept`() = runTest {
        storeRow(dice = "3d7", modifier = 2)

        assertEquals(DiceComposition(modifier = 2), repository.load())
    }

    @Test
    fun `a modifier beyond the limit is brought back to it`() = runTest {
        storeRow(dice = "1d20", modifier = MAX_MODIFIER + 5L)

        assertEquals(DiceComposition(countByDie = mapOf(Die.D20 to 1), modifier = MAX_MODIFIER), repository.load())
    }

    private fun storeRow(dice: String, modifier: Long) {
        driver.execute(null, "INSERT OR REPLACE INTO DiceComposition (id, dice, modifier) VALUES (1, ?, ?)", 2) {
            bindString(0, dice)
            bindLong(1, modifier)
        }
    }
}
