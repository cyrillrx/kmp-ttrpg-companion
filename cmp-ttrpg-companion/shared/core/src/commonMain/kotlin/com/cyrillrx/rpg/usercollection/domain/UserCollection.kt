package com.cyrillrx.rpg.usercollection.domain

data class UserCollection(
    val id: String,
    val name: String,
    val type: Type,
    val itemIds: List<String>,
) {
    enum class Type { SPELL, MAGICAL_ITEM, MONSTER }
}
