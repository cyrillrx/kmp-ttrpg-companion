package com.cyrillrx.rpg.character.domain

private const val WALK_SPEED_SLOW_FT = 25
private const val WALK_SPEED_STANDARD_FT = 30
private const val WALK_SPEED_MAX_FT = 120

fun Race.defaultWalkSpeed(): Int = when (this) {
    Race.HUMAN -> WALK_SPEED_STANDARD_FT
    Race.ELF -> WALK_SPEED_STANDARD_FT
    Race.HALF_ELF -> WALK_SPEED_STANDARD_FT
    Race.HALF_ORC -> WALK_SPEED_STANDARD_FT
    Race.DRAGONBORN -> WALK_SPEED_STANDARD_FT
    Race.TIEFLING -> WALK_SPEED_STANDARD_FT
    Race.DWARF -> WALK_SPEED_SLOW_FT
    Race.GNOME -> WALK_SPEED_SLOW_FT
    Race.HALFLING -> WALK_SPEED_SLOW_FT
}

fun isValidCharacterLevel(value: Int): Boolean = value in 1..20
fun isValidAbilityScore(value: Int): Boolean = value in 1..30
fun isValidArmorClass(value: Int): Boolean = value in 0..30
fun isValidMaxHitPoints(value: Int): Boolean = value >= 1
fun isValidWalkSpeed(value: Int): Boolean = value in WALK_SPEED_SLOW_FT..WALK_SPEED_MAX_FT && value % 5 == 0

fun Int.coerceToValidCharacterLevel(): Int = coerceIn(1, 20)
fun Int.coerceToValidAbilityScore(): Int = coerceIn(1, 30)
fun Int.coerceToValidArmorClass(): Int = coerceIn(0, 30)
fun Int.coerceToValidMaxHitPoints(): Int = coerceAtLeast(1)
fun Int.coerceToValidWalkSpeed(): Int {
    val clamped = coerceIn(WALK_SPEED_SLOW_FT, WALK_SPEED_MAX_FT)
    val remainder = clamped % 5
    return if (remainder <= 2) clamped - remainder else clamped + (5 - remainder)
}
