package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSavingThrows(
    val str: String? = null,
    val dex: String? = null,
    val con: String? = null,
    val int: String? = null,
    val wis: String? = null,
    val cha: String? = null,
)
