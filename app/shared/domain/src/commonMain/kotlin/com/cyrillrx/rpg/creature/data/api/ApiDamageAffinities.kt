package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiDamageAffinities(
    val acid: String?,
    val bludgeoning: String?,
    val cold: String?,
    val fire: String?,
    val force: String?,
    val lightning: String?,
    val necrotic: String?,
    val piercing: String?,
    val poison: String?,
    val psychic: String?,
    val radiant: String?,
    val slashing: String?,
    val thunder: String?,
    val bludgeoningNonMagical: String?,
    val piercingNonMagical: String?,
    val slashingNonMagical: String?,
)
