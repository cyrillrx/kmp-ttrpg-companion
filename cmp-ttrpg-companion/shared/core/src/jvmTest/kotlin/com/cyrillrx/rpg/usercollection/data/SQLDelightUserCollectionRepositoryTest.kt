package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.data.cache.TestDatabaseDriverFactory
import com.cyrillrx.rpg.core.domain.MutableClock
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class SQLDelightUserCollectionRepositoryTest {

    private fun buildRepository(clock: Clock = Clock.System) =
        SQLDelightUserCollectionRepository(TestDatabaseDriverFactory(), clock = clock)

    private fun spellList(id: String = "1", name: String = "Spellbook", itemIds: List<String> = emptyList()) =
        UserCollection(id = id, name = name, type = UserCollection.Type.SPELL, itemIds = itemIds)

    @Test
    fun `save and getAll returns lists filtered by type`() = runTest {
        val repository = buildRepository()
        val spells = spellList()
        val items = UserCollection(
            id = "2",
            name = "Artefacts",
            type = UserCollection.Type.MAGICAL_ITEM,
            itemIds = emptyList(),
        )

        repository.save(spells)
        repository.save(items)

        assertEquals(expected = spells, actual = repository.getAll(UserCollection.Type.SPELL).single().value)
        assertEquals(expected = items, actual = repository.getAll(UserCollection.Type.MAGICAL_ITEM).single().value)
        assertTrue(repository.getAll(UserCollection.Type.MONSTER).isEmpty())
    }

    @Test
    fun `get returns the list by id`() = runTest {
        val repository = buildRepository()
        val list = spellList(id = "abc", itemIds = listOf("Fireball", "Thunderwave"))

        repository.save(list)

        assertEquals(expected = list, actual = repository.get("abc"))
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        assertNull(buildRepository().get("missing"))
    }

    @Test
    fun `save replaces the list with the same id`() = runTest {
        val repository = buildRepository()
        val list = spellList()

        repository.save(list)
        val updated = list.copy(itemIds = listOf("spell1", "spell2"))
        repository.save(updated)

        assertEquals(expected = updated, actual = repository.get("1"))
        assertEquals(expected = 1, actual = repository.getAll(UserCollection.Type.SPELL).size)
    }

    @Test
    fun `delete removes the list by id`() = runTest {
        val repository = buildRepository()

        repository.save(spellList())
        repository.delete("1")

        assertNull(repository.get("1"))
        assertTrue(repository.getAll(UserCollection.Type.SPELL).isEmpty())
    }

    @Test
    fun `addToList appends the item and removeFromList drops it`() = runTest {
        val repository = buildRepository()
        val list = spellList()
        repository.save(list)

        assertEquals(
            expected = UserCollectionRepository.Result.Success,
            actual = repository.addToList(repository.get("1")!!, "Fireball"),
        )
        assertEquals(expected = listOf("Fireball"), actual = repository.get("1")?.itemIds)

        assertEquals(
            expected = UserCollectionRepository.Result.Success,
            actual = repository.removeFromList("1", "Fireball"),
        )
        assertEquals(expected = emptyList(), actual = repository.get("1")?.itemIds)
    }

    @Test
    fun `removeFromList reports a missing list`() = runTest {
        assertEquals(
            expected = UserCollectionRepository.Result.NotFound,
            actual = buildRepository().removeFromList("missing", "Fireball"),
        )
    }

    @Test
    fun `save advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val list = spellList()

        repository.save(list)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(list.copy(itemIds = listOf("spell1")))

        val stored = repository.getAll(UserCollection.Type.SPELL).single()
        assertEquals(expected = Instant.fromEpochMilliseconds(5_000L), actual = stored.updatedAt)
    }
}
