package com.cyrillrx.rpg.core.domain

interface EntityRepository<T : Entity> {
    suspend fun getById(id: String): T?
    suspend fun getByIds(ids: List<String>): List<T> = ids.mapNotNull { getById(it) }
}
