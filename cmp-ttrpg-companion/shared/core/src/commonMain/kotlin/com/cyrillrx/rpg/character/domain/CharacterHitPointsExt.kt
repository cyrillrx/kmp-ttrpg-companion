package com.cyrillrx.rpg.character.domain

val HitPoints.isDown: Boolean get() = current <= MIN_HIT_POINTS

fun HitPoints.isLethalDamage(amount: Int): Boolean =
    amount.coerceToValidHitPointAmount() - temporary - current >= max

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

/** Temporary hit points do not stack: the new amount replaces whatever was there. */
fun HitPoints.setTemporaryHitPoints(amount: Int): HitPoints =
    copy(temporary = amount.coerceToValidHitPointAmount())

/** Lowering the maximum caps the current pool; raising it heals nothing. */
fun HitPoints.setMaxHitPoints(value: Int): HitPoints {
    val coerced = value.coerceToValidMaxHitPoints()
    return copy(max = coerced, current = current.coerceToValidCurrentHitPoints(coerced))
}

/** The single entry point every caller goes through, so a preview can never diverge from what is applied. */
fun HitPoints.adjust(adjustmentType: HitPointAdjustment, amount: Int): HitPoints = when (adjustmentType) {
    HitPointAdjustment.DAMAGE -> takeDamage(amount)
    HitPointAdjustment.HEALING -> heal(amount)
    HitPointAdjustment.TEMPORARY -> setTemporaryHitPoints(amount)
    // An absolute value, not a delta: an amount nobody could mean is no change rather than a maximum of 1.
    HitPointAdjustment.MAXIMUM -> if (amount < MIN_MAX_HIT_POINTS) this else setMaxHitPoints(amount)
}

fun Character.adjustHitPoints(adjustmentType: HitPointAdjustment, amount: Int): Character =
    withHitPoints(hitPoints.adjust(adjustmentType, amount))
