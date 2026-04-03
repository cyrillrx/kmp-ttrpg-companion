package com.cyrillrx.rpg.userlist.presentation.viewmodel

import com.cyrillrx.rpg.userlist.domain.UserList
import com.cyrillrx.rpg.userlist.domain.UserListRepository

class FailingUserListRepository : UserListRepository {
    override suspend fun getAll(type: UserList.Type): List<UserList> = error("Repository failure")
    override suspend fun get(id: String): UserList = error("Repository failure")
    override suspend fun save(list: UserList) = Unit
    override suspend fun delete(id: String) = Unit
}
