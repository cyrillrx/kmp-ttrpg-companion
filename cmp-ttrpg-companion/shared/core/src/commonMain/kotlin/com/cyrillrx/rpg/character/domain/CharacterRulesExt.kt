package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.MAX_HIT_POINTS
import com.cyrillrx.rpg.creature.domain.Speeds
import com.cyrillrx.rpg.creature.domain.coerceToValidCreatureSpeeds
import com.cyrillrx.rpg.dnd.domain.DND_FEET_STEP
import com.cyrillrx.rpg.dnd.domain.coerceToNearestFootStep

const val MIN_CHARACTER_LEVEL = 1
const val MAX_CHARACTER_LEVEL = 20
const val MIN_ABILITY_SCORE = 1
const val MAX_ABILITY_SCORE = 30
const val MIN_HIT_POINTS = 0

private const val WALK_SPEED_SLOW_FT = 25
private const val WALK_SPEED_STANDARD_FT = 30

const val MIN_WALK_SPEED_FT = 25
const val MAX_WALK_SPEED_FT = 120

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
fun isValidWalkSpeedInFeet(value: Int): Boolean =
    value in MIN_WALK_SPEED_FT..MAX_WALK_SPEED_FT && value % DND_FEET_STEP == 0

fun Int.coerceToValidCharacterLevel(): Int = coerceIn(MIN_CHARACTER_LEVEL, MAX_CHARACTER_LEVEL)
fun Int.coerceToValidAbilityScore(): Int = coerceIn(MIN_ABILITY_SCORE, MAX_ABILITY_SCORE)
fun Int.coerceToValidHitPointAmount(): Int = coerceIn(MIN_HIT_POINTS, MAX_HIT_POINTS)

// coerceIn throws when the upper bound is below the lower one, and the maximum comes from stored
// data no validation guards: a corrupt sheet would crash the editor instead of reading as empty.
fun Int.coerceToValidCurrentHitPoints(maxHitPoints: Int): Int =
    coerceIn(MIN_HIT_POINTS, maxHitPoints.coerceAtLeast(MIN_HIT_POINTS))

fun Int.coerceToValidWalkSpeedInFeet(): Int = coerceToNearestFootStep(MIN_WALK_SPEED_FT, MAX_WALK_SPEED_FT)

// The walk speed is held to the narrower character range; the other modes fall back to the creature
// bounds, the rules defining no character-specific ceiling for flying or swimming.
fun Speeds.coerceToValidCharacterSpeeds(): Speeds =
    coerceToValidCreatureSpeeds().copy(walk = walk.coerceToValidWalkSpeedInFeet())
