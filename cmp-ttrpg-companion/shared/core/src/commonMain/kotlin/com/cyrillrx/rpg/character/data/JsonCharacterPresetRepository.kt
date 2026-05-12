package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.creature.data.toAlignment
import com.cyrillrx.rpg.creature.data.toDomain
import com.cyrillrx.rpg.creature.data.toSize
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Language
import com.cyrillrx.rpg.creature.domain.Speeds

class JsonCharacterPresetRepository(
    private val fileReader: FileReader,
    private val filePath: String,
) : CharacterRepository {
    private var cache: List<Character>? = null

    override suspend fun getAll(filter: CharacterFilter?): List<Character> {
        val all =
            cache ?: loadFromFile()
                .parse()
                .also { cache = it }
        return all.applyFilter(filter)
    }

    override suspend fun get(id: String): Character? = getAll(null).firstOrNull { it.id == id }

    override suspend fun save(character: Character) = Unit

    override suspend fun delete(id: String) = Unit

    private suspend fun loadFromFile(): List<ApiCharacter> {
        val result = fileReader.readFile(filePath)
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<ApiCharacter>.parse(): List<Character> {
            val (characters, errors) = partitionBy { it.toCharacter() }
            errors.forEach { println("WARNING: character preset import error: $it") }
            return characters
        }

        private fun ApiCharacter.toCharacter(): Result<Character, CharacterImportError> {
            val id = id
                ?: return Result.Failure(CharacterImportError.MissingId)
            val name = name
                ?: return Result.Failure(CharacterImportError.MissingName(id))
            val apiTranslations = translations
                ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
            val translationMap = apiTranslations.mapValues { (_, t) ->
                Character.Translation(
                    shortDescription = t.shortDescription.orEmpty(),
                    description = t.description.orEmpty(),
                )
            }
            val translations = translationMap.takeIf { it.isNotEmpty() }
                ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
            val level = level
                ?: return Result.Failure(CharacterImportError.MissingLevel(id))
            val apiSize = this@toCharacter.size
                ?: return Result.Failure(CharacterImportError.MissingSize(id))
            val size = apiSize.toSize()
                ?: return Result.Failure(CharacterImportError.UnknownSize(id, apiSize))
            val apiAlignment = this@toCharacter.alignment
                ?: return Result.Failure(CharacterImportError.MissingAlignment(id))
            val alignment = apiAlignment.toAlignment()
                ?: return Result.Failure(CharacterImportError.UnknownAlignment(id, apiAlignment))
            val armorClass = armorClass
                ?: return Result.Failure(CharacterImportError.MissingArmorClass(id))
            val maxHitPoints = maxHitPoints
                ?: return Result.Failure(CharacterImportError.MissingMaxHitPoints(id))
            val apiSkills = skills
                ?: return Result.Failure(CharacterImportError.MissingSkills(id))
            val apiRace = race
                ?: return Result.Failure(CharacterImportError.MissingRace(id))
            val race = apiRace.toRace()
                ?: return Result.Failure(CharacterImportError.UnknownRace(id, apiRace))
            val apiClazz = clazz
                ?: return Result.Failure(CharacterImportError.MissingClass(id))
            val clazz = apiClazz.toClass()
                ?: return Result.Failure(CharacterImportError.UnknownClass(id, apiClazz))
            val (parsedLanguages, languageErrors) = languages.orEmpty().partitionBy { lang -> lang.toLanguage(id) }
            languageErrors.forEach { println("WARNING: character preset import error: $it") }
            val languages = parsedLanguages.takeIf { languageErrors.isEmpty() }
                ?: return Result.Failure(languageErrors.first())

            return Result.Success(
                Character(
                    id = id,
                    name = name,
                    translations = translations,
                    background = background,
                    race = race,
                    clazz = clazz,
                    level = level,
                    size = size,
                    alignment = alignment,
                    abilities = abilities.toDomain(),
                    armorClass = armorClass,
                    maxHitPoints = maxHitPoints,
                    speeds = speeds.toDomain(),
                    languages = languages,
                    skills = apiSkills.toDomain(),
                ),
            )
        }

        private fun ApiCharacter.ApiAbilities?.toDomain(): Abilities =
            Abilities(
                str = Ability(this?.str ?: Ability.DEFAULT_VALUE),
                dex = Ability(this?.dex ?: Ability.DEFAULT_VALUE),
                con = Ability(this?.con ?: Ability.DEFAULT_VALUE),
                int = Ability(this?.int ?: Ability.DEFAULT_VALUE),
                wis = Ability(this?.wis ?: Ability.DEFAULT_VALUE),
                cha = Ability(this?.cha ?: Ability.DEFAULT_VALUE),
            )

        private fun ApiCharacter.ApiSpeeds?.toDomain(): Speeds =
            Speeds(
                walk = this?.walk,
                fly = this?.fly,
                swim = this?.swim,
                climb = this?.climb,
            )

        private fun String.toRace(): Race? = Race.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toClass(): Character.Class? =
            Character.Class.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toLanguage(id: String): Result<Language, CharacterImportError> =
            Language.entries.find { it.name.equals(this, ignoreCase = true) }
                ?.let { Result.Success(it) }
                ?: Result.Failure(CharacterImportError.UnknownLanguage(id, this))
    }
}
