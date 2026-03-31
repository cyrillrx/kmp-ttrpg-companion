package com.cyrillrx.rpg.userlist.domain

interface UserListRepository {
    suspend fun getAll(type: UserList.Type): List<UserList>
    suspend fun get(id: String): UserList?
    suspend fun save(list: UserList)
    suspend fun delete(id: String)

    suspend fun addToList(list: UserList, itemId: String): Result {
        val updatedList = list.copy(itemIds = list.itemIds + itemId)
        save(updatedList)
        return Result.Success
    }

    suspend fun removeFromList(listId: String, itemId: String): Result {
        val list = get(listId) ?: return Result.NotFound

        return removeFromList(list, itemId)
    }

    suspend fun removeFromList(list: UserList, itemId: String): Result {
        val updatedList = list.copy(itemIds = list.itemIds - itemId)
        save(updatedList)
        return Result.Success
    }

    sealed class Result {
        object Success : Result()
        object NotFound : Result()
        class Error(val message: String) : Result()
    }
}
