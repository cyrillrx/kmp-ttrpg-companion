package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.creature.data.api.ApiBestiaryItem
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.BaseCreature
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.domain.CreatureRepository

class JsonCreatureRepository(private val fileReader: FileReader) : CreatureRepository {

    override suspend fun getAll(filter: CreatureFilter?): List<Creature> {
        val items = loadFromFile()
        val allCreatures = items.map { it.toCreature() }

        filter ?: return allCreatures

        return allCreatures.filter(filter::matches)
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
            val monster = header?.monster

            return Creature(
                id = title ?: "",
                name = title ?: "",
                description = content ?: "",
                type = getType(),
                subtype = getSubtype(),
                size = getSize(),
                alignment = getAlignment(),
                challengeRating = challengeRating(),
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

        private fun ApiBestiaryItem.getSize(): BaseCreature.Size {
            return when (size) {
                "TP" -> BaseCreature.Size.TINY
                "P" -> BaseCreature.Size.SMALL
                "M" -> BaseCreature.Size.MEDIUM
                "G" -> BaseCreature.Size.LARGE
                "TG" -> BaseCreature.Size.HUGE
                "Gig" -> BaseCreature.Size.GARGANTUAN
                else -> BaseCreature.Size.UNKNOWN
            }
        }

        private fun ApiBestiaryItem.getAlignment(): BaseCreature.Alignment {
            return when (alignment) {
                "Loyal bon" -> BaseCreature.Alignment.LAWFUL_GOOD
                "Loyal neutre" -> BaseCreature.Alignment.LAWFUL_NEUTRAL
                "Loyal mauvais" -> BaseCreature.Alignment.LAWFUL_EVIL
                "Neutre bon" -> BaseCreature.Alignment.NEUTRAL_GOOD
                "Neutre" -> BaseCreature.Alignment.NEUTRAL
                "Neutre mauvais" -> BaseCreature.Alignment.NEUTRAL_EVIL
                "Chaotique bon" -> BaseCreature.Alignment.CHAOTIC_GOOD
                "Chaotique neutral" -> BaseCreature.Alignment.CHAOTIC_NEUTRAL
                "Chaotique mauvais" -> BaseCreature.Alignment.CHAOTIC_EVIL
                else -> BaseCreature.Alignment.UNKNOWN
            }
        }

        private fun ApiBestiaryItem.challengeRating(): Float = challenge ?: 0f

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
