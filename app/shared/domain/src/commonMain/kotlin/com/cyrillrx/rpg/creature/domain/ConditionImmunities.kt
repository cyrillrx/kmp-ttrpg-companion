package com.cyrillrx.rpg.creature.domain

data class ConditionImmunities(
    val blinded: Boolean = false,
    val charmed: Boolean = false,
    val deafened: Boolean = false,
    val exhaustion: Boolean = false,
    val frightened: Boolean = false,
    val grappled: Boolean = false,
    val incapacitated: Boolean = false,
    val paralyzed: Boolean = false,
    val petrified: Boolean = false,
    val poisoned: Boolean = false,
    val prone: Boolean = false,
    val restrained: Boolean = false,
    val stunned: Boolean = false,
    val unconscious: Boolean = false,
)

/**
 * Enumerates every condition carried by [ConditionImmunities]. Each entry knows how to read its
 * own immunity flag, so callers can iterate over [entries] instead of hand-listing accessors.
 * The [isImmune] reference is a plain function type (not a `KProperty1`), so it compiles to a
 * direct getter call with no reflection.
 */
enum class Condition(val isImmune: (ConditionImmunities) -> Boolean) {
    BLINDED(ConditionImmunities::blinded),
    CHARMED(ConditionImmunities::charmed),
    DEAFENED(ConditionImmunities::deafened),
    EXHAUSTION(ConditionImmunities::exhaustion),
    FRIGHTENED(ConditionImmunities::frightened),
    GRAPPLED(ConditionImmunities::grappled),
    INCAPACITATED(ConditionImmunities::incapacitated),
    PARALYZED(ConditionImmunities::paralyzed),
    PETRIFIED(ConditionImmunities::petrified),
    POISONED(ConditionImmunities::poisoned),
    PRONE(ConditionImmunities::prone),
    RESTRAINED(ConditionImmunities::restrained),
    STUNNED(ConditionImmunities::stunned),
    UNCONSCIOUS(ConditionImmunities::unconscious),
}
