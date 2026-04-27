package com.cyrillrx.rpg.spell.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.api.ApiSpell
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.SpellComponents
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository
import com.cyrillrx.rpg.spell.domain.applyFilter

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    private var cache: List<Spell>? = null

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        val allSpells = cache ?: loadFromFile()
            .mapNotNull { it.toSpell() }
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
        private fun ApiSpell.toSpell(): Spell? {
            val id = id ?: return null
            val translations = translations
                ?.mapValues { (_, t) -> t.toDomain() }
                ?.takeIf { it.isNotEmpty() }
                ?: return null
            val school = school?.toSchool() ?: return null
            val apiComponents = components ?: return null

            return Spell(
                id = id,
                source = source ?: "srd_5.1",
                level = level ?: 0,
                school = school,
                concentration = concentration ?: false,
                ritual = ritual ?: false,
                components = SpellComponents(
                    verbal = apiComponents.verbal,
                    somatic = apiComponents.somatic,
                    material = apiComponents.material,
                ),
                availableClasses = availableClasses?.mapNotNull { it.toPlayerClass() } ?: emptyList(),
                translations = translations,
            )
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
            Spell.School.entries.firstOrNull { it.name.lowercase() == this.lowercase() }

        private fun String.toPlayerClass(): PlayerCharacter.Class? =
            PlayerCharacter.Class.entries.firstOrNull { it.name.lowercase() == this.lowercase() }
    }
}
