package com.cyrillrx.rpg.spellbook.domain

interface SpellRepository {
    suspend fun getAll(): List<Spell>
    suspend fun filter(query: String): List<Spell>
}
