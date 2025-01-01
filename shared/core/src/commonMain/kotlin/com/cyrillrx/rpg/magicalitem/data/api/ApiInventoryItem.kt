package com.cyrillrx.rpg.magicalitem.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiInventoryItem(
    val title: String,
    val content: String,
    val type: String,
    val subtype: String?,
    val rarity: String,
    val attunement: String?,
    val header: Header,
) {
    @Serializable
    class Header(val magicitem: MagicalItem, val taxonomy: Taxonomy) {

        @Serializable
        class MagicalItem(
            val type: String,
            val rarity: String,
            val attunement: String?,
        )

        @Serializable
        class Taxonomy(
            val category: Array<String>,
            val source: Array<String>,
        )
    }
}
