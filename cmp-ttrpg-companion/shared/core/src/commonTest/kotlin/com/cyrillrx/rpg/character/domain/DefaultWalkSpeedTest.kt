package com.cyrillrx.rpg.character.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class DefaultWalkSpeedTest {

    @Test
    fun `standard races have 30ft walk speed`() {
        assertEquals(30, Race.HUMAN.defaultWalkSpeed())
        assertEquals(30, Race.ELF.defaultWalkSpeed())
        assertEquals(30, Race.HALF_ELF.defaultWalkSpeed())
        assertEquals(30, Race.HALF_ORC.defaultWalkSpeed())
        assertEquals(30, Race.DRAGONBORN.defaultWalkSpeed())
        assertEquals(30, Race.TIEFLING.defaultWalkSpeed())
    }

    @Test
    fun `small races have 25ft walk speed`() {
        assertEquals(25, Race.DWARF.defaultWalkSpeed())
        assertEquals(25, Race.GNOME.defaultWalkSpeed())
        assertEquals(25, Race.HALFLING.defaultWalkSpeed())
    }
}
