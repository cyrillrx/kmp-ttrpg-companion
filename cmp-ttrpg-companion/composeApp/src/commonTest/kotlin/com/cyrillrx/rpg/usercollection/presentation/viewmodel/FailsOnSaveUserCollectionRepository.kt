package com.cyrillrx.rpg.usercollection.presentation.viewmodel

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.data.RamUserCollectionRepository
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository

class FailsOnSaveUserCollectionRepository : UserCollectionRepository {
    private val delegate = RamUserCollectionRepository()
    override suspend fun getAll(type: UserCollection.ItemType): List<Stored<UserCollection>> = delegate.getAll(type)
    override suspend fun get(id: String): UserCollection? = delegate.get(id)
    override suspend fun save(collection: UserCollection): Unit = error("Save failed")
    override suspend fun delete(id: String) = delegate.delete(id)
}
