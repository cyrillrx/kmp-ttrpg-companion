package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.data.api.ApiBestiaryItem
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature

class JsonCreatureRepository(private val fileReader: FileReader) : CreatureRepository {

    override suspend fun getAll(): List<Creature> {
        val items = loadFromFile()
        return items.map { it.toCreature() }
    }

    override suspend fun filter(query: String): List<Creature> {
        return getAll().filter(query)
    }

    private suspend fun loadFromFile(): List<ApiBestiaryItem> {
        val result = fileReader.readFile("files/bestiaire.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<Creature>.filter(query: String): ArrayList<Creature> =
            filterTo(ArrayList()) { spell -> spell.filter(query) }

        private fun Creature.filter(query: String): Boolean {
            val lowerCaseQuery = query.trim().lowercase()
            return name.lowercase().contains(lowerCaseQuery)
        }

        private fun ApiBestiaryItem.toCreature(): Creature {
            val monster = header?.monster

            return Creature(
                name = title ?: "",
                description = content ?: "",
                type = getType(),
                subtype = getSubtype(),
                size = getSize(),
                alignment = getAlignment(),
                abilities = Abilities(
                    extractAbilities(monster?.str),
                    extractAbilities(monster?.dex),
                    extractAbilities(monster?.con),
                    extractAbilities(monster?.int),
                    extractAbilities(monster?.wis),
                    extractAbilities(monster?.cha),
                ),
                armorClass = monster.getArmorClass(),
                maxHitPoints = monster.getHitPoints(),
                speed = monster?.speed ?: "",
                languages = emptyList(),
            )
        }

        private fun ApiBestiaryItem.getType(): Creature.Type {
            val type = truetype?.split(" (")?.getOrNull(0) ?: ""
            return when (type) {
                "Aberration" -> Creature.Type.ABERRATION
                "Bête" -> Creature.Type.BEAST
                "Céleste" -> Creature.Type.CELESTIAL
                "Créature artificielle" -> Creature.Type.CONSTRUCT
                "Dragon" -> Creature.Type.DRAGON
                "Élémentaire" -> Creature.Type.ELEMENTAL
                "Fée" -> Creature.Type.FEY
                "Fiélon" -> Creature.Type.FIEND
                "Géant" -> Creature.Type.GIANT
                "Humanoïde" -> Creature.Type.HUMANOID
                "Créature monstrueuse" -> Creature.Type.MONSTROSITY
                "Vase" -> Creature.Type.OOZE
                "Plant" -> Creature.Type.PLANT
                "Undead" -> Creature.Type.UNDEAD
                else -> Creature.Type.UNKNOWN
            }
        }

        private fun ApiBestiaryItem.getSubtype(): String {
            return truetype
                ?.split(" (")
                ?.getOrNull(1)
                ?.replace(")", "")
                .orEmpty()
        }

        private fun ApiBestiaryItem.getSize(): Creature.Size {
            return when (size) {
                "TP" -> Creature.Size.TINY
                "P" -> Creature.Size.SMALL
                "M" -> Creature.Size.MEDIUM
                "G" -> Creature.Size.LARGE
                "TG" -> Creature.Size.HUGE
                "Gig" -> Creature.Size.GARGANTUAN
                else -> Creature.Size.UNKNOWN
            }
        }

        private fun ApiBestiaryItem.getAlignment(): Creature.Alignment {
            return when (alignment) {
                "Loyal bon" -> Creature.Alignment.LAWFUL_GOOD
                "Loyal neutre" -> Creature.Alignment.LAWFUL_NEUTRAL
                "Loyal mauvais" -> Creature.Alignment.LAWFUL_EVIL
                "Neutre bon" -> Creature.Alignment.NEUTRAL_GOOD
                "Neutre" -> Creature.Alignment.NEUTRAL
                "Neutre mauvais" -> Creature.Alignment.NEUTRAL_EVIL
                "Chaotique bon" -> Creature.Alignment.CHAOTIC_GOOD
                "Chaotique neutral" -> Creature.Alignment.CHAOTIC_NEUTRAL
                "Chaotique mauvais" -> Creature.Alignment.CHAOTIC_EVIL
                else -> Creature.Alignment.UNKNOWN
            }
        }

        private fun ApiMonster?.getArmorClass(): Int {
            return this?.ac?.split(' ')?.firstOrNull()?.toIntOrNull() ?: 10
        }

        private fun ApiMonster?.getHitPoints(): Int {
            return this?.hp?.split(' ')?.firstOrNull()?.toIntOrNull() ?: 10
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
