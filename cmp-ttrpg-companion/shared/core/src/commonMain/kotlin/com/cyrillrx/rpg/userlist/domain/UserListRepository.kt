package com.cyrillrx.rpg.userlist.domain

interface UserListRepository {
    suspend fun getAll(type: UserList.Type): List<UserList>
    suspend fun get(id: String): UserList?
    suspend fun save(list: UserList)
    suspend fun delete(id: String)
}
