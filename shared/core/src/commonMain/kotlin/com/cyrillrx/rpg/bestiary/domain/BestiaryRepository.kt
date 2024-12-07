package com.cyrillrx.rpg.bestiary.domain

interface BestiaryRepository {
    suspend fun getAll(): List<Creature>
}
