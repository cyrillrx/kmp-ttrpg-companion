package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSpeeds(
    val walk: Int? = null,
    val fly: Int? = null,
    val swim: Int? = null,
    val climb: Int? = null,
    val burrow: Int? = null,
    val hover: Boolean? = null,
)
