package com.cyrillrx.rpg.creature.domain

interface CreatureRepository {
    suspend fun getAll(filter: CreatureFilter?): List<Creature>
}
