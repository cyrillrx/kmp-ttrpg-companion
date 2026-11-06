package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiAbilities(
    val str: Int? = null,
    val dex: Int? = null,
    val con: Int? = null,
    val int: Int? = null,
    val wis: Int? = null,
    val cha: Int? = null,
)
