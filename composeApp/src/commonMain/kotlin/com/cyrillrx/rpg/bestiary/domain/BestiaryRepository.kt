package com.cyrillrx.rpg.bestiary.domain

import com.cyrillrx.rpg.models.bestiary.Creature

interface BestiaryRepository {
    suspend fun getAll(): List<Creature>
}
