package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.Imported
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
import com.cyrillrx.rpg.creature.data.api.ApiAbilities
import com.cyrillrx.rpg.creature.data.api.ApiSavingThrows
import com.cyrillrx.rpg.creature.data.api.ApiSkills
import com.cyrillrx.rpg.creature.data.api.ApiSpeeds
import com.cyrillrx.rpg.creature.data.createAbilities
import com.cyrillrx.rpg.creature.data.toAlignment
import com.cyrillrx.rpg.creature.data.toSize
import com.cyrillrx.rpg.creature.data.toSkills
import com.cyrillrx.rpg.creature.data.toSpeeds
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import com.cyrillrx.rpg.creature.domain.coerceToValidArmorClass
import com.cyrillrx.rpg.creature.domain.coerceToValidMaxHitPoints

internal fun ApiCharacter.toCharacter(): Result<Imported<Character, CharacterImportWarning>, CharacterImportError> {
    val id = id
        ?: return Result.Failure(CharacterImportError.MissingId)
    val warnings = mutableListOf<CharacterImportWarning>()

    fun <T> T.coerce(field: String, coerce: (T) -> T): T = coerce(this).also {
        if (it != this) warnings += CharacterImportWarning.ValueCoerced(id, field, declared = "$this", kept = "$it")
    }

    val name = name
        ?: return Result.Failure(CharacterImportError.MissingName(id))
    val apiTranslations = translations
        ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
    val (translations, translationErrors) = apiTranslations.partitionBy { locale, t -> t.toTranslation(id, locale) }
    if (translations.isEmpty() && translationErrors.isNotEmpty()) {
        return Result.Failure(CharacterImportError.MissingTranslations(id))
    }
    translationErrors.forEach { warnings += CharacterImportWarning.TranslationDropped(it) }
    val apiSize = size
        ?: return Result.Failure(CharacterImportError.MissingSize(id))
    val size = apiSize.toSize()
        ?: return Result.Failure(CharacterImportError.UnknownSize(id, apiSize))
    val apiAlignment = alignment
        ?: return Result.Failure(CharacterImportError.MissingAlignment(id))
    val alignment = apiAlignment.toAlignment()
        ?: return Result.Failure(CharacterImportError.UnknownAlignment(id, apiAlignment))
    val armorClass = armorClass?.coerce("armor class", Int::coerceToValidArmorClass)
        ?: return Result.Failure(CharacterImportError.MissingArmorClass(id))
    val maxHitPoints = maxHitPoints?.coerce("max hit points", Int::coerceToValidMaxHitPoints)
        ?: return Result.Failure(CharacterImportError.MissingMaxHitPoints(id))
    val currentHitPoints = currentHitPoints
        ?.coerce("current hit points") { it.coerceToValidCurrentHitPoints(maxHitPoints) }
        ?: maxHitPoints
    val temporaryHitPoints = temporaryHitPoints
        ?.coerce("temporary hit points", Int::coerceToValidHitPointAmount)
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
        clazz to level.coerce("class level", Int::coerceToValidCharacterLevel)
    }
    val (parsedLanguages, languageErrors) = languages.orEmpty().partitionBy { lang -> lang.toLanguage(id) }
    val languages = parsedLanguages.takeIf { languageErrors.isEmpty() }
        ?: return Result.Failure(languageErrors.first())
    val background = background?.let { apiBackground ->
        apiBackground.toBackground()
            .also { if (it == null) warnings += CharacterImportWarning.UnknownBackground(id, apiBackground) }
    }

    val character = Character(
        id = id,
        name = name,
        translations = translations,
        background = background,
        race = race,
        classes = classLevels,
        primaryClass = classLevels.keys.first(),
        size = size,
        alignment = alignment,
        abilities = createAbilities(abilities, savingThrows),
        armorClass = armorClass,
        maxHitPoints = maxHitPoints,
        currentHitPoints = currentHitPoints,
        temporaryHitPoints = temporaryHitPoints,
        speeds = speeds.toSpeeds().coerce("speeds") { it.coerceToValidCharacterSpeeds() },
        languages = languages,
        skills = apiSkills.toSkills(),
    )
    return Result.Success(Imported(character, warnings))
}

