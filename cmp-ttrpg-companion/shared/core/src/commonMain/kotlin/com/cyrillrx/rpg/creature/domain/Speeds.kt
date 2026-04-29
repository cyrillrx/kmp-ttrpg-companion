package com.cyrillrx.rpg.creature.domain

data class Speeds(
    val walk: Int? = null,
    val fly: Int? = null,
    val swim: Int? = null,
    val climb: Int? = null,
    val burrow: Int? = null,
    val hover: Boolean = false,
)
