package com.cyrillrx.rpg.api.bestiary

class BestiaryItem {

    val title: String? = null
    val content: String? = null
    val header: Header? = null

    val type: String? = null
    val subtype: String? = null
    val size: String? = null
    val alignment: String? = null
    val challenge: Float? = null

    class Header {
        val monster : Monster? = null
    }
}