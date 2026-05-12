package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Language
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds

class SampleCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Character> = characters.applyFilter(filter)

    override suspend fun get(id: String): Character? = characters.firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<Character> {
        val all = characters.associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    override suspend fun save(character: Character) = Unit

    override suspend fun delete(id: String) = Unit

    companion object {
        private val characters: List<Character> =
            listOf(
                humanFighter(),
                elfRogue(),
            )

        fun getAll(): List<Character> = characters

        fun humanFighter() =
            Character(
                id = "sample-fighter",
                name = "Borin Pierrenoire",
                size = Creature.Size.MEDIUM,
                alignment = Creature.Alignment.LAWFUL_GOOD,
                abilities =
                    Abilities(
                        str = Ability(16),
                        dex = Ability(12),
                        con = Ability(14),
                        int = Ability(10),
                        wis = Ability(10),
                        cha = Ability(8),
                    ),
                armorClass = 16,
                maxHitPoints = 12,
                speeds = Speeds(walk = 30),
                languages = listOf(Language.COMMON, Language.DWARVISH),
                level = 1,
                clazz = Character.Class.FIGHTER,
                skills = Skills(),
            )

        fun elfRogue() =
            Character(
                id = "sample-rogue",
                name = "Lyra Vossen",
                size = Creature.Size.MEDIUM,
                alignment = Creature.Alignment.CHAOTIC_NEUTRAL,
                abilities =
                    Abilities(
                        str = Ability(8),
                        dex = Ability(16),
                        con = Ability(12),
                        int = Ability(14),
                        wis = Ability(10),
                        cha = Ability(14),
                    ),
                armorClass = 14,
                maxHitPoints = 8,
                speeds = Speeds(walk = 30),
                languages = listOf(Language.COMMON, Language.ELVISH),
                level = 1,
                clazz = Character.Class.ROGUE,
                skills = Skills(),
            )
    }
}
