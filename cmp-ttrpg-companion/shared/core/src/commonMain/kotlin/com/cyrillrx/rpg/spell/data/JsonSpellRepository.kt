package com.cyrillrx.rpg.spell.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.api.ApiSpell
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.domain.applyFilter

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    private var cache: List<Spell>? = null

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        val allSpells = cache ?: loadAndParse()
        return allSpells.applyFilter(filter)
    }

    private suspend fun loadAndParse(): List<Spell> {
        val apiSpells = loadFromFile()
        val results = apiSpells.map { it.toSpell() }

        val failures = results.filterIsInstance<Result.Failure<SpellImportError>>()
        val successes = results.filterIsInstance<Result.Success<Spell>>()
        failures.forEach { println("WARNING: spell import error: ${it.error}") }
        return successes.map { it.value }.also { cache = it }
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
        private fun ApiSpell.toSpell(): Result<Spell, SpellImportError> {
            val id = id
                ?: return Result.Failure(SpellImportError.MissingId)
            val source = source
                ?: return Result.Failure(SpellImportError.MissingSource(id))
            val level = level
                ?: return Result.Failure(SpellImportError.MissingLevel(id))
            val school = school?.toSchool()
                ?: return Result.Failure(SpellImportError.UnknownSchool(id, school.orEmpty()))
            val concentration = concentration
                ?: return Result.Failure(SpellImportError.MissingConcentration(id))
            val ritual = ritual
                ?: return Result.Failure(SpellImportError.MissingRitual(id))
            val components = components
                ?: return Result.Failure(SpellImportError.MissingComponents(id))
            val availableClasses = availableClasses
                ?.mapNotNull { it.toPlayerClass() }
                ?: return Result.Failure(SpellImportError.MissingAvailableClasses(id))
            val translations = translations
                ?.mapValues { (_, apiSpellTranslation) -> apiSpellTranslation.toDomain() }
                ?.takeIf { it.isNotEmpty() }
                ?: return Result.Failure(SpellImportError.MissingTranslations(id))

            val spell = Spell(
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
            )
            return Result.Success(spell)
        }

        private fun ApiSpell.Translation.toDomain() = Spell.Translation(
            name = name.orEmpty(),
            castingTime = castingTime.orEmpty(),
            range = range.orEmpty(),
            duration = duration.orEmpty(),
            materialDescription = materialDescription,
            description = description.orEmpty(),
        )

        private fun String.toSchool(): Spell.School? =
            Spell.School.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toPlayerClass(): PlayerCharacter.Class? =
            PlayerCharacter.Class.entries.find { it.name.equals(this, ignoreCase = true) }
    }
}
