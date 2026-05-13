package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSpeeds(
    val walk: Int?,
    val fly: Int?,
    val swim: Int?,
    val climb: Int?,
    val burrow: Int? = null,
    val hover: Boolean? = null,
)
