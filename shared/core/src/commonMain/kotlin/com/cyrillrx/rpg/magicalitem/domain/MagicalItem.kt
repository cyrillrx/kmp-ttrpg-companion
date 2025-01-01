package com.cyrillrx.rpg.magicalitem.domain

import kotlinx.serialization.Serializable

@Serializable
class MagicalItem(
    val title: String,
    val subtitle: String,
    val description: String,
    val type: Type,
    val rarity: Rarity,
    val attunement: Boolean,
) {
    enum class Type { ARMOR, POTION, RING, ROD, SCROLL, STAFF, WAND, WEAPON, WONDROUS_ITEM }
    enum class Rarity { COMMON, UNCOMMON, RARE, VERY_RARE, LEGENDARY, ARTIFACT }
}
