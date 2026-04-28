package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.domain.Monster

class CreatureEntityRepository(
    private val creatureRepository: CreatureRepository,
) : EntityRepository<Monster> {
    override suspend fun getById(id: String): Monster? = creatureRepository.getById(id)
}
