package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.domain.MAX_HIT_POINTS
import com.cyrillrx.rpg.character.domain.adjust
import com.cyrillrx.rpg.character.domain.coerceToValidHitPointAmount
import com.cyrillrx.rpg.character.domain.isDown
import com.cyrillrx.rpg.character.domain.isLethalDamage
import com.cyrillrx.rpg.character.domain.isValidMaxHitPoints
import com.cyrillrx.rpg.core.domain.toSignedString

private val maxInputLength = MAX_HIT_POINTS.toString().length

internal enum class PreviewOutcome { NONE, STANDING, DOWN, LETHAL }

internal data class HitPointsEditorState(
    val hitPoints: HitPoints,
    val adjustment: HitPointAdjustment,
    val input: String = "",
) {
    val amount: Int get() = input.toIntOrNull() ?: 0

    // An empty keypad is not an amount of zero: a typed zero clears the temporary pool, while
    // nothing typed leaves it alone. A maximum of zero would coerce up to one, so it is refused.
    private val hasValidAmount: Boolean
        get() = input.isNotEmpty() && (adjustment != HitPointAdjustment.MAXIMUM || isValidMaxHitPoints(amount))

    val preview: HitPoints get() = if (hasValidAmount) hitPoints.adjust(adjustment, amount) else hitPoints

    // A killing blow on a character already at zero moves no pool, but refusing it would contradict
    // the badge announcing it.
    val isApplyEnabled: Boolean
        get() = hasValidAmount && (preview != hitPoints || outcome == PreviewOutcome.LETHAL)

    // NONE is no blow to speak of, which is not the same as a blow they walk away from: an empty
    // keypad announces nothing even on a character who is already down. A lethal blow always
    // empties the pool, so the two remaining cases are ordered rather than combined.
    val outcome: PreviewOutcome
        get() = when {
            !hasValidAmount -> PreviewOutcome.NONE
            adjustment == HitPointAdjustment.DAMAGE && hitPoints.isLethalDamage(amount) -> PreviewOutcome.LETHAL
            preview.isDown -> PreviewOutcome.DOWN
            else -> PreviewOutcome.STANDING
        }

    // A shortcut adds to the typed amount, which only reads as a delta on damage and healing:
    // the temporary and maximum amounts replace the pool rather than move it.
    val showsShortcuts: Boolean
        get() = adjustment == HitPointAdjustment.DAMAGE || adjustment == HitPointAdjustment.HEALING

    // e.g. "-35" for damage, "+20" for healing; temporary and maximum hit points are absolute, not deltas
    val signedAmount: String
        get() {
            if (amount == 0) return "0"
            return when (adjustment) {
                HitPointAdjustment.DAMAGE -> (-amount).toSignedString()
                HitPointAdjustment.HEALING -> amount.toSignedString()
                HitPointAdjustment.TEMPORARY, HitPointAdjustment.MAXIMUM -> amount.toString()
            }
        }

    // e.g. "22/24", or "22/24(+5)" once temporary hit points are in play on either side
    val currentRatio: String get() = hitPoints.toRatio(showsTemporary)

    val previewRatio: String get() = preview.toRatio(showsTemporary)

    // Spelling out a temporary pool that stays empty would be noise; dropping the one that falls to
    // zero would hide the change. So both sides carry the suffix as soon as either one needs it.
    private val showsTemporary: Boolean get() = hitPoints.temporary > 0 || preview.temporary > 0
}

internal fun HitPointsEditorState.withAdjustment(adjustment: HitPointAdjustment): HitPointsEditorState =
    copy(adjustment = adjustment)

internal fun HitPointsEditorState.withDigitAppended(digit: Int): HitPointsEditorState {
    val appended = (input + digit).trimStart('0').ifEmpty { "0" }
    return if (appended.length > maxInputLength) this else copy(input = appended)
}

internal fun HitPointsEditorState.withLastDigitRemoved(): HitPointsEditorState = copy(input = input.dropLast(1))

internal fun HitPointsEditorState.cleared(): HitPointsEditorState = copy(input = "")

internal fun HitPointsEditorState.withAmountAdded(delta: Int): HitPointsEditorState =
    copy(input = (amount + delta).coerceToValidHitPointAmount().toString())

private fun HitPoints.toRatio(withTemporary: Boolean): String =
    if (withTemporary) "$current/$max(${temporary.toSignedString()})" else "$current/$max"
