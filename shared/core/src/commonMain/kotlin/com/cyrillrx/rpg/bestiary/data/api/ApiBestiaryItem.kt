package com.cyrillrx.rpg.bestiary.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiBestiaryItem {
    val title: String? = null
    val content: String? = null
    val header: Header? = null

    val type: String? = null
    val subtype: String? = null
    val size: String? = null
    val alignment: String? = null
    val challenge: Float? = null

    @Serializable
    class Header {
        val monster: ApiMonster? = null
    }
}
