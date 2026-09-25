package com.cyrillrx.rpg.usercollection.domain

import com.cyrillrx.rpg.core.domain.Entity

data class UserCollection(
    override val id: String,
    val name: String,
    val itemType: ItemType,
    val itemIds: List<String>,
) : Entity {
    override fun displayName(locale: String): String = name

    enum class ItemType { SPELL, MAGICAL_ITEM, MONSTER }
}
