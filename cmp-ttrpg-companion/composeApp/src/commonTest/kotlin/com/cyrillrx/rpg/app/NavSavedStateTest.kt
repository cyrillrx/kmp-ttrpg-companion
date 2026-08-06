package com.cyrillrx.rpg.app

import androidx.navigation3.runtime.NavKey
import com.cyrillrx.rpg.spell.presentation.navigation.SpellRoute
import com.cyrillrx.rpg.usercollection.presentation.navigation.UserCollectionRoute
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class NavSavedStateTest {

    @Test
    fun `a discriminator left over from a renamed route falls back to Home`() {
        val serializer = navSerializersModule.getPolymorphic(
            NavKey::class,
            "com.cyrillrx.rpg.userlist.presentation.navigation.UserListRoute.Spell",
        )

        assertNotNull(serializer, "an unknown discriminator must resolve, otherwise restoring throws")
        assertEquals(MainRoute.Home.serializer().descriptor, serializer.descriptor)
    }

    @Test
    fun `known routes still resolve to their own serializer`() {
        listOf(
            MainRoute.Home.serializer(),
            UserCollectionRoute.Spell.serializer(),
            SpellRoute.Compendium.serializer(),
        ).forEach { expected ->
            val serializer = navSerializersModule.getPolymorphic(NavKey::class, expected.descriptor.serialName)

            assertEquals(expected.descriptor, serializer?.descriptor)
        }
    }
}
