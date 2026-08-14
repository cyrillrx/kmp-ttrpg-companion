package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.domain.MutableClock
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class RamUserCollectionRepositoryTest {

    private fun buildRepository(clock: Clock = Clock.System) = RamUserCollectionRepository(clock = clock)

    @Test
    fun `save and getAll return collections filtered by type`() = runTest {
        val repository = buildRepository()

        val spellCollection = UserCollection(
            id = "1",
            name = "My Spells",
            type = UserCollection.Type.SPELL,
            itemIds = emptyList(),
        )
        val itemCollection = UserCollection(
            id = "2",
            name = "Artefacts",
            type = UserCollection.Type.MAGICAL_ITEM,
            itemIds = emptyList(),
        )

        repository.save(spellCollection)
        repository.save(itemCollection)

        val spellCollections = repository.getAll(UserCollection.Type.SPELL)
        assertEquals(expected = 1, actual = spellCollections.size)
        assertEquals(expected = spellCollection, actual = spellCollections.first().value)

        val itemCollections = repository.getAll(UserCollection.Type.MAGICAL_ITEM)
        assertEquals(expected = 1, actual = itemCollections.size)
        assertEquals(expected = itemCollection, actual = itemCollections.first().value)
    }

    @Test
    fun `get returns the collection by id`() = runTest {
        val repository = buildRepository()
        val collection =
            UserCollection(id = "abc", name = "Test", type = UserCollection.Type.SPELL, itemIds = emptyList())

        repository.save(collection)

        val result = repository.get("abc")
        assertEquals(expected = collection, actual = result)
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        val repository = buildRepository()
        assertNull(repository.get("missing"))
    }

    @Test
    fun `initial collections are keyed by id and keep their timestamp`() = runTest {
        val collection =
            UserCollection(id = "seed", name = "Seeded", type = UserCollection.Type.SPELL, itemIds = listOf("Fireball"))
        val updatedAt = Instant.parse("2024-01-15T10:30:00Z")
        val repository = RamUserCollectionRepository(listOf(Stored(value = collection, updatedAt = updatedAt)))

        assertEquals(expected = collection, actual = repository.get("seed"))

        val stored = repository.getAll(UserCollection.Type.SPELL)
        assertEquals(expected = 1, actual = stored.size)
        assertEquals(expected = updatedAt, actual = stored.first().updatedAt)
    }

    @Test
    fun `seeded collections stay per instance`() = runTest {
        val collection =
            UserCollection(id = "seed", name = "Seeded", type = UserCollection.Type.SPELL, itemIds = emptyList())
        val seed = listOf(Stored(value = collection, updatedAt = Instant.parse("2024-01-15T10:30:00Z")))

        RamUserCollectionRepository(seed).delete("seed")

        assertEquals(expected = collection, actual = RamUserCollectionRepository(seed).get("seed"))
    }

    @Test
    fun `save updates itemIds on existing collection`() = runTest {
        val repository = buildRepository()
        val collection =
            UserCollection(id = "1", name = "My Spells", type = UserCollection.Type.SPELL, itemIds = emptyList())

        repository.save(collection)

        val updated = collection.copy(itemIds = listOf("spell1", "spell2"))
        repository.save(updated)

        val result = repository.get("1")
        assertEquals(expected = updated, actual = result)
    }

    @Test
    fun `delete removes collection by id`() = runTest {
        val repository = buildRepository()
        val collection =
            UserCollection(id = "1", name = "My Spells", type = UserCollection.Type.SPELL, itemIds = emptyList())

        repository.save(collection)
        repository.delete("1")

        assertNull(repository.get("1"))
        assertTrue(repository.getAll(UserCollection.Type.SPELL).isEmpty())
    }

    @Test
    fun `getAll returns nothing when no collection of the given type exists`() = runTest {
        val repository = buildRepository()
        val collection =
            UserCollection(id = "1", name = "My Spells", type = UserCollection.Type.SPELL, itemIds = emptyList())

        repository.save(collection)

        val monsterCollections = repository.getAll(UserCollection.Type.MONSTER)
        assertTrue(monsterCollections.isEmpty())
    }

    @Test
    fun `rename changes the name and leaves the items alone`() = runTest {
        val repository = buildRepository()
        val collection = UserCollection(
            id = "1",
            name = "My Spells",
            type = UserCollection.Type.SPELL,
            itemIds = listOf("spell1", "spell2"),
        )
        repository.save(collection)

        val result = repository.rename("1", "Combat Spells")

        assertEquals(expected = UserCollectionRepository.Result.Success, actual = result)
        val renamed = repository.get("1")
        assertEquals(expected = "Combat Spells", actual = renamed?.name)
        assertEquals(expected = listOf("spell1", "spell2"), actual = renamed?.itemIds)
    }

    @Test
    fun `rename reports a missing collection`() = runTest {
        val repository = buildRepository()

        assertEquals(
            expected = UserCollectionRepository.Result.NotFound,
            actual = repository.rename("missing", "Combat Spells"),
        )
    }

    @Test
    fun `save advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val collection =
            UserCollection(id = "1", name = "My Spells", type = UserCollection.Type.SPELL, itemIds = emptyList())

        repository.save(collection)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(collection.copy(itemIds = listOf("spell1")))

        val stored = repository.getAll(UserCollection.Type.SPELL).single()
        assertEquals(Instant.fromEpochMilliseconds(5_000L), stored.updatedAt)
    }
}
