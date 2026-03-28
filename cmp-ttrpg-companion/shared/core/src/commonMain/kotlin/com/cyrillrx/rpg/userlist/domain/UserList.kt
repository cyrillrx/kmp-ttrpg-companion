package com.cyrillrx.rpg.userlist.domain

data class UserList(
    val id: String,
    val name: String,
    val type: Type,
    val itemIds: List<String>,
) {
    enum class Type { SPELL, MAGICAL_ITEM, CREATURE }
}
