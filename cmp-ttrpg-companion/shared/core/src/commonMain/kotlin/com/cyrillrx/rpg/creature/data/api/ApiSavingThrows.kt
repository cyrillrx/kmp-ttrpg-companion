package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSavingThrows(
    val str: String?,
    val dex: String?,
    val con: String?,
    val int: String?,
    val wis: String?,
    val cha: String?,
)
