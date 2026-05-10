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
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds

class JsonCharacterPresetRepository(
    private val fileReader: FileReader,
) : CharacterRepository {
    private var cache: List<Character>? = null

    override suspend fun getAll(filter: CharacterFilter?): List<Character> {
        val all = cache ?: loadFromFile().parse().also { cache = it }
        return all.applyFilter(filter)
    }

    override suspend fun get(id: String): Character? = getAll(null).firstOrNull { it.id == id }

    override suspend fun save(character: Character) = Unit

    override suspend fun delete(id: String) = Unit

    private suspend fun loadFromFile(): List<ApiCharacter> {
        val result = fileReader.readFile("files/character_presets.json")
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
            val id = id ?: return Result.Failure(CharacterImportError.MissingId)
            val name = name ?: return Result.Failure(CharacterImportError.MissingName(id))
            val resolvedSize =
                size?.let { s ->
                    s.toSize() ?: return Result.Failure(CharacterImportError.UnknownSize(id, s))
                } ?: Creature.Size.MEDIUM
            val resolvedAlignment =
                alignment?.let { a ->
                    a.toAlignment() ?: return Result.Failure(CharacterImportError.UnknownAlignment(id, a))
                } ?: Creature.Alignment.NEUTRAL

            val characterTranslations =
                translations
                    ?.mapValues { (_, t) ->
                        Character.Translation(
                            shortDescription = t.shortDescription.orEmpty(),
                            description = t.description.orEmpty(),
                        )
                    }
                    ?: emptyMap()

            return Result.Success(
                Character(
                    id = id,
                    name = name,
                    translations = characterTranslations,
                    description = characterTranslations["en"]?.description.orEmpty(),
                    background = background,
                    race = race?.toRace() ?: Race.HUMAN,
                    clazz = clazz?.toClass() ?: Character.Class.UNKNOWN,
                    level = level ?: 1,
                    size = resolvedSize,
                    alignment = resolvedAlignment,
                    abilities = abilities.toDomain(),
                    armorClass = armorClass ?: 10,
                    maxHitPoints = maxHitPoints ?: 10,
                    speeds = speeds.toDomain(),
                    languages = languages ?: emptyList(),
                    skills = Skills(),
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

        private fun String.toClass(): Character.Class? = Character.Class.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toSize(): Creature.Size? = Creature.Size.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toAlignment(): Creature.Alignment? = Creature.Alignment.entries.find { it.name.equals(this, ignoreCase = true) }
    }
}
