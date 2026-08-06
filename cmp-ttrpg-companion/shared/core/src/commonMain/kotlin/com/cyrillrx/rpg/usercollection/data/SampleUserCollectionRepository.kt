package com.cyrillrx.rpg.usercollection.data

import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.usercollection.domain.UserCollection
import com.cyrillrx.rpg.usercollection.domain.UserCollectionRepository
import kotlin.time.Clock
import kotlin.time.Instant

class SampleUserCollectionRepository(
    private val clock: Clock = Clock.System,
) : UserCollectionRepository {

    override suspend fun getAll(type: UserCollection.Type): List<Stored<UserCollection>> =
        lists.values.filter { it.value.type == type }

    override suspend fun get(id: String): UserCollection? = lists[id]?.value

    override suspend fun save(list: UserCollection) {
        lists[list.id] = Stored(value = list, updatedAt = clock.now())
    }

    override suspend fun delete(id: String) {
        lists.remove(id)
    }

    companion object {
        private val samples: List<Stored<UserCollection>> = listOf(
            combatSpells(),
            supportSpells(),
            gandalfSpells(),
        )

        private val lists = mutableMapOf<String, Stored<UserCollection>>().apply {
            samples.forEach { put(it.value.id, it) }
        }

        fun getAll(): List<Stored<UserCollection>> = samples

        fun getFirst(): Stored<UserCollection> = samples.first()

        private fun combatSpells() = stored(
            UserCollection(
                id = "sample-spell-list-1",
                name = "Combat Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-15T10:30:00Z",
        )

        private fun supportSpells() = stored(
            UserCollection(
                id = "sample-spell-list-2",
                name = "Support Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Mage Armor", "Detect Thoughts"),
            ),
            updatedAt = "2024-01-10T08:00:00Z",
        )

        private fun gandalfSpells() = stored(
            UserCollection(
                id = "sample-spell-list-3",
                name = "Gandalf's Spells",
                type = UserCollection.Type.SPELL,
                itemIds = listOf("Fireball", "Thunderwave", "Counterspell"),
            ),
            updatedAt = "2024-01-20T14:00:00Z",
        )

        private fun stored(list: UserCollection, updatedAt: String): Stored<UserCollection> =
            Stored(value = list, updatedAt = Instant.parse(updatedAt))
    }
}
