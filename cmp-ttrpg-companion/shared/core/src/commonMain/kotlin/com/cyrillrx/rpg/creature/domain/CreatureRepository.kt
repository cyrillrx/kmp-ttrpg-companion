package com.cyrillrx.rpg.creature.domain

interface CreatureRepository {
    suspend fun getAll(filter: CreatureFilter?): List<Creature>
    suspend fun getById(id: String): Creature?
    suspend fun getByIds(ids: List<String>): List<Creature> = ids.mapNotNull { getById(it) }
}
