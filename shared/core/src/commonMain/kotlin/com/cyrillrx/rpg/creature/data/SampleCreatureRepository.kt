package com.cyrillrx.rpg.creature.data;

import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.BaseCreature
import com.cyrillrx.rpg.creature.domain.Creature

class SampleCreatureRepository {
    fun getAll(): List<Creature> {
        val item = get()
        return listOf(item, item, item)
    }

    fun get(): Creature = Creature(
        id = "1",
        name = "Goblin",
        description = "A small, black-hearted creature that lairs in despoiled dungeons and other dismal settings.",
        type = Creature.Type.HUMANOID,
        subtype = "goblinoid",
        size = BaseCreature.Size.SMALL,
        alignment = BaseCreature.Alignment.LAWFUL_EVIL,
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
        speed = "",
        languages = emptyList(),
    )
}
