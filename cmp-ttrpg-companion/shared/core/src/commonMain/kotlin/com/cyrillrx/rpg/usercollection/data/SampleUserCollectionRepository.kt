package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlin.time.Clock
import kotlin.time.Instant

class SampleUserCollectionRepository(clock: Clock = Clock.System) :
    UserCollectionRepository by RamUserCollectionRepository(samples, clock) {

    companion object {
        private val samples: List<Stored<UserCollection>> = listOf(
            combatSpells(),
            supportSpells(),
            gandalfSpells(),
        )

        fun getAll(): List<Stored<UserCollection>> = samples

        fun getFirst(): Stored<UserCollection> = samples.first()

        private fun combatSpells() = stored(
            UserCollection(
                id = "sample-spell-collection-1",
                name = "Combat Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-15T10:30:00Z",
        )

        private fun supportSpells() = stored(
            UserCollection(
                id = "sample-spell-collection-2",
                name = "Support Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Mage Armor", "Detect Thoughts"),
            ),
            updatedAt = "2024-01-10T08:00:00Z",
        )

        private fun gandalfSpells() = stored(
            UserCollection(
                id = "sample-spell-collection-3",
                name = "Gandalf's Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-20T14:00:00Z",
        )

        private fun stored(collection: UserCollection, updatedAt: String): Stored<UserCollection> =
            Stored(value = collection, updatedAt = Instant.parse(updatedAt))
    }
}
