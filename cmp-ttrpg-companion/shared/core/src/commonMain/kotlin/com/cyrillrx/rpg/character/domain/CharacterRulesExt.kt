package com.cyrillrx.rpg.character.domain

import kotlin.math.roundToInt

private const val WALK_SPEED_SLOW_FT = 25
private const val WALK_SPEED_STANDARD_FT = 30

private const val WALK_SPEED_MIN_FT = 25
private const val WALK_SPEED_MAX_FT = 120
private const val WALK_SPEED_MIN_M = 7.5f
private const val WALK_SPEED_MAX_M = 36f
private const val WALK_SPEED_STEP_FT = 5
private const val WALK_SPEED_STEP_M = 1.5f

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
fun isValidWalkSpeedInFeet(value: Int): Boolean =
    value in WALK_SPEED_MIN_FT..WALK_SPEED_MAX_FT && value % WALK_SPEED_STEP_FT == 0

fun isValidWalkSpeedInMeters(value: Float): Boolean =
    value in WALK_SPEED_MIN_M..WALK_SPEED_MAX_M && value % WALK_SPEED_STEP_M == 0f

fun Int.coerceToValidCharacterLevel(): Int = coerceIn(1, 20)
fun Int.coerceToValidAbilityScore(): Int = coerceIn(1, 30)
fun Int.coerceToValidArmorClass(): Int = coerceIn(0, 30)
fun Int.coerceToValidMaxHitPoints(): Int = coerceAtLeast(1)

private fun Float.coerceToNearestStep(step: Float, min: Float, max: Float): Float {
    val rounded = (this / step).roundToInt() * step
    return rounded.coerceIn(min, max)
}

fun Int.coerceToValidWalkSpeedInFeet(): Int =
    toFloat().coerceToNearestStep(
        step = WALK_SPEED_STEP_FT.toFloat(),
        min = WALK_SPEED_MIN_FT.toFloat(),
        max = WALK_SPEED_MAX_FT.toFloat(),
    ).roundToInt()

fun Float.coerceToValidWalkSpeedInMeters(): Float =
    coerceToNearestStep(step = WALK_SPEED_STEP_M, min = WALK_SPEED_MIN_M, max = WALK_SPEED_MAX_M)

fun Int.coerceToValidWalkSpeedInMeters(): Float = toFloat().coerceToValidWalkSpeedInMeters()
