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

class JsonSpellRepository(
    private val fileReader: FileReader,
    private val locale: String,
) : SpellRepository {

    private var cache: List<Spell>? = null

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        val allSpells = cache ?: loadFromFile()
            .mapNotNull { it.toSpell(locale) }
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
        private const val FALLBACK_LOCALE = "en"

        private fun ApiSpell.toSpell(locale: String): Spell? {
            val id = id ?: return null
            val translation = translations.resolve(locale) ?: return null
            val school = school?.toSchool() ?: return null
            val apiComponents = components ?: return null

            return Spell(
                id = id,
                source = source ?: "srd_5.1",
                title = translation.name.orEmpty(),
                description = translation.description.orEmpty(),
                level = level ?: 0,
                school = school,
                concentration = concentration ?: false,
                ritual = ritual ?: false,
                castingTime = translation.castingTime.orEmpty(),
                range = translation.range.orEmpty(),
                duration = translation.duration.orEmpty(),
                components = SpellComponents(
                    verbal = apiComponents.verbal,
                    somatic = apiComponents.somatic,
                    material = apiComponents.material,
                ),
                materialDescription = translation.materialDescription,
                availableClasses = availableClasses?.mapNotNull { it.toPlayerClass() } ?: emptyList(),
            )
        }

        private fun Map<String, ApiSpell.Translation>?.resolve(locale: String): ApiSpell.Translation? {
            if (isNullOrEmpty()) return null
            return get(locale) ?: get(FALLBACK_LOCALE) ?: entries.minByOrNull { it.key }?.value
        }

        private fun String.toSchool(): Spell.School? =
            Spell.School.entries.firstOrNull { it.name.lowercase() == this.lowercase() }

        private fun String.toPlayerClass(): PlayerCharacter.Class? =
            PlayerCharacter.Class.entries.firstOrNull { it.name.lowercase() == this.lowercase() }
    }
}
