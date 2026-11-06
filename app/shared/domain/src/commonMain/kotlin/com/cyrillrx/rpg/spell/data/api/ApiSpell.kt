package com.cyrillrx.rpg.spell.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiSpell(
    val id: String?,
    val source: String?,
    val level: Int?,
    val school: String?,
    val concentration: Boolean?,
    val ritual: Boolean?,
    val components: Components?,
    val availableClasses: List<String>?,
    val translations: Map<String, Translation>?,
) {
    @Serializable
    class Components(
        val verbal: Boolean,
        val somatic: Boolean,
        val material: Boolean,
    )

    @Serializable
    class Translation(
        val name: String?,
        val castingTime: String?,
        val range: String?,
        val duration: String?,
        val materialDescription: String?,
        val description: String?,
    )
}
