package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.Speeds
import com.cyrillrx.rpg.creature.domain.applyFilter

class SampleMonsterRepository : MonsterRepository {
    override suspend fun getAll(filter: MonsterFilter?): List<Monster> = monsters.applyFilter(filter)

    override suspend fun getById(id: String): Monster? = monsters.firstOrNull { it.id == id }

    companion object {
        private val monsters: List<Monster> = listOf(
            goblin(),
            youngRedDragon(),
            skeleton(),
            direWolf(),
            balor(),
            gelatinousCube(),
            celestialFiend(),
        )

        fun getAll(): List<Monster> = monsters

        fun getFirst(): Monster = monsters.first()

        fun goblin() = Monster(
            id = "1",
            source = "srd_5.1",
            types = setOf(Monster.Type.HUMANOID),
            size = Creature.Size.SMALL,
            alignment = Creature.Alignment.NEUTRAL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d6",
            abilities = Abilities(
                strength = AbilityScore(8),
                dexterity = AbilityScore(14),
                constitution = AbilityScore(10),
                intelligence = AbilityScore(10),
                wisdom = AbilityScore(8),
                charisma = AbilityScore(8),
            ),
            armorClass = 15,
            maxHitPoints = 7,
            speeds = Speeds(walk = 30),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Goblin",
                    description = "A small, black-hearted creature that lairs in despoiled dungeons and other " +
                        "dismal settings.",
                    senses = "Darkvision 60 ft.",
                    languages = listOf("Common", "Goblin"),
                ),
            ),
        )

        fun youngRedDragon() = Monster(
            id = "2",
            source = "srd_5.1",
            types = setOf(Monster.Type.DRAGON),
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 10f,
            hitDice = "17d10+85",
            abilities = Abilities(
                strength = AbilityScore(23),
                dexterity = AbilityScore(10),
                constitution = AbilityScore(21),
                intelligence = AbilityScore(14),
                wisdom = AbilityScore(11),
                charisma = AbilityScore(19),
            ),
            armorClass = 18,
            maxHitPoints = 178,
            speeds = Speeds(walk = 40, climb = 40, fly = 80),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Young Red Dragon",
                    description = "A fierce dragon that breathes fire and terrorizes the countryside.",
                    senses = "Blindsight 30 ft., Darkvision 120 ft.",
                    languages = listOf("Common", "Draconic"),
                ),
            ),
        )

        private fun skeleton() = Monster(
            id = "3",
            source = "srd_5.1",
            types = setOf(Monster.Type.UNDEAD),
            size = Creature.Size.MEDIUM,
            alignment = Creature.Alignment.LAWFUL_EVIL,
            challengeRating = 0.25f,
            hitDice = "2d8+4",
            abilities = Abilities(
                strength = AbilityScore(10),
                dexterity = AbilityScore(14),
                constitution = AbilityScore(15),
                intelligence = AbilityScore(6),
                wisdom = AbilityScore(8),
                charisma = AbilityScore(5),
            ),
            armorClass = 13,
            maxHitPoints = 13,
            speeds = Speeds(walk = 30),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Skeleton",
                    description = "An animated pile of bones held together by dark magic.",
                    senses = "Darkvision 60 ft.",
                    languages = listOf("understands languages it knew in life"),
                ),
            ),
        )

        private fun direWolf() = Monster(
            id = "4",
            source = "srd_5.1",
            types = setOf(Monster.Type.BEAST),
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 1f,
            hitDice = "5d10+10",
            abilities = Abilities(
                strength = AbilityScore(17),
                dexterity = AbilityScore(15),
                constitution = AbilityScore(15),
                intelligence = AbilityScore(3),
                wisdom = AbilityScore(12),
                charisma = AbilityScore(7),
            ),
            armorClass = 14,
            maxHitPoints = 37,
            speeds = Speeds(walk = 50),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Dire Wolf",
                    description = "A large and fearsome wolf that hunts in packs.",
                    senses = "Passive Perception 13",
                    languages = emptyList(),
                ),
            ),
        )

        fun balor() = Monster(
            id = "5",
            source = "srd_5.1",
            types = setOf(Monster.Type.FIEND),
            size = Creature.Size.HUGE,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 19f,
            hitDice = "19d12+114",
            abilities = Abilities(
                strength = AbilityScore(26),
                dexterity = AbilityScore(15),
                constitution = AbilityScore(22),
                intelligence = AbilityScore(20),
                wisdom = AbilityScore(16),
                charisma = AbilityScore(22),
            ),
            armorClass = 19,
            maxHitPoints = 262,
            speeds = Speeds(walk = 40, fly = 80),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Balor",
                    description = "A towering fiend wreathed in flame, wielding a whip and longsword of fire.",
                    senses = "Truesight 120 ft.",
                    languages = listOf("Abyssal", "telepathy 120 ft."),
                ),
            ),
        )

        private fun gelatinousCube() = Monster(
            id = "6",
            source = "srd_5.1",
            types = setOf(Monster.Type.OOZE),
            size = Creature.Size.LARGE,
            alignment = Creature.Alignment.NEUTRAL,
            challengeRating = 2f,
            hitDice = "8d10+40",
            abilities = Abilities(
                strength = AbilityScore(14),
                dexterity = AbilityScore(3),
                constitution = AbilityScore(20),
                intelligence = AbilityScore(1),
                wisdom = AbilityScore(6),
                charisma = AbilityScore(1),
            ),
            armorClass = 6,
            maxHitPoints = 84,
            speeds = Speeds(walk = 15),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Gelatinous Cube",
                    description = "A nearly transparent ooze that fills dungeon corridors.",
                    senses = "Blindsight 60 ft.",
                    languages = emptyList(),
                ),
            ),
        )

        fun celestialFiend() = Monster(
            id = "7",
            source = "srd_5.1",
            types = setOf(Monster.Type.CELESTIAL, Monster.Type.FIEND),
            size = Creature.Size.MEDIUM,
            alignment = Creature.Alignment.CHAOTIC_EVIL,
            challengeRating = 4f,
            hitDice = "9d8",
            abilities = Abilities(
                strength = AbilityScore(16),
                dexterity = AbilityScore(12),
                constitution = AbilityScore(13),
                intelligence = AbilityScore(14),
                wisdom = AbilityScore(11),
                charisma = AbilityScore(10),
            ),
            armorClass = 13,
            maxHitPoints = 40,
            speeds = Speeds(walk = 30),
            translations = mapOf(
                "en" to Monster.Translation(
                    name = "Celestial Fiend",
                    description = "A creature of dual nature.",
                    senses = "Darkvision 60 ft.",
                    languages = listOf("Common", "Celestial", "Infernal"),
                ),
            ),
        )
    }
}
