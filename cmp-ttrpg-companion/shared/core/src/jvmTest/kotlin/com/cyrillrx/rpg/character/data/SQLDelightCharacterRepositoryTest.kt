package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SQLDelightCharacterRepositoryTest {

    private fun buildRepository() = SQLDelightCharacterRepository(TestDatabaseDriverFactory())

    @Test
    fun `save and getAll returns all saved characters`() = runTest {
        val repository = buildRepository()
        val fighter = SampleCharacterRepository.humanFighter()
        val rogue = SampleCharacterRepository.elfRogue()

        repository.save(fighter)
        repository.save(rogue)

        val result = repository.getAll(null)
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
        assertEquals("Lyra Vossen", result.first().name)
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
}
