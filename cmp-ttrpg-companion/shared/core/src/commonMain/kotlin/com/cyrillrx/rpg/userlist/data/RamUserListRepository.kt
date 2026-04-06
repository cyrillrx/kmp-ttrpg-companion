package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository

class RamUserListRepository : UserListRepository {
    private val lists = mutableMapOf<String, UserList>()

    override suspend fun getAll(type: UserList.Type): List<UserList> =
        lists.values.filter { it.type == type }.sortedByDescending { it.lastModified }

    override suspend fun get(id: String): UserList? = lists[id]

    override suspend fun save(list: UserList) {
        lists[list.id] = list
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }
}
