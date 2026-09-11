package com.cyrillrx.rpg.character.domain

val HitPoints.isDown: Boolean get() = current <= MIN_HIT_POINTS

/** Temporary hit points absorb first; whatever is left erodes the current pool, floored at zero. */
fun HitPoints.withDamageTaken(amount: Int): HitPoints {
    val dealt = amount.coerceToValidHitPointAmount()
    val absorbed = minOf(temporary, dealt)
    return copy(
        current = (current - (dealt - absorbed)).coerceToValidCurrentHitPoints(max),
        temporary = temporary - absorbed,
    )
}

fun HitPoints.withHealingApplied(amount: Int): HitPoints =
    copy(current = (current + amount.coerceToValidHitPointAmount()).coerceToValidCurrentHitPoints(max))

/** Temporary hit points do not stack: the new amount replaces whatever was there. */
fun HitPoints.withTemporaryHitPointsSet(amount: Int): HitPoints =
    copy(temporary = amount.coerceToValidHitPointAmount())

/** Lowering the maximum caps the current pool; raising it heals nothing. */
fun HitPoints.withMaxHitPointsSet(value: Int): HitPoints {
    val coerced = value.coerceToValidMaxHitPoints()
    return copy(max = coerced, current = current.coerceToValidCurrentHitPoints(coerced))
}

/** The single entry point every caller goes through, so a preview can never diverge from what is applied. */
fun HitPoints.applying(adjustment: HitPointAdjustment, amount: Int): HitPoints = when (adjustment) {
    HitPointAdjustment.DAMAGE -> withDamageTaken(amount)
    HitPointAdjustment.HEALING -> withHealingApplied(amount)
    HitPointAdjustment.TEMPORARY -> withTemporaryHitPointsSet(amount)
}

fun Character.withHitPointsAdjusted(adjustment: HitPointAdjustment, amount: Int): Character =
    withHitPoints(hitPoints.applying(adjustment, amount))

fun Character.withMaxHitPoints(value: Int): Character = withHitPoints(hitPoints.withMaxHitPointsSet(value))
