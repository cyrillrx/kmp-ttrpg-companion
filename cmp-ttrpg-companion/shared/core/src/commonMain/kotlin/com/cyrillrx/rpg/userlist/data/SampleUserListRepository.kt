package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository

class SampleUserListRepository : UserListRepository {

    override suspend fun getAll(type: UserList.Type): List<UserList> = lists.values.filter { it.type == type }

    override suspend fun get(id: String): UserList? = lists[id]

    override suspend fun save(list: UserList) {
        lists[list.id] = list
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }

    companion object {
        private val samples: List<UserList> = listOf(
            combatSpells(),
            supportSpells(),
            gandalfSpells(),
        )

        private val lists = mutableMapOf<String, UserList>().apply {
            samples.forEach { put(it.id, it) }
        }

        fun getAll(): List<UserList> = samples

        fun getFirst(): UserList = samples.first()

        private fun combatSpells() = UserList(
            id = "sample-spell-list-1",
            name = "Combat Spells",
            type = UserList.Type.SPELL,
            itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
        )

        private fun supportSpells() = UserList(
            id = "sample-spell-list-2",
            name = "Support Spells",
            type = UserList.Type.SPELL,
            itemIds = listOf("Mage Armor", "Detect Thoughts"),
        )

        private fun gandalfSpells() = UserList(
            id = "sample-spell-list-3",
            name = "Gandalf's Spells",
            type = UserList.Type.SPELL,
            itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
        )
    }
}
