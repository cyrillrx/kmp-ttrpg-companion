package com.cyrillrx.rpg.character.data.api

import com.cyrillrx.rpg.creature.data.api.ApiAbilities
import com.cyrillrx.rpg.creature.data.api.ApiSavingThrows
import com.cyrillrx.rpg.creature.data.api.ApiSkills
import com.cyrillrx.rpg.creature.data.api.ApiSpeeds
import kotlinx.serialization.Serializable

@Serializable
internal class ApiCharacter(
    val id: String?,
    val name: String?,
    val background: String?,
    val race: String?,
    val clazz: String?,
    val level: Int?,
    val size: String?,
    val alignment: String?,
    val abilities: ApiAbilities?,
    val savingThrows: ApiSavingThrows?,
    val armorClass: Int?,
    val maxHitPoints: Int?,
    val speeds: ApiSpeeds?,
    val skills: ApiSkills?,
    val languages: List<String>?,
    val translations: Map<String, Translation>?,
) {
    @Serializable
    class Translation(
        val shortDescription: String?,
        val description: String?,
    )
}
