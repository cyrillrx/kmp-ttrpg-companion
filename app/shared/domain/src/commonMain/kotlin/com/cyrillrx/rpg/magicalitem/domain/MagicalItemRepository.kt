package com.cyrillrx.rpg.magicalitem.domain

import com.cyrillrx.rpg.core.domain.EntityRepository

interface MagicalItemRepository : EntityRepository<MagicalItem> {
    suspend fun getAll(filter: MagicalItemFilter?): List<MagicalItem>
}
