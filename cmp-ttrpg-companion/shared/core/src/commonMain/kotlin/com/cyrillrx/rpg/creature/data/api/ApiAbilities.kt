package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiAbilities(
    val str: Int?,
    val dex: Int?,
    val con: Int?,
    val int: Int?,
    val wis: Int?,
    val cha: Int?,
)
