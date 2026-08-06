package com.cyrillrx.rpg.usercollection.domain

import com.cyrillrx.rpg.core.domain.Stored

interface UserCollectionRepository {
    suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>>
    suspend fun get(id: String): UserCollection?
    suspend fun save(list: UserCollection)
    suspend fun delete(id: String)

    suspend fun addToList(list: UserCollection, itemId: String): Result {
        save(list.copy(itemIds = list.itemIds + itemId))
        return Result.Success
    }

    suspend fun removeFromList(listId: String, itemId: String): Result {
        val list = get(listId) ?: return Result.NotFound

        return removeFromList(list, itemId)
    }

    suspend fun removeFromList(list: UserCollection, itemId: String): Result {
        save(list.copy(itemIds = list.itemIds - itemId))
        return Result.Success
    }

    sealed class Result {
        object Success : Result()
        object NotFound : Result()
        class Error(val message: String) : Result()
    }
}
