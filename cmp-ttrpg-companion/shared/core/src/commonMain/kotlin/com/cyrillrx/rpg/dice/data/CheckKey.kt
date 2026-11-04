package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.dice.domain.RollContext

private const val SAVING_THROW_PREFIX = "save:"
private const val SKILL_CHECK_PREFIX = "skill:"
private const val INITIATIVE_KEY = "initiative"

/** A stable, untranslated key such as `skill:perception`, `save:wisdom` or `initiative`. */
internal fun D20Check.toCheckKey(): String = when (this) {
    is D20Check.SavingThrow -> SAVING_THROW_PREFIX + ability.name.lowercase()
    is D20Check.SkillCheck -> SKILL_CHECK_PREFIX + skill.name.lowercase()
    D20Check.Initiative -> INITIATIVE_KEY
}

/** Null when the key names no check this build knows. */
internal fun String.toD20Check(): D20Check? = when {
    this == INITIATIVE_KEY -> D20Check.Initiative

    startsWith(SAVING_THROW_PREFIX) ->
        Ability.entries.findByName(removePrefix(SAVING_THROW_PREFIX))?.let(D20Check::SavingThrow)

    startsWith(SKILL_CHECK_PREFIX) ->
        Skill.entries.findByName(removePrefix(SKILL_CHECK_PREFIX))?.let(D20Check::SkillCheck)

    else -> null
}

private fun <E : Enum<E>> List<E>.findByName(name: String): E? = find { it.name.equals(name, ignoreCase = true) }

/** Null unless both columns are set and the key names a known check. */
internal fun storedRollContext(characterId: String?, checkKey: String?): RollContext? {
    val check = checkKey?.toD20Check()
    if (characterId == null || check == null) return null

    return RollContext(characterId, check)
}
