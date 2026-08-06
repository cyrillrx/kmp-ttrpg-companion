package com.cyrillrx.rpg.usercollection.domain

import com.cyrillrx.rpg.core.domain.Stored

interface UserCollectionRepository {
    suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>>
    suspend fun get(id: String): UserCollection?
    suspend fun save(collection: UserCollection)
    suspend fun delete(id: String)

    suspend fun addToCollection(collection: UserCollection, itemId: String): Result {
        save(collection.copy(itemIds = collection.itemIds + itemId))
        return Result.Success
    }

    suspend fun removeFromCollection(collectionId: String, itemId: String): Result {
        val collection = get(collectionId) ?: return Result.NotFound

        return removeFromCollection(collection, itemId)
    }

    suspend fun removeFromCollection(collection: UserCollection, itemId: String): Result {
        save(collection.copy(itemIds = collection.itemIds - itemId))
        return Result.Success
    }

    sealed class Result {
        object Success : Result()
        object NotFound : Result()
        class Error(val message: String) : Result()
    }
}
