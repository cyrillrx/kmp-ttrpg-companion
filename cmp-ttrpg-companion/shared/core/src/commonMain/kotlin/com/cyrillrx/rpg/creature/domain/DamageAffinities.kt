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
