package com.cyrillrx.rpg.creature.domain

class Monster(
    id: String,
    size: Size,
    alignment: Alignment,
    abilities: Abilities,
    armorClass: Int,
    maxHitPoints: Int,
    speed: String,
    languages: List<String>,
    val source: String,
    val type: Type,
    val challengeRating: Float,
    val hitDice: String,
    val skills: Skills = Skills(),
    val damageAffinities: DamageAffinities = DamageAffinities(),
    val conditionImmunities: ConditionImmunities = ConditionImmunities(),
    val translations: Map<String, Translation> = emptyMap(),
) : Creature(
    id = id,
    size = size,
    alignment = alignment,
    abilities = abilities,
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    speed = speed,
    languages = languages,
) {
    data class Translation(
        val name: String,
        val subtype: String? = null,
        val description: String,
        val speed: String,
        val senses: String,
        val languages: List<String>,
    )

    fun resolveTranslation(locale: String): Translation? =
        translations[locale]
            ?: translations["en"]
            ?: translations.entries.minByOrNull { it.key }?.value

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
