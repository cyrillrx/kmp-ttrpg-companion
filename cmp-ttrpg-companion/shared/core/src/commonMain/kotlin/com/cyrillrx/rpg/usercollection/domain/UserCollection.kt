package com.cyrillrx.rpg.usercollection.domain

data class UserCollection(
    val id: String,
    val name: String,
    val itemType: ItemType,
    val itemIds: List<String>,
) {
    enum class ItemType { SPELL, MAGICAL_ITEM, MONSTER }
}
