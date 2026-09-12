package com.cyrillrx.rpg.character.domain

/** The hit point pools of a [Character], extracted so the rules and the editors can work without the whole sheet. */
data class HitPoints(
    val current: Int,
    val max: Int,
    val temporary: Int = 0,
)

enum class HitPointAdjustment { DAMAGE, HEALING, MAXIMUM, TEMPORARY }

val Character.hitPoints: HitPoints
    get() = HitPoints(current = currentHitPoints, max = maxHitPoints, temporary = temporaryHitPoints)

fun Character.withHitPoints(hitPoints: HitPoints): Character = copy(
    currentHitPoints = hitPoints.current,
    maxHitPoints = hitPoints.max,
    temporaryHitPoints = hitPoints.temporary,
)
