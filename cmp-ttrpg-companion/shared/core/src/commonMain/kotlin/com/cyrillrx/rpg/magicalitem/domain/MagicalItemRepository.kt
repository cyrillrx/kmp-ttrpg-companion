package com.cyrillrx.rpg.magicalitem.domain

interface MagicalItemRepository {
    suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem>
    suspend fun getById(id: String): MagicalItem?
    suspend fun getByIds(ids: List<String>): List<MagicalItem> = ids.mapNotNull { getById(it) }
}
