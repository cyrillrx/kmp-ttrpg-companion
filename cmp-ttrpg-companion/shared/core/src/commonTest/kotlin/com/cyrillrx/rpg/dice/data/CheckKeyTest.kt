package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Skill
import com.cyrillrx.rpg.dice.domain.RollContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CheckKeyTest {

    @Test
    fun `every check reads back from its key`() {
        val checks = Ability.entries.map(D20Check::SavingThrow) +
            Skill.entries.map(D20Check::SkillCheck) +
            D20Check.Initiative

        assertEquals(checks, checks.map { it.toCheckKey().toD20Check() })
    }

    @Test
    fun `a key reads as the untranslated check name`() {
        assertEquals("skill:perception", D20Check.SkillCheck(Skill.PERCEPTION).toCheckKey())
        assertEquals("save:wisdom", D20Check.SavingThrow(Ability.WISDOM).toCheckKey())
        assertEquals("initiative", D20Check.Initiative.toCheckKey())
    }

    @Test
    fun `a key naming no known check reads as no check`() {
        assertNull("skill:juggling".toD20Check())
        assertNull("attack:longsword".toD20Check())
    }

    @Test
    fun `a stored context needs both its character and a known check`() {
        assertEquals(
            RollContext(characterId = "hero", check = D20Check.Initiative),
            storedRollContext(characterId = "hero", checkKey = "initiative"),
        )
        assertNull(storedRollContext(characterId = null, checkKey = "initiative"))
        assertNull(storedRollContext(characterId = "hero", checkKey = null))
        assertNull(storedRollContext(characterId = "hero", checkKey = "save:luck"))
    }
}
