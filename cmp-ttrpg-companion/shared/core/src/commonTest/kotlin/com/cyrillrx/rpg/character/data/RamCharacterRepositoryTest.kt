package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.core.domain.MutableClock
import com.cyrillrx.rpg.core.domain.values
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class RamCharacterRepositoryTest {

    private fun buildRepository(clock: Clock = Clock.System) = RamCharacterRepository(clock)

    @Test
    fun `save and getAll returns all saved characters`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        repository.save(rogue)

        val result = repository.getAll(null).values()
        assertEquals(expected = 2, actual = result.size)
        assertTrue(result.contains(fighter))
        assertTrue(result.contains(rogue))
    }

    @Test
    fun `get returns the character by id`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)

        assertEquals(expected = fighter, actual = repository.get(fighter.id))
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        assertNull(buildRepository().get("missing"))
    }

    @Test
    fun `getByIds returns only the known characters in the requested order`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        repository.save(rogue)

        val result = repository.getByIds(listOf(rogue.id, "missing", fighter.id))
        assertEquals(expected = listOf(rogue, fighter), actual = result)
    }

    @Test
    fun `save replaces the character with the same id`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        repository.save(fighter.copy(currentHitPoints = 1))

        val stored = repository.getAll(null).single()
        assertEquals(expected = 1, actual = stored.value.currentHitPoints)
    }

    @Test
    fun `delete removes the character by id`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        repository.delete(fighter.id)

        assertNull(repository.get(fighter.id))
        assertTrue(repository.getAll(null).isEmpty())
    }

    @Test
    fun `getAll applies the name filter`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        repository.save(SampleCharacterRepository.elfRogue())

        val result = repository.getAll(CharacterFilter(query = fighter.name))
        assertEquals(expected = 1, actual = result.size)
        assertEquals(expected = fighter, actual = result.single().value)
    }

    @Test
    fun `save advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val fighter = SampleCharacterRepository.humanFighter()

        repository.save(fighter)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(fighter.copy(currentHitPoints = 1))

        val stored = repository.getAll(null).single()
        assertEquals(expected = Instant.fromEpochMilliseconds(5_000L), actual = stored.updatedAt)
    }
}
