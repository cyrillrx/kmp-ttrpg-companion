package com.cyrillrx.rpg.userlist.domain

import kotlinx.datetime.Instant

data class UserList(
    val id: String,
    val name: String,
    val type: Type,
    val itemIds: List<String>,
    val lastModified: Instant = Instant.fromEpochMilliseconds(0L),
) {
    enum class Type { SPELL, MAGICAL_ITEM, CREATURE }
}
