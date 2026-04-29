package com.cyrillrx.rpg.creature.domain

interface MonsterRepository {
    suspend fun getAll(filter: MonsterFilter?): List<Monster>
    suspend fun getById(id: String): Monster?
    suspend fun getByIds(ids: List<String>): List<Monster> = ids.mapNotNull { getById(it) }
}
