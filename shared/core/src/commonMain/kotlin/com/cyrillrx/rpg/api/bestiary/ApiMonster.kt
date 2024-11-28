package com.cyrillrx.rpg.api.bestiary

import kotlinx.serialization.Serializable

@Serializable
class ApiMonster {
    // Creature
    val type: String? = null
    val subtype: String? = null
    val size: String? = null
    val alignment: String? = null
    val ac: String? = null
    val hp: String? = null
    val speed: String? = null
    val languages: String? = null

    // Abilities
    val str: String? = null
    val dex: String? = null
    val con: String? = null
    val int: String? = null
    val wis: String? = null
    val cha: String? = null

    val challenge: Float? = null
    val px: String? = null
}
