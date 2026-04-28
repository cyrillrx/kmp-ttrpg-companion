package com.cyrillrx.rpg.magicalitem.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiInventoryItem(
    val id: String?,
    val source: String?,
    val type: String?,
    val rarity: String?,
    val attunement: Boolean?,
    val translations: Map<String, Translation>?,
) {
    @Serializable
    class Translation(
        val name: String?,
        val subtype: String?,
        val description: String?,
    )
}
