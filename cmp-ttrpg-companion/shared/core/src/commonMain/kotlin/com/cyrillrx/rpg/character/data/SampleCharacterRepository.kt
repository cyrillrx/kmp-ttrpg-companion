package com.cyrillrx.rpg.character.data

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.applyFilter
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
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
                        strength = Ability(16, Proficiency.PROFICIENT),
                        dexterity = Ability(12),
                        constitution = Ability(14, Proficiency.PROFICIENT),
                        intelligence = Ability(10),
                        wisdom = Ability(10),
                        charisma = Ability(8),
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
                        strength = Ability(8),
                        dexterity = Ability(16),
                        constitution = Ability(12),
                        intelligence = Ability(14),
                        wisdom = Ability(10),
                        charisma = Ability(14),
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
