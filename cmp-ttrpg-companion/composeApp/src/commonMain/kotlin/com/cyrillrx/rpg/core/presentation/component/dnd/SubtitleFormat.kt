package com.cyrillrx.rpg.core.presentation.component.dnd

const val SUBTITLE_SEPARATOR = " · "

/**
 * Concatenates the non-null [segments] as-is (no separator inserted between them).
 *
 * Each segment is expected to already carry its own leading connector, because subtitle
 * segments are heterogeneous: some are parenthetical (`" (subtype)"`) and some are
 * separator-prefixed (`SUBTITLE_SEPARATOR + value`). Returns `null` when every segment is null,
 * so callers can omit the subtitle entirely.
 */
fun joinNonNull(vararg segments: String?): String? {
    val validSegments = segments.filterNotNull()
    if (validSegments.isEmpty()) return null

    return validSegments.joinToString("")
}
