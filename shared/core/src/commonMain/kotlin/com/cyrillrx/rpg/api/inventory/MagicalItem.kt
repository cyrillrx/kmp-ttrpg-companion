package com.cyrillrx.rpg.api.inventory

class MagicalItem(
    val title: String,
    val subtitle: String,
    val description: String,
    val type: Type,
    val rarety: Rarety,
    val attunement : Boolean
) {
    enum class Type { ARMOR, POTION, RING, ROD, SCROLL, STAFF, WAND, WEAPON, WONDROUS_ITEM }
    enum class Rarety { COMMON, UNCOMMON, RARE, VERY_RARE, LEGENDARY, ARTIFACT }
}
