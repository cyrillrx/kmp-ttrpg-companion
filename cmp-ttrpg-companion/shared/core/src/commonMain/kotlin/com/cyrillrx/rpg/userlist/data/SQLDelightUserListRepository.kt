package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository

class SQLDelightUserListRepository(databaseDriverFactory: DatabaseDriverFactory) : UserListRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(type: UserList.Type): List<UserList> = database.getAllUserLists(type)

    override suspend fun get(id: String): UserList? = database.getUserList(id)

    override suspend fun save(list: UserList) {
        val existing = database.getUserList(list.id)
        if (existing == null) {
            database.insertUserList(list)
        } else {
            database.updateUserList(list)
        }
    }

    override suspend fun delete(id: String) {
        database.deleteUserList(id)
    }
}
