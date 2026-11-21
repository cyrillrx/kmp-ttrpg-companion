package com.cyrillrx.rpg.core.presentation.format

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
import com.cyrillrx.rpg.core.presentation.theme.MonsterTypeColors
import com.cyrillrx.rpg.creature.domain.Monster
import kotlin.test.Test
import kotlin.test.assertEquals

class MonsterFormatTest {

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
    fun `getColor maps every type to its accent color`() {
        assertEquals(expected = MonsterTypeColors.aberration, actual = Monster.Type.ABERRATION.getColor())
        assertEquals(expected = MonsterTypeColors.beast, actual = Monster.Type.BEAST.getColor())
        assertEquals(expected = MonsterTypeColors.celestial, actual = Monster.Type.CELESTIAL.getColor())
        assertEquals(expected = MonsterTypeColors.construct, actual = Monster.Type.CONSTRUCT.getColor())
        assertEquals(expected = MonsterTypeColors.dragon, actual = Monster.Type.DRAGON.getColor())
        assertEquals(expected = MonsterTypeColors.elemental, actual = Monster.Type.ELEMENTAL.getColor())
        assertEquals(expected = MonsterTypeColors.fey, actual = Monster.Type.FEY.getColor())
        assertEquals(expected = MonsterTypeColors.fiend, actual = Monster.Type.FIEND.getColor())
        assertEquals(expected = MonsterTypeColors.giant, actual = Monster.Type.GIANT.getColor())
        assertEquals(expected = MonsterTypeColors.humanoid, actual = Monster.Type.HUMANOID.getColor())
        assertEquals(expected = MonsterTypeColors.monstrosity, actual = Monster.Type.MONSTROSITY.getColor())
        assertEquals(expected = MonsterTypeColors.ooze, actual = Monster.Type.OOZE.getColor())
        assertEquals(expected = MonsterTypeColors.plant, actual = Monster.Type.PLANT.getColor())
        assertEquals(expected = MonsterTypeColors.swarm, actual = Monster.Type.SWARM.getColor())
        assertEquals(expected = MonsterTypeColors.undead, actual = Monster.Type.UNDEAD.getColor())
        assertEquals(expected = MonsterTypeColors.unknown, actual = Monster.Type.UNKNOWN.getColor())
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
