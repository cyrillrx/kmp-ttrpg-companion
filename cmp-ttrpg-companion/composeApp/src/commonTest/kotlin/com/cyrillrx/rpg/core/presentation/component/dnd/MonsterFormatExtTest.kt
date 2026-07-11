package com.cyrillrx.rpg.core.presentation.component.dnd

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.BugReport
import androidx.compose.material.icons.filled.Dangerous
import androidx.compose.material.icons.filled.Forest
import androidx.compose.material.icons.filled.Grain
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.SmartToy
import androidx.compose.material.icons.filled.Water
import androidx.compose.material.icons.filled.Whatshot
import com.cyrillrx.rpg.core.presentation.theme.MonsterAberration
import com.cyrillrx.rpg.core.presentation.theme.MonsterBeast
import com.cyrillrx.rpg.core.presentation.theme.MonsterCelestial
import com.cyrillrx.rpg.core.presentation.theme.MonsterConstruct
import com.cyrillrx.rpg.core.presentation.theme.MonsterDragon
import com.cyrillrx.rpg.core.presentation.theme.MonsterElemental
import com.cyrillrx.rpg.core.presentation.theme.MonsterFey
import com.cyrillrx.rpg.core.presentation.theme.MonsterFiend
import com.cyrillrx.rpg.core.presentation.theme.MonsterGiant
import com.cyrillrx.rpg.core.presentation.theme.MonsterHumanoid
import com.cyrillrx.rpg.core.presentation.theme.MonsterMonstrosity
import com.cyrillrx.rpg.core.presentation.theme.MonsterOoze
import com.cyrillrx.rpg.core.presentation.theme.MonsterPlant
import com.cyrillrx.rpg.core.presentation.theme.MonsterSwarm
import com.cyrillrx.rpg.core.presentation.theme.MonsterUndead
import com.cyrillrx.rpg.core.presentation.theme.MonsterUnknown
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterFormatExtTest {

    @Test
    fun `formatCRValue returns 0 for zero`() {
        assertEquals(expected = "0", actual = formatCRValue(0f))
    }

    @Test
    fun `formatCRValue returns 1-8 for 0 125`() {
        assertEquals(expected = "1/8", actual = formatCRValue(0.125f))
    }

    @Test
    fun `formatCRValue returns 1-4 for 0 25`() {
        assertEquals(expected = "1/4", actual = formatCRValue(0.25f))
    }

    @Test
    fun `formatCRValue returns 1-2 for 0 5`() {
        assertEquals(expected = "1/2", actual = formatCRValue(0.5f))
    }

    @Test
    fun `formatCRValue returns integer string for whole number`() {
        assertEquals(expected = "10", actual = formatCRValue(10f))
    }

    @Test
    fun `formatCRValue returns decimal string for non-standard float`() {
        assertEquals(expected = "1.5", actual = formatCRValue(1.5f))
    }

    @Test
    fun `toFormattedCR returns CR prefix with formatted value`() {
        val goblin = SampleMonsterRepository.goblin()
        assertEquals(expected = "CR 1/4", actual = goblin.toFormattedCR())
    }

    @Test
    fun `getColor maps every type to its accent color`() {
        assertEquals(expected = MonsterAberration, actual = Monster.Type.ABERRATION.getColor())
        assertEquals(expected = MonsterBeast, actual = Monster.Type.BEAST.getColor())
        assertEquals(expected = MonsterCelestial, actual = Monster.Type.CELESTIAL.getColor())
        assertEquals(expected = MonsterConstruct, actual = Monster.Type.CONSTRUCT.getColor())
        assertEquals(expected = MonsterDragon, actual = Monster.Type.DRAGON.getColor())
        assertEquals(expected = MonsterElemental, actual = Monster.Type.ELEMENTAL.getColor())
        assertEquals(expected = MonsterFey, actual = Monster.Type.FEY.getColor())
        assertEquals(expected = MonsterFiend, actual = Monster.Type.FIEND.getColor())
        assertEquals(expected = MonsterGiant, actual = Monster.Type.GIANT.getColor())
        assertEquals(expected = MonsterHumanoid, actual = Monster.Type.HUMANOID.getColor())
        assertEquals(expected = MonsterMonstrosity, actual = Monster.Type.MONSTROSITY.getColor())
        assertEquals(expected = MonsterOoze, actual = Monster.Type.OOZE.getColor())
        assertEquals(expected = MonsterPlant, actual = Monster.Type.PLANT.getColor())
        assertEquals(expected = MonsterSwarm, actual = Monster.Type.SWARM.getColor())
        assertEquals(expected = MonsterUndead, actual = Monster.Type.UNDEAD.getColor())
        assertEquals(expected = MonsterUnknown, actual = Monster.Type.UNKNOWN.getColor())
    }

    @Test
    fun `getColor assigns a distinct color to each type`() {
        val colors = Monster.Type.entries.map { it.getColor() }
        assertEquals(expected = Monster.Type.entries.size, actual = colors.toSet().size)
    }

    @Test
    fun `getIcon maps every type to its icon`() {
        assertEquals(expected = Icons.Filled.Psychology, actual = Monster.Type.ABERRATION.getIcon())
        assertEquals(expected = Icons.Filled.Pets, actual = Monster.Type.BEAST.getIcon())
        assertEquals(expected = Icons.Filled.Bolt, actual = Monster.Type.CELESTIAL.getIcon())
        assertEquals(expected = Icons.Filled.SmartToy, actual = Monster.Type.CONSTRUCT.getIcon())
        assertEquals(expected = Icons.Filled.Whatshot, actual = Monster.Type.DRAGON.getIcon())
        assertEquals(expected = Icons.Filled.AcUnit, actual = Monster.Type.ELEMENTAL.getIcon())
        assertEquals(expected = Icons.Filled.Forest, actual = Monster.Type.FEY.getIcon())
        assertEquals(expected = Icons.Filled.Dangerous, actual = Monster.Type.FIEND.getIcon())
        assertEquals(expected = Icons.Filled.Shield, actual = Monster.Type.GIANT.getIcon())
        assertEquals(expected = Icons.Filled.Groups, actual = Monster.Type.HUMANOID.getIcon())
        assertEquals(expected = Icons.Filled.BugReport, actual = Monster.Type.MONSTROSITY.getIcon())
        assertEquals(expected = Icons.Filled.Water, actual = Monster.Type.OOZE.getIcon())
        assertEquals(expected = Icons.Filled.Forest, actual = Monster.Type.PLANT.getIcon())
        assertEquals(expected = Icons.Filled.Grain, actual = Monster.Type.SWARM.getIcon())
        assertEquals(expected = Icons.Filled.Dangerous, actual = Monster.Type.UNDEAD.getIcon())
        assertEquals(expected = Icons.Filled.QuestionMark, actual = Monster.Type.UNKNOWN.getIcon())
    }
}
