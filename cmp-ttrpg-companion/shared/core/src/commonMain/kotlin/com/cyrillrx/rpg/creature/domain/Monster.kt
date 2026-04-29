package com.cyrillrx.rpg.creature.domain

private const val FALLBACK_LOCALE = "en"

class Monster(
    id: String,
    size: Size,
    alignment: Alignment,
    abilities: Abilities,
    armorClass: Int,
    maxHitPoints: Int,
    languages: List<String>,
    val source: String,
    val type: Type,
    val challengeRating: Float,
    val hitDice: String,
    val skills: Skills = Skills(),
    val damageAffinities: DamageAffinities = DamageAffinities(),
    val conditionImmunities: ConditionImmunities = ConditionImmunities(),
    val translations: Map<String, Translation>,
) : Creature(
    id = id,
    size = size,
    alignment = alignment,
    abilities = abilities,
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    languages = languages,
) {
    init {
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
        val speed: String,
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
        UNDEAD,
        UNKNOWN,
    }
}
