package com.cyrillrx.rpg.spell.domain

interface SpellRepository {
    suspend fun getAll(): List<Spell>
    suspend fun filter(query: String): List<Spell>
}
