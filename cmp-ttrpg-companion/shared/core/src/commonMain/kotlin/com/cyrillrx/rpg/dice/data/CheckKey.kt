package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.dice.domain.RollContext

private const val SAVING_THROW_PREFIX = "save:"
private const val SKILL_CHECK_PREFIX = "skill:"
private const val INITIATIVE_KEY = "initiative"

/** A stable, untranslated key such as `skill:PERCEPTION`, `save:WISDOM` or `initiative`. */
internal fun D20Check.toCheckKey(): String = when (this) {
    is D20Check.SavingThrow -> SAVING_THROW_PREFIX + ability.name
    is D20Check.SkillCheck -> SKILL_CHECK_PREFIX + skill.name
    D20Check.Initiative -> INITIATIVE_KEY
}

/** Null when the key names no check this build knows. */
internal fun String.toD20Check(): D20Check? = when {
    this == INITIATIVE_KEY -> D20Check.Initiative

    startsWith(SAVING_THROW_PREFIX) ->
        Ability.entries.find { it.name == removePrefix(SAVING_THROW_PREFIX) }?.let(D20Check::SavingThrow)

    startsWith(SKILL_CHECK_PREFIX) ->
        Skill.entries.find { it.name == removePrefix(SKILL_CHECK_PREFIX) }?.let(D20Check::SkillCheck)

    else -> null
}

/** Null unless both columns are set and the key names a known check. */
internal fun storedRollContext(characterId: String?, checkKey: String?): RollContext? {
    val check = checkKey?.toD20Check()
    if (characterId == null || check == null) return null

    return RollContext(characterId, check)
}
