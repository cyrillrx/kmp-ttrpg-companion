package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.core.domain.MutableClock
import com.cyrillrx.rpg.userlist.domain.UserList
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.time.Clock
import kotlin.time.Instant

class RamUserListRepositoryTest {

    private fun buildRepository(clock: Clock = Clock.System) = RamUserListRepository(clock)

    @Test
    fun `save and getAll returns list filtered by type`() = runTest {
        val repository = buildRepository()

        val spellList = UserList(id = "1", name = "Spellbook", type = UserList.Type.SPELL, itemIds = emptyList())
        val itemList = UserList(id = "2", name = "Artefacts", type = UserList.Type.MAGICAL_ITEM, itemIds = emptyList())

        repository.save(spellList)
        repository.save(itemList)

        val spellLists = repository.getAll(UserList.Type.SPELL)
        assertEquals(expected = 1, actual = spellLists.size)
        assertEquals(expected = spellList, actual = spellLists.first().value)

        val itemLists = repository.getAll(UserList.Type.MAGICAL_ITEM)
        assertEquals(expected = 1, actual = itemLists.size)
        assertEquals(expected = itemList, actual = itemLists.first().value)
    }

    @Test
    fun `get returns the list by id`() = runTest {
        val repository = buildRepository()
        val list = UserList(id = "abc", name = "Test", type = UserList.Type.SPELL, itemIds = emptyList())

        repository.save(list)

        val result = repository.get("abc")
        assertEquals(expected = list, actual = result)
    }

    @Test
    fun `get returns null for unknown id`() = runTest {
        val repository = buildRepository()
        assertNull(repository.get("missing"))
    }

    @Test
    fun `save updates itemIds on existing list`() = runTest {
        val repository = buildRepository()
        val list = UserList(id = "1", name = "Spellbook", type = UserList.Type.SPELL, itemIds = emptyList())

        repository.save(list)

        val updated = list.copy(itemIds = listOf("spell1", "spell2"))
        repository.save(updated)

        val result = repository.get("1")
        assertEquals(expected = updated, actual = result)
    }

    @Test
    fun `delete removes list by id`() = runTest {
        val repository = buildRepository()
        val list = UserList(id = "1", name = "Spellbook", type = UserList.Type.SPELL, itemIds = emptyList())

        repository.save(list)
        repository.delete("1")

        assertNull(repository.get("1"))
        assertTrue(repository.getAll(UserList.Type.SPELL).isEmpty())
    }

    @Test
    fun `getAll returns empty list when no lists of given type exist`() = runTest {
        val repository = buildRepository()
        val list = UserList(id = "1", name = "Spellbook", type = UserList.Type.SPELL, itemIds = emptyList())

        repository.save(list)

        val monsterLists = repository.getAll(UserList.Type.MONSTER)
        assertTrue(monsterLists.isEmpty())
    }

    @Test
    fun `save advances updatedAt on update`() = runTest {
        val clock = MutableClock(Instant.fromEpochMilliseconds(1_000L))
        val repository = buildRepository(clock)
        val list = UserList(id = "1", name = "Spellbook", type = UserList.Type.SPELL, itemIds = emptyList())

        repository.save(list)
        clock.instant = Instant.fromEpochMilliseconds(5_000L)
        repository.save(list.copy(itemIds = listOf("spell1")))

        val stored = repository.getAll(UserList.Type.SPELL).single()
        assertEquals(Instant.fromEpochMilliseconds(5_000L), stored.updatedAt)
    }
}