private fun ApiCharacter.Translation.toTranslation(
    characterId: String,
    locale: String,
): Result<Character.Translation, CharacterImportError.InvalidTranslation> {
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

private fun String.toBackground(): Background? = Background.entries.find { it.name.equals(this, ignoreCase = true) }

private fun String.toRace(): Race? = Race.entries.find { it.name.equals(this, ignoreCase = true) }

private fun String.toClass(): Character.Class? =
    Character.Class.entries.find { it.name.equals(this, ignoreCase = true) }

private fun String.toLanguage(id: String): Result<Language, CharacterImportError> {
    val language = Language.entries.find { it.name.equals(this, ignoreCase = true) }
        ?: return Result.Failure(CharacterImportError.UnknownLanguage(id, this))

    return Result.Success(language)
}

internal fun Character.toApiCharacter(): ApiCharacter = ApiCharacter(
    id = id,
    name = name,
    background = background?.name?.lowercase(),
    race = race.name.lowercase(),
    classes = classesPrimaryFirst().mapKeys { (clazz, _) -> clazz.name.lowercase() },
    size = size.name.lowercase(),
    alignment = alignment.name.lowercase(),
    abilities = abilities.toApiAbilities(),
    savingThrows = abilities.toApiSavingThrows(),
    armorClass = armorClass,
    maxHitPoints = maxHitPoints,
    currentHitPoints = currentHitPoints,
    temporaryHitPoints = temporaryHitPoints,
    speeds = speeds.toApiSpeeds(),
    skills = skills.toApiSkills(),
    languages = languages.map { it.name.lowercase() },
    translations = translations.mapValues { (_, translation) ->
        ApiCharacter.Translation(
            shortDescription = translation.shortDescription,
            description = translation.description,
        )
    },
)

private fun Character.classesPrimaryFirst(): Map<Character.Class, Int> {
    val primaryLevel = classes[primaryClass] ?: return classes
    return mapOf(primaryClass to primaryLevel) + (classes - primaryClass)
}

private fun Abilities.toApiAbilities(): ApiAbilities = ApiAbilities(
    str = strength.value,
    dex = dexterity.value,
    con = constitution.value,
    int = intelligence.value,
    wis = wisdom.value,
    cha = charisma.value,
)

private fun Abilities.toApiSavingThrows(): ApiSavingThrows = ApiSavingThrows(
    str = strength.savingThrowProficiency.toApiProficiency(),
    dex = dexterity.savingThrowProficiency.toApiProficiency(),
    con = constitution.savingThrowProficiency.toApiProficiency(),
    int = intelligence.savingThrowProficiency.toApiProficiency(),
    wis = wisdom.savingThrowProficiency.toApiProficiency(),
    cha = charisma.savingThrowProficiency.toApiProficiency(),
)

private fun Speeds.toApiSpeeds(): ApiSpeeds = ApiSpeeds(
    walk = walk,
    fly = fly,
    swim = swim,
    climb = climb,
    burrow = burrow,
    hover = hover,
)

private fun Skills.toApiSkills(): ApiSkills = ApiSkills(
    acrobatics = acrobatics.toApiProficiency(),
    animalHandling = animalHandling.toApiProficiency(),
    arcana = arcana.toApiProficiency(),
    athletics = athletics.toApiProficiency(),
    deception = deception.toApiProficiency(),
    history = history.toApiProficiency(),
    insight = insight.toApiProficiency(),
    intimidation = intimidation.toApiProficiency(),
    investigation = investigation.toApiProficiency(),
    medicine = medicine.toApiProficiency(),
    nature = nature.toApiProficiency(),
    perception = perception.toApiProficiency(),
    performance = performance.toApiProficiency(),
    persuasion = persuasion.toApiProficiency(),
    religion = religion.toApiProficiency(),
    sleightOfHand = sleightOfHand.toApiProficiency(),
    stealth = stealth.toApiProficiency(),
    survival = survival.toApiProficiency(),
)

private fun Proficiency.toApiProficiency(): String? = when (this) {
    Proficiency.NONE -> null
    Proficiency.PROFICIENT -> "proficient"
    Proficiency.EXPERT -> "expert"
}
