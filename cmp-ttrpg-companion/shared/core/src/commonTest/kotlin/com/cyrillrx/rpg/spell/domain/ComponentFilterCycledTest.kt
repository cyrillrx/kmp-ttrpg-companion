package com.cyrillrx.rpg.spell.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentFilterCycledTest {

    @Test
    fun `cycling an absent component adds it as required`() {
        val result = emptyMap<Spell.ComponentType, ComponentFilterState>().cycled(Spell.ComponentType.VERBAL)
        assertEquals(mapOf(Spell.ComponentType.VERBAL to ComponentFilterState.REQUIRED), result)
    }

    @Test
    fun `cycling a required component moves it to excluded`() {
        val filter = mapOf(Spell.ComponentType.VERBAL to ComponentFilterState.REQUIRED)
        val result = filter.cycled(Spell.ComponentType.VERBAL)
        assertEquals(mapOf(Spell.ComponentType.VERBAL to ComponentFilterState.EXCLUDED), result)
    }

    @Test
    fun `cycling an excluded component drops it from the map`() {
        val filter = mapOf(Spell.ComponentType.VERBAL to ComponentFilterState.EXCLUDED)
        val result = filter.cycled(Spell.ComponentType.VERBAL)
        assertEquals(emptyMap(), result)
    }

    @Test
    fun `cycling one component leaves the others untouched`() {
        val filter = mapOf(
            Spell.ComponentType.VERBAL to ComponentFilterState.REQUIRED,
            Spell.ComponentType.SOMATIC to ComponentFilterState.EXCLUDED,
        )
        val result = filter.cycled(Spell.ComponentType.VERBAL)
        assertEquals(
            mapOf(
                Spell.ComponentType.VERBAL to ComponentFilterState.EXCLUDED,
                Spell.ComponentType.SOMATIC to ComponentFilterState.EXCLUDED,
            ),
            result,
        )
    }
}
