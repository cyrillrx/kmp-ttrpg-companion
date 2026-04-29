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
    val speeds: ApiSpeeds?,
    val skills: Map<String, String>?,
    val damageAffinities: Map<String, String>?,
    val conditionImmunities: Map<String, Boolean>?,
    val translations: Map<String, Translation>?,
) {
    @Serializable
    class ApiAbilities(
        val str: ApiAbility?,
        val dex: ApiAbility?,
        val con: ApiAbility?,
        val int: ApiAbility?,
        val wis: ApiAbility?,
        val cha: ApiAbility?,
    )

    @Serializable
    class ApiAbility(
        val value: Int?,
        val savingThrowProficiency: String?,
    )

    @Serializable
    class ApiSpeeds(
        val walk: Int?,
        val fly: Int?,
        val swim: Int?,
        val climb: Int?,
        val burrow: Int?,
        val hover: Boolean?,
    )

    @Serializable
    class Translation(
        val name: String?,
        val subtype: String?,
        val description: String?,
        val senses: String?,
        val languages: List<String>?,
    )
}
