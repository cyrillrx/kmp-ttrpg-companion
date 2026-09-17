package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.domain.MIN_HIT_POINTS
import com.cyrillrx.rpg.core.domain.toSignedString

private const val HIGH_THRESHOLD = 0.5f
private const val MEDIUM_THRESHOLD = 0.25f

internal enum class HealthLevel { HIGH, MEDIUM, LOW }

internal data class HealthGaugeState(
    val currentFraction: Float,
    val missingFraction: Float,
    val temporaryFraction: Float,
    val level: HealthLevel,
    val isDown: Boolean,
    val ratio: String,
    val temporary: String?,
)

/**
 * The gauge spans [HitPoints.max] plus [HitPoints.temporary], and the missing health keeps its place
 * between the two, so temporary hit points show up beyond the maximum the way they stack on top of it
 * in play. The colour threshold ignores them: they are a buffer, not health.
 */
internal fun HitPoints.toGaugeState(): HealthGaugeState {
    val total = max + temporary
    val ratioToMax = if (max > 0) current.toFloat() / max else 0f
    val filled = current.coerceAtLeast(MIN_HIT_POINTS).coerceAtMost(max)
    return HealthGaugeState(
        currentFraction = if (total > 0) filled.toFloat() / total else 0f,
        missingFraction = if (total > 0) (max - filled).toFloat() / total else 0f,
        temporaryFraction = if (total > 0) temporary.toFloat() / total else 0f,
        level = when {
            ratioToMax > HIGH_THRESHOLD -> HealthLevel.HIGH
            ratioToMax > MEDIUM_THRESHOLD -> HealthLevel.MEDIUM
            else -> HealthLevel.LOW
        },
        isDown = current <= MIN_HIT_POINTS,
        ratio = "$current / $max",
        temporary = if (temporary > MIN_HIT_POINTS) temporary.toSignedString() else null,
    )
}
