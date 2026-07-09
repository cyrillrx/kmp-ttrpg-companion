package com.cyrillrx.rpg.spell.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class ComponentFilterStateTest {

    @Test
    fun `next cycles none to required to excluded and back`() {
        assertEquals(ComponentFilterState.REQUIRED, ComponentFilterState.NONE.next())
        assertEquals(ComponentFilterState.EXCLUDED, ComponentFilterState.REQUIRED.next())
        assertEquals(ComponentFilterState.NONE, ComponentFilterState.EXCLUDED.next())
    }
}
