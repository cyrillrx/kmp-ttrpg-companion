package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlin.time.Clock

class RamUserCollectionRepository(
    private val clock: Clock = Clock.System,
) : UserCollectionRepository {
    private val lists = mutableMapOf<String, Stored<UserCollection>>()

    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> =
        lists.values.filter { it.value.type == type }

    override suspend fun get(id: String): UserCollection? = lists[id]?.value

    override suspend fun save(list: UserCollection) {
        lists[list.id] = Stored(value = list, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }
}
