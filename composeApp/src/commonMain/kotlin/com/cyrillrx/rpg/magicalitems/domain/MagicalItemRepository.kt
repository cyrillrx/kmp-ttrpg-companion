package com.cyrillrx.rpg.magicalitems.domain

import com.cyrillrx.rpg.models.magicalitems.MagicalItem

interface MagicalItemRepository {
    suspend fun getAll(): List<MagicalItem>
}
