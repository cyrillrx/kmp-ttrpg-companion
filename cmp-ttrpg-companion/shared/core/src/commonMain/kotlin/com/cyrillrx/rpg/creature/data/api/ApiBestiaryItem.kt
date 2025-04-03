package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiBestiaryItem {
    val title: String? = null
    val content: String? = null
    val header: Header? = null

    val truetype: String? = null
    val size: String? = null
    val alignment: String? = null
    val challenge: Float? = null

    @Serializable
    class Header {
        val monster: ApiMonster? = null
    }
}
