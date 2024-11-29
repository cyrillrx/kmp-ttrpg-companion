package com.cyrillrx.rpg.app

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    data object Home : Route

    @Serializable
    data object SpellList : Route

    @Serializable
    data class SpellDetail(val serializedSpell: String) : Route

    @Serializable
    data object Bestiary : Route

    @Serializable
    data class BestiaryDetail(val id: String) : Route

    @Serializable
    data object Inventory : Route

    @Serializable
    data class BestiaryInventory(val id: String) : Route
}
