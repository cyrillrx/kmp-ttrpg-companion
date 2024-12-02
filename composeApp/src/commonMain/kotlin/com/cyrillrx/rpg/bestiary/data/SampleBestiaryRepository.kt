package com.cyrillrx.rpg.bestiary.data;

import com.cyrillrx.rpg.models.bestiary.Abilities
import com.cyrillrx.rpg.models.bestiary.Creature

class SampleBestiaryRepository {
    fun getAll(): List<Creature> {
        val item = get()
        return listOf(item, item, item)
    }

    fun get(): Creature = Creature(
        name = "Goblin",
        description = "A small, black-hearted creature that lairs in despoiled dungeons and other dismal settings.",
        type = "Humanoid",
        subtype = "goblinoid",
        size = "Small",
        alignment = "Neutral Evil",
        abilities = Abilities(
            strValue = 8,
            dexValue = 14,
            conValue = 10,
            intValue = 10,
            wisValue = 8,
            chaValue = 8,
        ),
    )
}
