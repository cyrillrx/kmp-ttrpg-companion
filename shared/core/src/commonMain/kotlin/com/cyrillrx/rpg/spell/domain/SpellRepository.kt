package com.cyrillrx.rpg.spell.domain

interface SpellRepository {
    suspend fun getAll(filter: SpellFilter?): List<Spell>
}
