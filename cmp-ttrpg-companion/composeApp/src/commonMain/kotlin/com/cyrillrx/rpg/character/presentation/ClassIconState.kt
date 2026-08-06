package com.cyrillrx.rpg.character.presentation

import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.presentation.format.toSvgPath

internal sealed interface ClassIconState {
    data object Loading : ClassIconState
    data class Loaded(val bytes: ByteArray) : ClassIconState
    data object Error : ClassIconState
    data object Unknown : ClassIconState
}

internal suspend fun resolveClassIconState(
    clazz: Character.Class,
    readBytes: suspend (String) -> ByteArray,
): ClassIconState = if (clazz == Character.Class.UNKNOWN) {
    ClassIconState.Unknown
} else {
    try {
        ClassIconState.Loaded(readBytes(clazz.toSvgPath()))
    } catch (_: Exception) {
        ClassIconState.Error
    }
}
