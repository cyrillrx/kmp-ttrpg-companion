package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.core.domain.EntityRepository

interface MonsterRepository : EntityRepository<Monster> {
    suspend fun getAll(filter: MonsterFilter?): List<Monster>
}
