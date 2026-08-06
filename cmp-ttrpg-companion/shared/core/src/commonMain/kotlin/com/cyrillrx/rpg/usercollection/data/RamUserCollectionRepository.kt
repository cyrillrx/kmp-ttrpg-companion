package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlin.time.Clock

class RamUserCollectionRepository(
    private val clock: Clock = Clock.System,
) : UserCollectionRepository {
    private val collections = mutableMapOf<String, Stored<UserCollection>>()

    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> =
        collections.values.filter { it.value.type == type }

    override suspend fun get(id: String): UserCollection? = collections[id]?.value

    override suspend fun save(collection: UserCollection) {
        collections[collection.id] = Stored(value = collection, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        collections.remove(id)
    }
}
