package com.cyrillrx.rpg.magicalitem.data

import com.cyrillrx.rpg.core.domain.EntityRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.domain.MagicalItemRepository

class MagicalItemEntityRepository(
    private val delegate: MagicalItemRepository,
) : EntityRepository<MagicalItem> {
    override suspend fun getById(id: String): MagicalItem? = delegate.getById(id)
}
