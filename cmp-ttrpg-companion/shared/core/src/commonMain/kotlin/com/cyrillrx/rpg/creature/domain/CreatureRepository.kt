package com.cyrillrx.rpg.creature.domain

interface CreatureRepository {
    suspend fun getAll(filter: CreatureFilter?): List<Monster>
    suspend fun getById(id: String): Monster?
    suspend fun getByIds(ids: List<String>): List<Monster> = ids.mapNotNull { getById(it) }
}
