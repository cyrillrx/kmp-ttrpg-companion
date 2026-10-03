package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.coerceAndWarn
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterLevel
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterSpeeds
import com.cyrillrx.rpg.character.domain.coerceToValidCurrentHitPoints
import com.cyrillrx.rpg.character.domain.coerceToValidHitPointAmount
import com.cyrillrx.rpg.creature.data.createAbilities
import com.cyrillrx.rpg.creature.data.toAlignment
import com.cyrillrx.rpg.creature.data.toSize
import com.cyrillrx.rpg.creature.data.toSkills
import com.cyrillrx.rpg.creature.data.toSpeeds
import com.cyrillrx.rpg.creature.domain.coerceToValidArmorClass
import com.cyrillrx.rpg.creature.domain.coerceToValidMaxHitPoints

internal fun ApiCharacter.toCharacter(source: String): Result<Character, CharacterImportError> {
    val id = id
        ?: return Result.Failure(CharacterImportError.MissingId)
    val name = name
        ?: return Result.Failure(CharacterImportError.MissingName(id))
    val apiTranslations = translations
        ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
    val translations = apiTranslations.toTranslations(source, id)
        ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
    val apiSize = size
        ?: return Result.Failure(CharacterImportError.MissingSize(id))
    val size = apiSize.toSize()
        ?: return Result.Failure(CharacterImportError.UnknownSize(id, apiSize))
    val apiAlignment = alignment
        ?: return Result.Failure(CharacterImportError.MissingAlignment(id))
    val alignment = apiAlignment.toAlignment()
        ?: return Result.Failure(CharacterImportError.UnknownAlignment(id, apiAlignment))
    val armorClass = armorClass?.coerceAndWarn(source, id, "armor class", Int::coerceToValidArmorClass)
        ?: return Result.Failure(CharacterImportError.MissingArmorClass(id))
    val maxHitPoints = maxHitPoints?.coerceAndWarn(source, id, "max hit points", Int::coerceToValidMaxHitPoints)
        ?: return Result.Failure(CharacterImportError.MissingMaxHitPoints(id))
    val currentHitPoints = currentHitPoints
        ?.coerceAndWarn(source, id, "current hit points") { it.coerceToValidCurrentHitPoints(maxHitPoints) }
        ?: maxHitPoints
    val temporaryHitPoints = temporaryHitPoints
        ?.coerceAndWarn(source, id, "temporary hit points", Int::coerceToValidHitPointAmount)
        ?: 0
    speeds?.walk
        ?: return Result.Failure(CharacterImportError.MissingWalkSpeed(id))
    val apiSkills = skills
        ?: return Result.Failure(CharacterImportError.MissingSkills(id))
    val apiRace = race
        ?: return Result.Failure(CharacterImportError.MissingRace(id))
    val race = apiRace.toRace()
        ?: return Result.Failure(CharacterImportError.UnknownRace(id, apiRace))
    val apiClasses = classes?.takeIf { it.isNotEmpty() }
        ?: return Result.Failure(CharacterImportError.MissingClasses(id))
    val classLevels = apiClasses.entries.associate { (apiClass, level) ->
        val clazz = apiClass.toClass()
            ?: return Result.Failure(CharacterImportError.UnknownClass(id, apiClass))
        clazz to level.coerceAndWarn(source, id, "class level", Int::coerceToValidCharacterLevel)
    }
    val (parsedLanguages, languageErrors) = languages.orEmpty().partitionBy { lang -> lang.toLanguage(id) }
    languageErrors.forEach { println("WARNING: $source import error: $it") }
    val languages = parsedLanguages.takeIf { languageErrors.isEmpty() }
        ?: return Result.Failure(languageErrors.first())

    return Result.Success(
        Character(
            id = id,
            name = name,
            translations = translations,
            background = background?.toBackground(),
            race = race,
            classes = classLevels,
            // The preset format carries no primary class: the first declared one stands in.
            primaryClass = classLevels.keys.first(),
            size = size,
            alignment = alignment,
            abilities = createAbilities(abilities, savingThrows),
            armorClass = armorClass,
            maxHitPoints = maxHitPoints,
            currentHitPoints = currentHitPoints,
            temporaryHitPoints = temporaryHitPoints,
            speeds = speeds.toSpeeds()
                .coerceAndWarn(source, id, "speeds") { it.coerceToValidCharacterSpeeds() },
            languages = languages,
            skills = apiSkills.toSkills(),
        ),
    )
}

private fun Map<String, ApiCharacter.Translation>.toTranslations(
    source: String,
    characterId: String,
): Map<String, Character.Translation>? {
    // A sheet created in the app holds no translation until its short description is filled in.
    if (isEmpty()) return emptyMap()
    val (parsedTranslations, translationErrors) = partitionBy { locale, t ->
        t.toTranslation(characterId, locale)
    }
    translationErrors.forEach { println("WARNING: $source import error: $it") }
    return parsedTranslations.takeIf { it.isNotEmpty() }
}

private fun ApiCharacter.Translation.toTranslation(
    characterId: String,
    locale: String,
): Result<Character.Translation, CharacterImportError> {
    val shortDescription = shortDescription
        ?: return Result.Failure(
            CharacterImportError.InvalidTranslation(characterId, locale, field = "shortDescription"),
        )
    val description = description
        ?: return Result.Failure(
            CharacterImportError.InvalidTranslation(characterId, locale, field = "description"),
        )
    return Result.Success(
        Character.Translation(
            shortDescription = shortDescription,
            description = description,
        ),
    )
}

private fun String.toBackground(): Background? =
    Background.entries
        .find { it.name.equals(this, ignoreCase = true) }
        .also { if (it == null) println("WARNING: unknown background '$this'") }

private fun String.toRace(): Race? = Race.entries.find { it.name.equals(this, ignoreCase = true) }

private fun String.toClass(): Character.Class? =
    Character.Class.entries.find { it.name.equals(this, ignoreCase = true) }

private fun String.toLanguage(id: String): Result<Language, CharacterImportError> {
    val language = Language.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: return Result.Failure(CharacterImportError.UnknownLanguage(id, this))

    return Result.Success(language)
}
