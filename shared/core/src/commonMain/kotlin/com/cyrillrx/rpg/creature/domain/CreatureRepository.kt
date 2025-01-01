package com.cyrillrx.rpg.creature.domain

interface CreatureRepository {
    suspend fun getAll(): List<Creature>
    suspend fun filter(query: String): List<Creature>
}
