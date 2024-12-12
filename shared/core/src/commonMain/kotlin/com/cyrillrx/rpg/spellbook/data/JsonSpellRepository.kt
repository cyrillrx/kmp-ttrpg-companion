package com.cyrillrx.rpg.spellbook.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.core.domain.Character
import com.cyrillrx.rpg.spellbook.data.api.ApiSpell
import com.cyrillrx.rpg.spellbook.domain.Spell
import com.cyrillrx.rpg.spellbook.domain.Spell.School
import com.cyrillrx.rpg.spellbook.domain.SpellRepository

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    override suspend fun getAll(): List<Spell> {
        val item = loadFromFile()
        return item.map { it.toSpell() }
    }

    override suspend fun filter(query: String): List<Spell> {
        return getAll().filter(query)
    }

    private suspend fun loadFromFile(): List<ApiSpell> {
        val result = fileReader.readFile("files/grimoire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<Spell>.filter(query: String): ArrayList<Spell> =
            filterTo(ArrayList()) { spell -> spell.matches(query) }

        private fun Spell.matches(query: String): Boolean {
            val lowerCaseQuery = query.trim().lowercase()
            return title.lowercase().contains(lowerCaseQuery) ||
                description.lowercase().contains(lowerCaseQuery)
        }

        private fun ApiSpell.toSpell(): Spell {
            return Spell(
                title = title.orEmpty(),
                description = content.orEmpty(),
                level = level ?: 0,
                castingTime = casting_time.orEmpty(),
                range = range.orEmpty(),
                components = components.orEmpty(),
                duration = duration.orEmpty(),
                schools = getSchools(),
                availableClasses = getAvailableClasses(),
            )
        }

        private fun ApiSpell.getSchools(): List<School> {
            val apiSchool = header?.taxonomy?.spell_school
            return apiSchool?.mapNotNull { it.toSchool() } ?: emptyList()
        }

        private fun String.toSchool(): School? {
            return when (this) {
                "Abjuration" -> return School.ABJURATION
                "Invocation" -> return School.CONJURATION
                "Divination" -> return School.DIVINATION
                "Enchantement" -> return School.ENCHANTMENT
                "Évocation" -> return School.EVOCATION
                "Illusion" -> return School.ILLUSION
                "Nécromancie" -> return School.NECROMANCY
                "Transmutation" -> return School.TRANSMUTATION
                else -> null
            }
        }

        private fun ApiSpell.getAvailableClasses(): List<Character.Class> {
            val apiCharacterClasses = header?.taxonomy?.spell_class
            return apiCharacterClasses?.mapNotNull { it?.toCharacterClasses() } ?: emptyList()
        }

        private fun String.toCharacterClasses(): Character.Class? {
            return when (this) {
                "Bard" -> return Character.Class.BARD
                "Clerc" -> return Character.Class.CLERIC
                "Druide" -> return Character.Class.DRUID
                "Paladin" -> return Character.Class.PALADIN
                "Ranger" -> return Character.Class.RANGER
                "Ensorceleur/Sorcelame" -> return Character.Class.SORCERER
                "Sorcier" -> return Character.Class.WARLOCK
                "Magicien" -> return Character.Class.WIZARD
                else -> null
            }
        }
    }
}
