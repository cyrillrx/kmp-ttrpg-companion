package com.cyrillrx.rpg.spell.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.api.ApiSpell
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.domain.applyFilter

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    private var cache: List<Spell>? = null

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        val allSpells = cache ?: loadFromFile()
            .parse()
            .also { cache = it }
        return allSpells.applyFilter(filter)
    }

    override suspend fun getById(id: String): Spell? =
        getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<Spell> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    private suspend fun loadFromFile(): List<ApiSpell> {
        val result = fileReader.readFile("files/spells.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<ApiSpell>.parse(): List<Spell> {
            val (spells, errors) = partitionBy { it.toSpell() }
            errors.forEach { println("WARNING: spell import error: $it") }
            return spells
        }

        private fun ApiSpell.toSpell(): Result<Spell, SpellImportError> {
            val id = id
                ?: return Result.Failure(SpellImportError.MissingId)
            val source = source
                ?: return Result.Failure(SpellImportError.MissingSource(id))
            val level = level
                ?: return Result.Failure(SpellImportError.MissingLevel(id))
            val apiSchool = school
            val school = apiSchool?.toSchool()
                ?: return Result.Failure(SpellImportError.UnknownSchool(id, apiSchool.orEmpty()))
            val concentration = concentration
                ?: return Result.Failure(SpellImportError.MissingConcentration(id))
            val ritual = ritual
                ?: return Result.Failure(SpellImportError.MissingRitual(id))
            val components = components
                ?: return Result.Failure(SpellImportError.MissingComponents(id))
            val apiAvailableClasses = availableClasses
                ?: return Result.Failure(SpellImportError.MissingAvailableClasses(id))
            val (availableClasses, classErrors) = apiAvailableClasses.partitionBy { apiClass ->
                apiClass.toPlayerClass()?.let { Result.Success(it) }
                    ?: Result.Failure(SpellImportError.UnknownClass(id, apiClass))
            }
            classErrors.forEach { println("WARNING: $it") }
            if (availableClasses.isEmpty()) return Result.Failure(SpellImportError.EmptyAvailableClasses(id))
            val apiTranslations = translations
                ?: return Result.Failure(SpellImportError.MissingTranslations(id))
            val (translationMap, translationErrors) = apiTranslations.partitionBy { locale, t ->
                t.toDomain(id, locale)
            }
            translationErrors.forEach { println("WARNING: spell import error: $it") }
            val translations = translationMap.takeIf { it.isNotEmpty() }
                ?: return Result.Failure(SpellImportError.MissingTranslations(id))

            return Result.Success(
                Spell(
                    id = id,
                    source = source,
                    level = level,
                    school = school,
                    concentration = concentration,
                    ritual = ritual,
                    components = Spell.Components(
                        verbal = components.verbal,
                        somatic = components.somatic,
                        material = components.material,
                    ),
                    availableClasses = availableClasses,
                    translations = translations,
                ),
            )
        }

        private fun ApiSpell.Translation.toDomain(
            spellId: String,
            locale: String,
        ): Result<Spell.Translation, SpellImportError> {
            val name = name ?: return Result.Failure(SpellImportError.InvalidTranslation(spellId, locale))
            val castingTime = castingTime ?: return Result.Failure(SpellImportError.InvalidTranslation(spellId, locale))
            val range = range ?: return Result.Failure(SpellImportError.InvalidTranslation(spellId, locale))
            val duration = duration ?: return Result.Failure(SpellImportError.InvalidTranslation(spellId, locale))
            val description = description ?: return Result.Failure(SpellImportError.InvalidTranslation(spellId, locale))
            return Result.Success(
                Spell.Translation(
                    name = name,
                    castingTime = castingTime,
                    range = range,
                    duration = duration,
                    materialDescription = materialDescription,
                    description = description,
                ),
            )
        }

        private fun String.toSchool(): Spell.School? =
            Spell.School.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toPlayerClass(): PlayerCharacter.Class? =
            PlayerCharacter.Class.entries.find { it.name.equals(this, ignoreCase = true) }
    }
}
