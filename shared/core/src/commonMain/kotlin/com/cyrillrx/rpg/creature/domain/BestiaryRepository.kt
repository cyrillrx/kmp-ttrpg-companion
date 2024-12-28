package com.cyrillrx.rpg.creature.domain

interface BestiaryRepository {
    suspend fun getAll(): List<Creature>
    suspend fun filter(query: String): List<Creature>
}
