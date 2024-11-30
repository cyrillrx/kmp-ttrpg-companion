package com.cyrillrx.rpg.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object SpellList : Route

    @Serializable
    data object AlternativeSpellList : Route

    @Serializable
    data class SpellDetail(val serializedSpell: String) : Route

    @Serializable
    data object Bestiary : Route

    @Serializable
    data class BestiaryDetail(val id: String) : Route

    @Serializable
    data object MagicalItems : Route

    @Serializable
    data class MagicalItemDetail(val id: String) : Route
}
