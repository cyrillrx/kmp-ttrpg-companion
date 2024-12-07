package com.cyrillrx.rpg.magicalitems.domain

interface MagicalItemRepository {
    suspend fun getAll(): List<MagicalItem>
    suspend fun filter(query: String): List<MagicalItem>
}
