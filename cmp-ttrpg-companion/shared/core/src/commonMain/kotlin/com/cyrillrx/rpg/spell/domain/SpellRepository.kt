package com.cyrillrx.rpg.spell.domain

interface SpellRepository {
    suspend fun getAll(filter: SpellFilter?): List<Spell>
    suspend fun getById(id: String): Spell?
    suspend fun getByIds(ids: List<String>): List<Spell> = ids.mapNotNull { getById(it) }
}
