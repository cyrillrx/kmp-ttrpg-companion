package com.cyrillrx.rpg.bestiary.data

import com.cyrillrx.rpg.api.bestiary.ApiBestiaryItem
import com.cyrillrx.rpg.bestiary.domain.BestiaryRepository
import com.cyrillrx.rpg.models.bestiary.Abilities
import com.cyrillrx.rpg.models.bestiary.Ability
import com.cyrillrx.rpg.models.bestiary.Creature
import com.cyrillrx.utils.deserialize
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rpg_companion.composeapp.generated.resources.Res

class JsonBestiaryRepository : BestiaryRepository {

    override suspend fun getAll(): List<Creature> {
        val items = loadFromFile()
        return items.map { it.toCreature() }
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadFromFile(): List<ApiBestiaryItem> {
        val bytes = Res.readBytes("files/bestiaire.json")
        val serializedBestiary: String = bytes.decodeToString()
        return serializedBestiary.deserialize() ?: listOf()
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
