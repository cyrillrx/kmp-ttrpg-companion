package com.cyrillrx.rpg.app

import kotlinx.serialization.Serializable

interface Route {

    @Serializable
    data object Home

    @Serializable
    data object Bestiary

    @Serializable
    data object MagicalItems

    @Serializable
    data object CharacterSheetList : Route
}
