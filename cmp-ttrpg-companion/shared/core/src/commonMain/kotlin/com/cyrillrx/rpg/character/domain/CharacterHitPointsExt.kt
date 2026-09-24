package com.cyrillrx.rpg.character.domain

import com.cyrillrx.rpg.creature.domain.coerceToValidMaxHitPoints

val HitPoints.isDown: Boolean get() = current <= MIN_HIT_POINTS

fun HitPoints.isLethalDamage(amount: Int): Boolean =
    amount.coerceAtLeast(MIN_HIT_POINTS) - temporary - current >= max

/** Temporary hit points absorb first; whatever is left erodes the current pool, floored at zero. */
fun HitPoints.takeDamage(amount: Int): HitPoints {
    val dealt = amount.coerceToValidHitPointAmount()
    val absorbed = minOf(temporary, dealt)
    return copy(
        current = (current - (dealt - absorbed)).coerceToValidCurrentHitPoints(max),
        temporary = temporary - absorbed,
    )
}

fun HitPoints.heal(amount: Int): HitPoints =
    copy(current = (current + amount.coerceToValidHitPointAmount()).coerceToValidCurrentHitPoints(max))

fun HitPoints.setTemporaryHitPoints(amount: Int): HitPoints =
    copy(temporary = amount.coerceToValidHitPointAmount())

fun HitPoints.setMaxHitPoints(value: Int): HitPoints {
    val coerced = value.coerceToValidMaxHitPoints()
    return copy(max = coerced, current = current.coerceToValidCurrentHitPoints(coerced))
}

fun HitPoints.adjust(adjustmentType: HitPointAdjustment, amount: Int): HitPoints = when (adjustmentType) {
    HitPointAdjustment.DAMAGE -> takeDamage(amount)
    HitPointAdjustment.HEALING -> heal(amount)
    HitPointAdjustment.TEMPORARY -> setTemporaryHitPoints(amount)
    HitPointAdjustment.MAXIMUM -> setMaxHitPoints(amount)
}

fun Character.adjustHitPoints(adjustmentType: HitPointAdjustment, amount: Int): Character =
    withHitPoints(hitPoints.adjust(adjustmentType, amount))
