package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.HitPoints
import com.cyrillrx.rpg.character.domain.MAX_HIT_POINTS
import com.cyrillrx.rpg.character.domain.applying
import com.cyrillrx.rpg.character.domain.coerceToValidHitPointAmount
import com.cyrillrx.rpg.character.domain.isDown
import com.cyrillrx.rpg.core.domain.toSignedString

private val maxInputLength = MAX_HIT_POINTS.toString().length

internal data class HitPointsEditorState(
    val hitPoints: HitPoints,
    val adjustment: HitPointAdjustment,
    val input: String = "",
) {
    val amount: Int get() = input.toIntOrNull() ?: 0

    val preview: HitPoints get() = hitPoints.applying(adjustment, amount)

    val isApplyEnabled: Boolean get() = preview != hitPoints

    val isPreviewDown: Boolean get() = preview.isDown

    // e.g. "-35" for damage, "+20" for healing, "5" for temporary hit points
    val signedAmount: String
        get() {
            if (amount == 0) return "0"
            return when (adjustment) {
                HitPointAdjustment.DAMAGE -> (-amount).toSignedString()
                HitPointAdjustment.HEALING -> amount.toSignedString()
                HitPointAdjustment.TEMPORARY -> amount.toString()
            }
        }

    // e.g. "22/24"
    val currentRatio: String get() = hitPoints.toRatio()

    val previewRatio: String get() = preview.toRatio()
}

internal fun HitPointsEditorState.withAdjustment(adjustment: HitPointAdjustment): HitPointsEditorState =
    copy(adjustment = adjustment)

internal fun HitPointsEditorState.withDigitAppended(digit: Int): HitPointsEditorState {
    val appended = (input + digit).trimStart('0')
    return if (appended.length > maxInputLength) this else copy(input = appended)
}

internal fun HitPointsEditorState.withLastDigitRemoved(): HitPointsEditorState = copy(input = input.dropLast(1))

internal fun HitPointsEditorState.cleared(): HitPointsEditorState = copy(input = "")

internal fun HitPointsEditorState.withAmount(amount: Int): HitPointsEditorState =
    copy(input = amount.coerceToValidHitPointAmount().toString())

private fun HitPoints.toRatio(): String = "$current/$max"
