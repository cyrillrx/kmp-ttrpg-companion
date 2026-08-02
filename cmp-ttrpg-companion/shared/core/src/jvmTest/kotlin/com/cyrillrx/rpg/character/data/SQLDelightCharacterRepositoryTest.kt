package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.Instant
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock

class SQLDelightCharacterRepositoryTest {

    private class MutableClock(var instant: Instant) : Clock {
        override fun now(): Instant = instant
    }

    private fun buildRepository(clock: Clock = Clock.System) =
        SQLDelightCharacterRepository(TestDatabaseDriverFactory(), clock = clock)

    @Test
    fun `save and getAll returns all saved characters`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        repository.save(rogue)

        val result = repository.getAll(null).map { it.value }
        assertEquals(2, result.size)
        assertTrue(result.contains(fighter))
        assertTrue(result.contains(rogue))
    }

    @Test
    fun `get returns character by id`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)

        assertEquals(fighter, repository.get(fighter.id))
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        val repository = buildRepository()
        assertNull(repository.get("missing"))
    }

    @Test
    fun `save updates an existing character`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        val updated = fighter.copy(currentHitPoints = 5, temporaryHitPoints = 2)
        repository.save(updated)

        assertEquals(updated, repository.get(fighter.id))
    }

    @Test
    fun `delete removes character by id`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        repository.delete(fighter.id)

        assertNull(repository.get(fighter.id))
        assertTrue(repository.getAll(null).isEmpty())
    }

    @Test
    fun `getAll with filter returns only matching characters`() = runTest {
        val repository = buildRepository()
        repository.save(SampleCharacterRepository.humanFighter())
        repository.save(SampleCharacterRepository.elfRogue())

        val result = repository.getAll(CharacterFilter(query = "Lyra"))
        assertEquals(1, result.size)
        assertEquals("Lyra Vossen", result.first().value.name)
    }

    @Test
    fun `getByIds returns only the requested characters`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        repository.save(rogue)

        val result = repository.getByIds(listOf(fighter.id))
        assertEquals(1, result.size)
        assertEquals(fighter, result.first())
    }

    @Test
    fun `getAll returns characters ordered by updatedAt descending`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        clock.instant = Instant.fromEpochMilliseconds(2_000L)
        repository.save(rogue)

        val result = repository.getAll(null)
        assertEquals(rogue, result.first().value)
        assertEquals(fighter, result.last().value)
    }

    @Test
    fun `save preserves createdAt and advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(fighter.copy(currentHitPoints = 1))

        val stored = repository.getAll(null).single()
        assertEquals(Instant.fromEpochMilliseconds(1_000L), stored.createdAt)
        assertEquals(Instant.fromEpochMilliseconds(5_000L), stored.updatedAt)
    }
}
