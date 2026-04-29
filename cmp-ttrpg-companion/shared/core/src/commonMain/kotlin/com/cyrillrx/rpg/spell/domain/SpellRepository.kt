package com.cyrillrx.rpg.spell.domain

import com.cyrillrx.rpg.core.domain.EntityRepository

interface SpellRepository : EntityRepository<Spell> {
    suspend fun getAll(filter: SpellFilter?): List<Spell>
}
