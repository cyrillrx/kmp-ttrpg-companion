package com.cyrillrx.rpg.creature.domain

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DamageAndConditionMappingTest {

    @Test
    fun `each damage type reads its own affinity field and no other`() {
        // Each pair sets exactly one field so the entry under test must report it while every
        // other entry stays NONE. Guards against a copy-paste mistake in the enum wiring.
        val cases = listOf(
            DamageType.ACID to DamageAffinities(acid = DamageAffinity.RESISTANT),
            DamageType.BLUDGEONING to DamageAffinities(bludgeoning = DamageAffinity.RESISTANT),
            DamageType.COLD to DamageAffinities(cold = DamageAffinity.RESISTANT),
            DamageType.FIRE to DamageAffinities(fire = DamageAffinity.RESISTANT),
            DamageType.FORCE to DamageAffinities(force = DamageAffinity.RESISTANT),
            DamageType.LIGHTNING to DamageAffinities(lightning = DamageAffinity.RESISTANT),
            DamageType.NECROTIC to DamageAffinities(necrotic = DamageAffinity.RESISTANT),
            DamageType.PIERCING to DamageAffinities(piercing = DamageAffinity.RESISTANT),
            DamageType.POISON to DamageAffinities(poison = DamageAffinity.RESISTANT),
            DamageType.PSYCHIC to DamageAffinities(psychic = DamageAffinity.RESISTANT),
            DamageType.RADIANT to DamageAffinities(radiant = DamageAffinity.RESISTANT),
            DamageType.SLASHING to DamageAffinities(slashing = DamageAffinity.RESISTANT),
            DamageType.THUNDER to DamageAffinities(thunder = DamageAffinity.RESISTANT),
            DamageType.BLUDGEONING_NONMAGICAL to DamageAffinities(bludgeoningNonMagical = DamageAffinity.RESISTANT),
            DamageType.PIERCING_NONMAGICAL to DamageAffinities(piercingNonMagical = DamageAffinity.RESISTANT),
            DamageType.SLASHING_NONMAGICAL to DamageAffinities(slashingNonMagical = DamageAffinity.RESISTANT),
        )
        assertEquals(DamageType.entries.size, cases.size, "Every DamageType entry must be covered")

        cases.forEach { (type, affinities) ->
            assertEquals(DamageAffinity.RESISTANT, type.select(affinities), "$type should read its own field")
            DamageType.entries.filter { it != type }.forEach { other ->
                assertEquals(DamageAffinity.NONE, other.select(affinities), "$other must not read $type's field")
            }
        }
    }

    @Test
    fun `each condition reads its own immunity flag and no other`() {
        val cases = listOf(
            Condition.BLINDED to ConditionImmunities(blinded = true),
            Condition.CHARMED to ConditionImmunities(charmed = true),
            Condition.DEAFENED to ConditionImmunities(deafened = true),
            Condition.EXHAUSTION to ConditionImmunities(exhaustion = true),
            Condition.FRIGHTENED to ConditionImmunities(frightened = true),
            Condition.GRAPPLED to ConditionImmunities(grappled = true),
            Condition.INCAPACITATED to ConditionImmunities(incapacitated = true),
            Condition.PARALYZED to ConditionImmunities(paralyzed = true),
            Condition.PETRIFIED to ConditionImmunities(petrified = true),
            Condition.POISONED to ConditionImmunities(poisoned = true),
            Condition.PRONE to ConditionImmunities(prone = true),
            Condition.RESTRAINED to ConditionImmunities(restrained = true),
            Condition.STUNNED to ConditionImmunities(stunned = true),
            Condition.UNCONSCIOUS to ConditionImmunities(unconscious = true),
        )
        assertEquals(Condition.entries.size, cases.size, "Every Condition entry must be covered")

        cases.forEach { (condition, immunities) ->
            assertTrue(condition.isImmune(immunities), "$condition should read its own flag")
            Condition.entries.filter { it != condition }.forEach { other ->
                assertFalse(other.isImmune(immunities), "$other must not read $condition's flag")
            }
        }
    }
}
