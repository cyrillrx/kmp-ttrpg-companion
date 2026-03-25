package com.cyrillrx.rpg.creature.domain

interface CreatureRepository {
    suspend fun getAll(filter: CreatureFilter?): List<Creature>
    suspend fun getById(id: String): Creature?
}
