package com.cyrillrx.rpg.userlist.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class SQLDelightUserListRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : UserListRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(type: UserList.Type): List<Stored<UserList>> =
        withContext(ioDispatcher) { database.getAllUserLists(type) }

    override suspend fun get(id: String): UserList? = withContext(ioDispatcher) { database.getUserList(id) }

    override suspend fun save(list: UserList) {
        val now = clock.now().toEpochMilliseconds()
        withContext(ioDispatcher) { database.saveUserList(list, createdAt = now, updatedAt = now) }
    }

    override suspend fun delete(id: String) {
        withContext(ioDispatcher) { database.deleteUserList(id) }
    }
}
