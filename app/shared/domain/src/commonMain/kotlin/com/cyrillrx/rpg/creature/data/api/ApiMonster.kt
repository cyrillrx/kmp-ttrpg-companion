package com.cyrillrx.rpg.creature.data.api

import kotlinx.serialization.Serializable

@Serializable
internal class ApiMonster(
    val id: String?,
    val source: String?,
    val type: String?,
    val size: String?,
    val alignment: String?,
    val challengeRating: Float?,
    val armorClass: Int?,
    val maxHitPoints: Int?,
    val hitDice: String?,
    val abilities: ApiAbilities?,
    val savingThrows: ApiSavingThrows?,
    val speeds: ApiSpeeds?,
    val skills: ApiSkills?,
    val damageAffinities: ApiDamageAffinities?,
    val conditionImmunities: ApiConditionImmunities?,
    val translations: Map<String, Translation>?,
) {
    @Serializable
    class Translation(
        val name: String?,
        val subtype: String?,
        val description: String?,
        val senses: String?,
        val languages: List<String>?,
    )
}
