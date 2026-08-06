package com.cyrillrx.rpg.usercollection.presentation

import com.cyrillrx.rpg.spell.domain.Spell
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_collection
import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CollectionDetailStateTest {

    @Test
    fun `a collection is loaded once its items are known, empty or not`() {
        assertTrue(stateWith(CollectionDetailState.Body.Empty).isLoaded)
        assertTrue(stateWith(CollectionDetailState.Body.WithData(emptyList<Spell>())).isLoaded)
    }

    @Test
    fun `a collection is not loaded while loading or on error`() {
        assertFalse(stateWith(CollectionDetailState.Body.Loading).isLoaded)
        assertFalse(
            stateWith(CollectionDetailState.Body.Error(Res.string.error_while_loading_collection)).isLoaded,
        )
    }

    private fun stateWith(body: CollectionDetailState.Body<Spell>) = CollectionDetailState(body = body)
}
