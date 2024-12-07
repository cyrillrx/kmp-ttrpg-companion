package com.cyrillrx.rpg.bestiary.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.bestiary.data.api.ApiBestiaryItem
import com.cyrillrx.rpg.bestiary.domain.Abilities
import com.cyrillrx.rpg.bestiary.domain.Ability
import com.cyrillrx.rpg.bestiary.domain.BestiaryRepository
import com.cyrillrx.rpg.bestiary.domain.Creature

class JsonBestiaryRepository(private val fileReader: FileReader) : BestiaryRepository {

    override suspend fun getAll(): List<Creature> {
        val items = loadFromFile()
        return items.map { it.toCreature() }
    }

    private suspend fun loadFromFile(): List<ApiBestiaryItem> {
        val result = fileReader.readFile("files/bestiaire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun ApiBestiaryItem.toCreature(): Creature {
            val abilities = header?.monster

            return Creature(
                name = title ?: "",
                description = content ?: "",
                type = type ?: "",
                subtype = subtype ?: "",
                size = size ?: "",
                alignment = alignment ?: "",
                abilities = Abilities(
                    extractAbilities(abilities?.str),
                    extractAbilities(abilities?.dex),
                    extractAbilities(abilities?.con),
                    extractAbilities(abilities?.int),
                    extractAbilities(abilities?.wis),
                    extractAbilities(abilities?.cha),
                ),
            )
        }

        private fun extractAbilities(ability: String?): Int {
            return ability
                ?.split(" ")
                ?.firstOrNull()
                ?.toIntOrNull()
                ?: Ability.DEFAULT_VALUE
        }
    }
}
