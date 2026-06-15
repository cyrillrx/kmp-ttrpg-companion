package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals

class SkillsApplySelectionTest {

    private val empty = Skills()
    private val withExpertAcrobatics = Skills(acrobatics = Proficiency.EXPERT)

    @Test
    fun `applySelection sets proficiency from map`() {
        val selected = mapOf(Skill.ACROBATICS to Proficiency.PROFICIENT)
        val result = empty.applySelection(selected)
        assertEquals(Proficiency.PROFICIENT, result.acrobatics)
    }

    @Test
    fun `applySelection sets NONE for skills absent from map`() {
        val selected = mapOf(Skill.ACROBATICS to Proficiency.PROFICIENT)
        val result = empty.applySelection(selected)
        assertEquals(Proficiency.NONE, result.athletics)
    }

    @Test
    fun `applySelection preserves EXPERT when skill remains in map`() {
        val selected = mapOf(Skill.ACROBATICS to Proficiency.EXPERT)
        val result = withExpertAcrobatics.applySelection(selected)
        assertEquals(Proficiency.EXPERT, result.acrobatics)
    }

    @Test
    fun `applySelection clears EXPERT when skill set to NONE in map`() {
        val selected = mapOf(Skill.ACROBATICS to Proficiency.NONE)
        val result = withExpertAcrobatics.applySelection(selected)
        assertEquals(Proficiency.NONE, result.acrobatics)
    }

    @Test
    fun `applySelection handles all 18 skills independently`() {
        val selected = Skill.entries.associateWith { skill ->
            if (skill == Skill.PERCEPTION) Proficiency.PROFICIENT else Proficiency.NONE
        }
        val result = empty.applySelection(selected)
        assertEquals(Proficiency.PROFICIENT, result.perception)
        assertEquals(Proficiency.NONE, result.acrobatics)
        assertEquals(Proficiency.NONE, result.survival)
    }
}
