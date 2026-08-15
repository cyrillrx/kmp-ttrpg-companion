package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository

class FailingUserCollectionRepository : UserCollectionRepository {
    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> = error("Repository failure")
    override suspend fun get(id: String): UserCollection = error("Repository failure")
    override suspend fun save(collection: UserCollection) = Unit
    override suspend fun delete(id: String) = Unit
}
