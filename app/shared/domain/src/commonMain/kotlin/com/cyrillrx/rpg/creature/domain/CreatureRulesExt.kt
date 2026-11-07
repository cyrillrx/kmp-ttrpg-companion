package com.cyrillrx.rpg.creature.domain

import com.cyrillrx.rpg.dnd.domain.DND_FEET_STEP
import com.cyrillrx.rpg.dnd.domain.coerceToNearestFootStep

const val MIN_ARMOR_CLASS = 1
const val MAX_ARMOR_CLASS = 40
const val MIN_MAX_HIT_POINTS = 1
const val MAX_HIT_POINTS = 999

// Wider than a character's walk speed: the bestiary runs from a 5 ft crawl to a solar's 150 ft flight.
const val MIN_CREATURE_SPEED_FT = 0
const val MAX_CREATURE_SPEED_FT = 200

fun isValidArmorClass(value: Int): Boolean = value in MIN_ARMOR_CLASS..MAX_ARMOR_CLASS
fun isValidMaxHitPoints(value: Int): Boolean = value in MIN_MAX_HIT_POINTS..MAX_HIT_POINTS
fun isValidCreatureSpeedInFeet(value: Int): Boolean =
    value in MIN_CREATURE_SPEED_FT..MAX_CREATURE_SPEED_FT && value % DND_FEET_STEP == 0

fun Int.coerceToValidArmorClass(): Int = coerceIn(MIN_ARMOR_CLASS, MAX_ARMOR_CLASS)
fun Int.coerceToValidMaxHitPoints(): Int = coerceIn(MIN_MAX_HIT_POINTS, MAX_HIT_POINTS)
fun Int.coerceToValidCreatureSpeedInFeet(): Int =
    coerceToNearestFootStep(MIN_CREATURE_SPEED_FT, MAX_CREATURE_SPEED_FT)

fun Speeds.coerceToValidCreatureSpeeds(): Speeds = copy(
    walk = walk.coerceToValidCreatureSpeedInFeet(),
    fly = fly?.coerceToValidCreatureSpeedInFeet(),
    swim = swim?.coerceToValidCreatureSpeedInFeet(),
    climb = climb?.coerceToValidCreatureSpeedInFeet(),
    burrow = burrow?.coerceToValidCreatureSpeedInFeet(),
)
