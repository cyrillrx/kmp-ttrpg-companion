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

    private fun spellCollection(id: String = "1", name: String = "My Spells", itemIds: List<String> = emptyList()) =
        UserCollection(id = id, name = name, type = UserCollection.Type.SPELL, itemIds = itemIds)

    @Test
    fun `save and getAll returns collections filtered by type`() = runTest {
        val repository = buildRepository()
        val spells = spellCollection()
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
    fun `get returns the collection by id`() = runTest {
        val repository = buildRepository()
        val collection = spellCollection(id = "abc", itemIds = listOf("Fireball", "Thunderwave"))

        repository.save(collection)

        assertEquals(expected = collection, actual = repository.get("abc"))
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        assertNull(buildRepository().get("missing"))
    }

    @Test
    fun `rename writes the name without rewriting the items`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        repository.save(spellCollection(itemIds = listOf("Fireball", "Thunderwave")))

        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        val result = repository.rename("1", "Combat Spells")

        assertEquals(expected = UserCollectionRepository.Result.Success, actual = result)
        val stored = repository.getAll(UserCollection.Type.SPELL).single()
        assertEquals(expected = "Combat Spells", actual = stored.value.name)
        assertEquals(expected = listOf("Fireball", "Thunderwave"), actual = stored.value.itemIds)
        assertEquals(expected = Instant.fromEpochMilliseconds(5_000L), actual = stored.updatedAt)
    }

    @Test
    fun `rename reports a missing collection`() = runTest {
        assertEquals(
            expected = UserCollectionRepository.Result.NotFound,
            actual = buildRepository().rename("missing", "Combat Spells"),
        )
    }

    @Test
    fun `save replaces the collection with the same id`() = runTest {
        val repository = buildRepository()
        val collection = spellCollection()

        repository.save(collection)
        val updated = collection.copy(itemIds = listOf("spell1", "spell2"))
        repository.save(updated)

        assertEquals(expected = updated, actual = repository.get("1"))
        assertEquals(expected = 1, actual = repository.getAll(UserCollection.Type.SPELL).size)
    }

    @Test
    fun `delete removes the collection by id`() = runTest {
        val repository = buildRepository()

        repository.save(spellCollection())
        repository.delete("1")

        assertNull(repository.get("1"))
        assertTrue(repository.getAll(UserCollection.Type.SPELL).isEmpty())
    }

    @Test
    fun `addToCollection appends the item and removeFromCollection drops it`() = runTest {
        val repository = buildRepository()
        val collection = spellCollection()
        repository.save(collection)

        assertEquals(
            expected = UserCollectionRepository.Result.Success,
            actual = repository.addToCollection(repository.get("1")!!, "Fireball"),
        )
        assertEquals(expected = listOf("Fireball"), actual = repository.get("1")?.itemIds)

        assertEquals(
            expected = UserCollectionRepository.Result.Success,
            actual = repository.removeFromCollection("1", "Fireball"),
        )
        assertEquals(expected = emptyList(), actual = repository.get("1")?.itemIds)
    }

    @Test
    fun `removeFromCollection reports a missing collection`() = runTest {
        assertEquals(
            expected = UserCollectionRepository.Result.NotFound,
            actual = buildRepository().removeFromCollection("missing", "Fireball"),
        )
    }

    @Test
    fun `save advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val collection = spellCollection()

        repository.save(collection)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(collection.copy(itemIds = listOf("spell1")))

        val stored = repository.getAll(UserCollection.Type.SPELL).single()
        assertEquals(expected = Instant.fromEpochMilliseconds(5_000L), actual = stored.updatedAt)
    }
}
