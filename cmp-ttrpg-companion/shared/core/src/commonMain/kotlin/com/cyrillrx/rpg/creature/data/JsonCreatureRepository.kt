package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.rpg.creature.data.api.ApiBestiaryItem
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.domain.CreatureRepository
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.applyFilter

class JsonCreatureRepository(private val fileReader: FileReader) : CreatureRepository {

    private var cache: List<Monster>? = null

    override suspend fun getAll(filter: CreatureFilter?): List<Monster> {
        val allCreatures = cache ?: loadFromFile()
            .map { it.toCreature() }
            .also { cache = it }

        return allCreatures.applyFilter(filter)
    }

    override suspend fun getById(id: String): Monster? =
        getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<Monster> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    private suspend fun loadFromFile(): List<ApiBestiaryItem> {
        val result = fileReader.readFile("files/creatures.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun ApiBestiaryItem.toCreature(): Monster {
            val monster = header?.monster

            return Monster(
                id = title ?: "",
                name = title ?: "",
                description = content ?: "",
                source = "srd_5.1",
                type = getType(),
                size = getSize(),
                alignment = getAlignment(),
                challengeRating = challengeRating(),
                hitDice = header?.monster.getHitDice(),
                abilities = Abilities(
                    str = Ability(extractAbilities(monster?.str)),
                    dex = Ability(extractAbilities(monster?.dex)),
                    con = Ability(extractAbilities(monster?.con)),
                    int = Ability(extractAbilities(monster?.int)),
                    wis = Ability(extractAbilities(monster?.wis)),
                    cha = Ability(extractAbilities(monster?.cha)),
                ),
                armorClass = monster.getArmorClass(),
                maxHitPoints = monster.getHitPoints(),
                speed = monster?.speed ?: "",
                languages = emptyList(),
            )
        }

        private fun ApiBestiaryItem.getType(): Monster.Type {
            val type = truetype?.split(" (")?.getOrNull(0) ?: ""
            return when (type) {
                "Aberration" -> Monster.Type.ABERRATION
                "Bête" -> Monster.Type.BEAST
                "Céleste" -> Monster.Type.CELESTIAL
                "Créature artificielle" -> Monster.Type.CONSTRUCT
                "Dragon" -> Monster.Type.DRAGON
                "Élémentaire" -> Monster.Type.ELEMENTAL
                "Fée" -> Monster.Type.FEY
                "Fiélon" -> Monster.Type.FIEND
                "Géant" -> Monster.Type.GIANT
                "Humanoïde" -> Monster.Type.HUMANOID
                "Créature monstrueuse" -> Monster.Type.MONSTROSITY
                "Vase" -> Monster.Type.OOZE
                "Plant" -> Monster.Type.PLANT
                "Undead" -> Monster.Type.UNDEAD
                else -> Monster.Type.UNKNOWN
            }
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

        private fun ApiBestiaryItem.challengeRating(): Float = challenge ?: 0f

        private fun ApiMonster?.getArmorClass(): Int {
            return this?.ac?.split(' ')?.firstOrNull()?.toIntOrNull() ?: 10
        }

        private fun ApiMonster?.getHitPoints(): Int {
            return this?.hp?.split(' ')?.firstOrNull()?.toIntOrNull() ?: 10
        }

        private fun ApiMonster?.getHitDice(): String {
            // hp format: "7 (2d6)" or "262 (19d12 + 114)"
            return this?.hp
                ?.substringAfter("(", "")
                ?.substringBefore(")", "")
                ?.replace(" ", "")
                .orEmpty()
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
