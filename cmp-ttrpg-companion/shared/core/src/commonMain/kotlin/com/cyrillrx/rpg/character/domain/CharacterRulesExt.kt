package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.dnd.domain.DND_FEET_STEP
import com.cyrillrx.rpg.dnd.domain.DND_METERS_STEP
import kotlin.math.roundToInt

const val MIN_CHARACTER_LEVEL = 1
const val MAX_CHARACTER_LEVEL = 20
const val MIN_ABILITY_SCORE = 1
const val MAX_ABILITY_SCORE = 30
const val MIN_ARMOR_CLASS = 0
const val MAX_ARMOR_CLASS = 30

private const val WALK_SPEED_SLOW_FT = 25
private const val WALK_SPEED_STANDARD_FT = 30

private const val MIN_WALK_SPEED_FT = 25
private const val MAX_WALK_SPEED_FT = 120
private const val MIN_WALK_SPEED_M = 7.5f
private const val MAX_WALK_SPEED_M = 36f

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

fun isValidCharacterLevel(value: Int): Boolean = value in MIN_CHARACTER_LEVEL..MAX_CHARACTER_LEVEL
fun isValidAbilityScore(value: Int): Boolean = value in MIN_ABILITY_SCORE..MAX_ABILITY_SCORE
fun isValidArmorClass(value: Int): Boolean = value in MIN_ARMOR_CLASS..MAX_ARMOR_CLASS
fun isValidMaxHitPoints(value: Int): Boolean = value >= 1
fun isValidWalkSpeedInFeet(value: Int): Boolean =
    value in MIN_WALK_SPEED_FT..MAX_WALK_SPEED_FT && value % DND_FEET_STEP == 0

fun isValidWalkSpeedInMeters(value: Float): Boolean =
    value in MIN_WALK_SPEED_M..MAX_WALK_SPEED_M && value % DND_METERS_STEP == 0f

fun Int.coerceToValidCharacterLevel(): Int = coerceIn(MIN_CHARACTER_LEVEL, MAX_CHARACTER_LEVEL)
fun Int.coerceToValidAbilityScore(): Int = coerceIn(MIN_ABILITY_SCORE, MAX_ABILITY_SCORE)
fun Int.coerceToValidArmorClass(): Int = coerceIn(MIN_ARMOR_CLASS, MAX_ARMOR_CLASS)
fun Int.coerceToValidMaxHitPoints(): Int = coerceAtLeast(1)

private fun Float.coerceToNearestStep(step: Float, min: Float, max: Float): Float {
    val rounded = (this / step).roundToInt() * step
    return rounded.coerceIn(min, max)
}

fun Int.coerceToValidWalkSpeedInFeet(): Int =
    toFloat().coerceToNearestStep(
        step = DND_FEET_STEP.toFloat(),
        min = MIN_WALK_SPEED_FT.toFloat(),
        max = MAX_WALK_SPEED_FT.toFloat(),
    ).roundToInt()

fun Float.coerceToValidWalkSpeedInMeters(): Float =
    coerceToNearestStep(step = DND_METERS_STEP, min = MIN_WALK_SPEED_M, max = MAX_WALK_SPEED_M)
