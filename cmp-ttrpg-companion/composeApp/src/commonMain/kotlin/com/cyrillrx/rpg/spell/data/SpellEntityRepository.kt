package com.cyrillrx.rpg.spell.data

import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellRepository

class SpellEntityRepository(
    private val repository: SpellRepository,
) : EntityRepository<Spell> {
    override suspend fun getById(id: String): Spell? = repository.getById(id)
}
