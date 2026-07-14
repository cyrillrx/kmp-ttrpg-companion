package com.cyrillrx.rpg.creature.domain

enum class DamageAffinity { NONE, RESISTANT, IMMUNE, VULNERABLE }

data class DamageAffinities(
    val acid: DamageAffinity = DamageAffinity.NONE,
    val bludgeoning: DamageAffinity = DamageAffinity.NONE,
    val cold: DamageAffinity = DamageAffinity.NONE,
    val fire: DamageAffinity = DamageAffinity.NONE,
    val force: DamageAffinity = DamageAffinity.NONE,
    val lightning: DamageAffinity = DamageAffinity.NONE,
    val necrotic: DamageAffinity = DamageAffinity.NONE,
    val piercing: DamageAffinity = DamageAffinity.NONE,
    val poison: DamageAffinity = DamageAffinity.NONE,
    val psychic: DamageAffinity = DamageAffinity.NONE,
    val radiant: DamageAffinity = DamageAffinity.NONE,
    val slashing: DamageAffinity = DamageAffinity.NONE,
    val thunder: DamageAffinity = DamageAffinity.NONE,
    val bludgeoningNonMagical: DamageAffinity = DamageAffinity.NONE,
    val piercingNonMagical: DamageAffinity = DamageAffinity.NONE,
    val slashingNonMagical: DamageAffinity = DamageAffinity.NONE,
)

/**
 * Enumerates every damage type carried by [DamageAffinities]. Each entry knows how to read its
 * own affinity, so callers can iterate over [entries] instead of hand-listing accessors.
 * The [select] reference is a plain function type (not a `KProperty1`), so it compiles to a
 * direct getter call with no reflection.
 */
enum class DamageType(val select: (DamageAffinities) -> DamageAffinity) {
    ACID(DamageAffinities::acid),
    BLUDGEONING(DamageAffinities::bludgeoning),
    COLD(DamageAffinities::cold),
    FIRE(DamageAffinities::fire),
    FORCE(DamageAffinities::force),
    LIGHTNING(DamageAffinities::lightning),
    NECROTIC(DamageAffinities::necrotic),
    PIERCING(DamageAffinities::piercing),
    POISON(DamageAffinities::poison),
    PSYCHIC(DamageAffinities::psychic),
    RADIANT(DamageAffinities::radiant),
    SLASHING(DamageAffinities::slashing),
    THUNDER(DamageAffinities::thunder),
    BLUDGEONING_NONMAGICAL(DamageAffinities::bludgeoningNonMagical),
    PIERCING_NONMAGICAL(DamageAffinities::piercingNonMagical),
    SLASHING_NONMAGICAL(DamageAffinities::slashingNonMagical),
}
