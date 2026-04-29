package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster

class MonsterEntityRepository(
    private val repository: MonsterRepository,
) : EntityRepository<Monster> {
    override suspend fun getById(id: String): Monster? = repository.getById(id)
}
