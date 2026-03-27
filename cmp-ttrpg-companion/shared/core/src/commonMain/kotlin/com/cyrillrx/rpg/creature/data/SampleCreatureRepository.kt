package com.cyrillrx.rpg.creature.data

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.BaseCreature
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.CreatureFilter
import com.cyrillrx.rpg.creature.domain.CreatureRepository

class SampleCreatureRepository : CreatureRepository {
    override suspend fun getAll(filter: CreatureFilter?): List<Creature> {
        filter ?: return creatures
        return creatures.filter(filter::matches)
    }

    override suspend fun getById(id: String): Creature? = creatures.firstOrNull { it.id == id }

    companion object {
        private val creatures: List<Creature> = listOf(
            goblin(),
            youngRedDragon(),
            skeleton(),
            direWolf(),
            balor(),
            gelatinousCube(),
        )

        fun getAll(): List<Creature> = creatures

        fun getFirst(): Creature = creatures.first()

        fun goblin() = Creature(
            id = "1",
            name = "Goblin",
            description = "A small, black-hearted creature that lairs in despoiled dungeons and other dismal settings.",
            type = Creature.Type.HUMANOID,
            subtype = "goblinoid",
            size = BaseCreature.Size.SMALL,
            alignment = BaseCreature.Alignment.NEUTRAL_EVIL,
            challengeRating = 0.25f,
            abilities = Abilities(
                strValue = 8,
                dexValue = 14,
                conValue = 10,
                intValue = 10,
                wisValue = 8,
                chaValue = 8,
            ),
            armorClass = 15,
            maxHitPoints = 7,
            speed = "30 ft.",
            languages = listOf("Common", "Goblin"),
        )

        fun youngRedDragon() = Creature(
            id = "2",
            name = "Young Red Dragon",
            description = "A fierce dragon that breathes fire and terrorizes the countryside.",
            type = Creature.Type.DRAGON,
            subtype = "",
            size = BaseCreature.Size.LARGE,
            alignment = BaseCreature.Alignment.CHAOTIC_EVIL,
            challengeRating = 10f,
            abilities = Abilities(
                strValue = 23,
                dexValue = 10,
                conValue = 21,
                intValue = 14,
                wisValue = 11,
                chaValue = 19,
            ),
            armorClass = 18,
            maxHitPoints = 178,
            speed = "40 ft., climb 40 ft., fly 80 ft.",
            languages = listOf("Common", "Draconic"),
        )

        private fun skeleton() = Creature(
            id = "3",
            name = "Skeleton",
            description = "An animated pile of bones held together by dark magic.",
            type = Creature.Type.UNDEAD,
            subtype = "",
            size = BaseCreature.Size.MEDIUM,
            alignment = BaseCreature.Alignment.LAWFUL_EVIL,
            challengeRating = 0.25f,
            abilities = Abilities(
                strValue = 10,
                dexValue = 14,
                conValue = 15,
                intValue = 6,
                wisValue = 8,
                chaValue = 5,
            ),
            armorClass = 13,
            maxHitPoints = 13,
            speed = "30 ft.",
            languages = listOf("understands languages it knew in life"),
        )

        private fun direWolf() = Creature(
            id = "4",
            name = "Dire Wolf",
            description = "A large and fearsome wolf that hunts in packs.",
            type = Creature.Type.BEAST,
            subtype = "",
            size = BaseCreature.Size.LARGE,
            alignment = BaseCreature.Alignment.NEUTRAL,
            challengeRating = 1f,
            abilities = Abilities(
                strValue = 17,
                dexValue = 15,
                conValue = 15,
                intValue = 3,
                wisValue = 12,
                chaValue = 7,
            ),
            armorClass = 14,
            maxHitPoints = 37,
            speed = "50 ft.",
            languages = emptyList(),
        )

        private fun balor() = Creature(
            id = "5",
            name = "Balor",
            description = "A towering fiend wreathed in flame, wielding a whip and longsword of fire.",
            type = Creature.Type.FIEND,
            subtype = "demon",
            size = BaseCreature.Size.HUGE,
            alignment = BaseCreature.Alignment.CHAOTIC_EVIL,
            challengeRating = 19f,
            abilities = Abilities(
                strValue = 26,
                dexValue = 15,
                conValue = 22,
                intValue = 20,
                wisValue = 16,
                chaValue = 22,
            ),
            armorClass = 19,
            maxHitPoints = 262,
            speed = "40 ft., fly 80 ft.",
            languages = listOf("Abyssal", "telepathy 120 ft."),
        )

        private fun gelatinousCube() = Creature(
            id = "6",
            name = "Gelatinous Cube",
            description = "A nearly transparent ooze that fills dungeon corridors.",
            type = Creature.Type.OOZE,
            subtype = "",
            size = BaseCreature.Size.LARGE,
            alignment = BaseCreature.Alignment.NEUTRAL,
            challengeRating = 2f,
            abilities = Abilities(strValue = 14, dexValue = 3, conValue = 20, intValue = 1, wisValue = 6, chaValue = 1),
            armorClass = 6,
            maxHitPoints = 84,
            speed = "15 ft.",
            languages = emptyList(),
        )
    }
}
