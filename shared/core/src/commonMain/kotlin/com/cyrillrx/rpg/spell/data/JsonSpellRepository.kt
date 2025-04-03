package com.cyrillrx.rpg.spell.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.character.domain.PlayerCharacter
import com.cyrillrx.rpg.spell.data.api.ApiSpell
import com.cyrillrx.rpg.spell.domain.Spell
import com.cyrillrx.rpg.spell.domain.Spell.School
import com.cyrillrx.rpg.spell.domain.SpellFilter
import com.cyrillrx.rpg.spell.domain.SpellRepository

class JsonSpellRepository(private val fileReader: FileReader) : SpellRepository {

    override suspend fun getAll(filter: SpellFilter?): List<Spell> {
        val item = loadFromFile()
        val allSpells = item.map { it.toSpell() }

        filter ?: return allSpells

        return allSpells.filter(filter::matches)
    }

    private suspend fun loadFromFile(): List<ApiSpell> {
        val result = fileReader.readFile("files/grimoire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
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

        private fun ApiSpell.getAvailableClasses(): List<PlayerCharacter.Class> {
            val apiCharacterClasses = header?.taxonomy?.spell_class
            return apiCharacterClasses?.mapNotNull { it?.toCharacterClasses() } ?: emptyList()
        }

        private fun String.toCharacterClasses(): PlayerCharacter.Class? {
            return when (this) {
                "Bard" -> return PlayerCharacter.Class.BARD
                "Clerc" -> return PlayerCharacter.Class.CLERIC
                "Druide" -> return PlayerCharacter.Class.DRUID
                "Paladin" -> return PlayerCharacter.Class.PALADIN
                "Ranger" -> return PlayerCharacter.Class.RANGER
                "Ensorceleur/Sorcelame" -> return PlayerCharacter.Class.SORCERER
                "Sorcier" -> return PlayerCharacter.Class.WARLOCK
                "Magicien" -> return PlayerCharacter.Class.WIZARD
                else -> null
            }
        }
    }
}
