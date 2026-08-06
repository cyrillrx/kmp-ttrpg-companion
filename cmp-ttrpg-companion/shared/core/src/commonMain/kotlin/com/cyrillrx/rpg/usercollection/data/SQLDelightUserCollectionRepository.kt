package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.data.cache.Database
import com.cyrillrx.rpg.core.data.cache.DatabaseDriverFactory
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import kotlin.time.Clock

class SQLDelightUserCollectionRepository(
    databaseDriverFactory: DatabaseDriverFactory,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val clock: Clock = Clock.System,
) : UserCollectionRepository {
    private val database = Database(databaseDriverFactory)

    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> =
        withContext(ioDispatcher) { database.getAllUserCollections(type) }

    override suspend fun get(id: String): UserCollection? = withContext(ioDispatcher) { database.getUserCollection(id) }

    override suspend fun save(collection: UserCollection) {
        val now = clock.now().toEpochMilliseconds()
        withContext(ioDispatcher) { database.saveUserCollection(collection, updatedAt = now) }
    }

    override suspend fun rename(id: String, name: String): UserCollectionRepository.Result =
        withContext(ioDispatcher) {
            if (database.getUserCollection(id) == null) {
                UserCollectionRepository.Result.NotFound
            } else {
                database.renameUserCollection(id, name, updatedAt = clock.now().toEpochMilliseconds())
                UserCollectionRepository.Result.Success
            }
        }

    override suspend fun delete(id: String) {
        withContext(ioDispatcher) { database.deleteUserCollection(id) }
    }
}
