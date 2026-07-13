package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterProficiencyBonusTest {

    @Test
    fun `low challenge rating yields a proficiency bonus of 2`() {
        // Goblin is CR 1/4.
        assertEquals(2, SampleMonsterRepository.goblin().proficiencyBonus())
    }

    @Test
    fun `mid challenge rating yields a proficiency bonus of 4`() {
        // Young red dragon is CR 10.
        assertEquals(4, SampleMonsterRepository.youngRedDragon().proficiencyBonus())
    }

    @Test
    fun `high challenge rating yields a proficiency bonus of 6`() {
        // Balor is CR 19.
        assertEquals(6, SampleMonsterRepository.balor().proficiencyBonus())
    }
}
