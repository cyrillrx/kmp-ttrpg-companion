package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.core.domain.FALLBACK_LOCALE

class Monster(
    override val id: String,
    override val size: Size,
    override val alignment: Alignment,
    override val abilities: Abilities,
    override val armorClass: Int,
    override val maxHitPoints: Int,
    override val speeds: Speeds,
    val source: String,
    val types: Set<Type>,
    val challengeRating: Float,
    val hitDice: String,
    val skills: Skills = Skills(),
    val damageAffinities: DamageAffinities = DamageAffinities(),
    val conditionImmunities: ConditionImmunities = ConditionImmunities(),
    val translations: Map<String, Translation>,
) : Creature() {
    init {
        require(types.isNotEmpty()) { "Monster $id must have at least one type" }
        require(translations.isNotEmpty()) { "Monster $id must have at least one translation" }
    }

    fun resolveTranslation(locale: String): Translation =
        translations[locale]
            ?: translations[FALLBACK_LOCALE]
            ?: requireNotNull(translations.entries.minByOrNull { it.key }?.value) {
                "Monster $id has empty translations"
            }

    data class Translation(
        val name: String,
        val subtype: String? = null,
        val description: String,
        val senses: String,
        val languages: List<String>,
    )

    enum class Type {
        ABERRATION,
        BEAST,
        CELESTIAL,
        CONSTRUCT,
        DRAGON,
        ELEMENTAL,
        FEY,
        FIEND,
        GIANT,
        HUMANOID,
        MONSTROSITY,
        OOZE,
        PLANT,
        SWARM,
        UNDEAD,
        UNKNOWN,
    }
}
