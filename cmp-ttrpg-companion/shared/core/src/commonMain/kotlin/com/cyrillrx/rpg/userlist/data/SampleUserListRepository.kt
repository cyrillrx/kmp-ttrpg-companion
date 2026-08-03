package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.time.Clock
import kotlin.time.Instant

class SampleUserListRepository(
    private val clock: Clock = Clock.System,
) : UserListRepository {

    override suspend fun getAll(type: UserList.Type): List<Stored<UserList>> =
        lists.values.filter { it.value.type == type }

    override suspend fun get(id: String): UserList? = lists[id]?.value

    override suspend fun save(list: UserList) {
        lists[list.id] = Stored(value = list, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }

    companion object {
        private val samples: List<Stored<UserList>> = listOf(
            combatSpells(),
            supportSpells(),
            gandalfSpells(),
        )

        private val lists = mutableMapOf<String, Stored<UserList>>().apply {
            samples.forEach { put(it.value.id, it) }
        }

        fun getAll(): List<UserList> = samples.map { it.value }

        fun getAllStored(): List<Stored<UserList>> = samples

        fun getFirst(): UserList = samples.first().value

        fun getFirstStored(): Stored<UserList> = samples.first()

        private fun combatSpells() = stored(
            UserList(
                id = "sample-spell-list-1",
                name = "Combat Spells",
                type = UserList.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-15T10:30:00Z",
        )

        private fun supportSpells() = stored(
            UserList(
                id = "sample-spell-list-2",
                name = "Support Spells",
                type = UserList.Type.SPELL,
                itemIds = listOf("Mage Armor", "Detect Thoughts"),
            ),
            updatedAt = "2024-01-10T08:00:00Z",
        )

        private fun gandalfSpells() = stored(
            UserList(
                id = "sample-spell-list-3",
                name = "Gandalf's Spells",
                type = UserList.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-20T14:00:00Z",
        )

        private fun stored(list: UserList, updatedAt: String): Stored<UserList> =
            Stored(value = list, updatedAt = Instant.parse(updatedAt))
    }
}
