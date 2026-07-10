package com.cyrillrx.rpg.character.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.character.data.api.ApiCharacter
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.creature.data.createAbilities
import com.cyrillrx.rpg.creature.data.toAlignment
import com.cyrillrx.rpg.creature.data.toSize
import com.cyrillrx.rpg.creature.data.toSkills
import com.cyrillrx.rpg.creature.data.toSpeeds
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JsonCharacterPresetRepository(
    private val fileReader: FileReader,
    private val filePath: String,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : CharacterRepository {
    private var cache: List<Character>? = null

    override suspend fun getAll(filter: CharacterFilter?): List<Character> = withContext(ioDispatcher) {
        val all = cache ?: loadFromFile()
            .parse()
            .also { cache = it }
        all.applyFilter(filter)
    }

    override suspend fun get(id: String): Character? = getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<Character> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

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
            val translations = apiTranslations.toTranslations(id)
                ?: return Result.Failure(CharacterImportError.MissingTranslations(id))
            val level = level
                ?: return Result.Failure(CharacterImportError.MissingLevel(id))
            val apiSize = size
                ?: return Result.Failure(CharacterImportError.MissingSize(id))
            val size = apiSize.toSize()
                ?: return Result.Failure(CharacterImportError.UnknownSize(id, apiSize))
            val apiAlignment = alignment
                ?: return Result.Failure(CharacterImportError.MissingAlignment(id))
            val alignment = apiAlignment.toAlignment()
                ?: return Result.Failure(CharacterImportError.UnknownAlignment(id, apiAlignment))
            val armorClass = armorClass
                ?: return Result.Failure(CharacterImportError.MissingArmorClass(id))
            val maxHitPoints = maxHitPoints
                ?: return Result.Failure(CharacterImportError.MissingMaxHitPoints(id))
            speeds?.walk
                ?: return Result.Failure(CharacterImportError.MissingWalkSpeed(id))
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
                    background = background?.toBackground(),
                    race = race,
                    clazz = clazz,
                    level = level,
                    size = size,
                    alignment = alignment,
                    abilities = createAbilities(abilities, savingThrows),
                    armorClass = armorClass,
                    maxHitPoints = maxHitPoints,
                    speeds = speeds.toSpeeds(),
                    languages = languages,
                    skills = apiSkills.toSkills(),
                ),
            )
        }

        private fun Map<String, ApiCharacter.Translation>.toTranslations(
            characterId: String,
        ): Map<String, Character.Translation>? {
            val (parsedTranslations, translationErrors) = partitionBy { locale, t ->
                t.toTranslation(characterId, locale)
            }
            translationErrors.forEach { println("WARNING: character preset import error: $it") }
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
    }
}
