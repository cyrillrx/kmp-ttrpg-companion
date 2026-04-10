package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureRepository

class CreatureEntityRepository(
    private val creatureRepository: CreatureRepository,
) : EntityRepository<Creature> {
    override suspend fun getById(id: String): Creature? = creatureRepository.getById(id)
}
