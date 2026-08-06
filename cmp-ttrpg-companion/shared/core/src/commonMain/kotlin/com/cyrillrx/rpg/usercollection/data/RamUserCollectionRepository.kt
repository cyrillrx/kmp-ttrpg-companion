package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlin.time.Clock

class RamUserListRepository(
    private val clock: Clock = Clock.System,
) : UserListRepository {
    private val lists = mutableMapOf<String, Stored<UserList>>()

    override suspend fun getAll(type: UserList.Type): List<Stored<UserList>> =
        lists.values.filter { it.value.type == type }

    override suspend fun get(id: String): UserList? = lists[id]?.value

    override suspend fun save(list: UserList) {
        lists[list.id] = Stored(value = list, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }
}
