package com.cyrillrx.rpg.core.presentation

import androidx.compose.runtime.Composable
import kotlinx.datetime.Instant
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.time_ago_days
import rpg_companion.composeapp.generated.resources.time_ago_hours
import rpg_companion.composeapp.generated.resources.time_ago_minutes
import rpg_companion.composeapp.generated.resources.time_ago_months
import rpg_companion.composeapp.generated.resources.time_ago_now
import rpg_companion.composeapp.generated.resources.time_ago_years
import kotlin.time.Clock

/** Coarse relative-time bucket computed from the elapsed duration between two instants. */
sealed interface RelativeTimePeriod {
    object Now : RelativeTimePeriod
    data class Minutes(val count: Int) : RelativeTimePeriod
    data class Hours(val count: Int) : RelativeTimePeriod
    data class Days(val count: Int) : RelativeTimePeriod
    data class Months(val count: Int) : RelativeTimePeriod
    data class Years(val count: Int) : RelativeTimePeriod
}

/**
 * Computes the relative-time bucket between this instant and [now].
 * Returns null for the epoch-zero sentinel, i.e. when no modification date is known.
 */
fun Instant.getRelativeTimePeriod(now: Instant): RelativeTimePeriod? {
    if (toEpochMilliseconds() == 0L) return null

    val elapsed = now - this
    val minutes = elapsed.inWholeMinutes.toInt()
    val hours = elapsed.inWholeHours.toInt()
    val days = elapsed.inWholeDays.toInt()
    return when {
        minutes < 1 -> RelativeTimePeriod.Now
        hours < 1 -> RelativeTimePeriod.Minutes(minutes)
        days < 1 -> RelativeTimePeriod.Hours(hours)
        days < 30 -> RelativeTimePeriod.Days(days)
        days < 360 -> RelativeTimePeriod.Months((days / 30).coerceAtLeast(1))
        else -> RelativeTimePeriod.Years((days / 365).coerceAtLeast(1))
    }
}

/**
 * Formats this instant as a human-readable "x ago" string (now / minutes / hours / days / months / years).
 * Returns null for the epoch-zero sentinel, i.e. when no modification date is known.
 */
@Composable
fun Instant.formatRelativeTime(now: Instant = Clock.System.now()): String? {
    return when (val period = getRelativeTimePeriod(now)) {
        null -> null
        RelativeTimePeriod.Now -> stringResource(Res.string.time_ago_now)
        is RelativeTimePeriod.Minutes -> pluralStringResource(Res.plurals.time_ago_minutes, period.count, period.count)
        is RelativeTimePeriod.Hours -> pluralStringResource(Res.plurals.time_ago_hours, period.count, period.count)
        is RelativeTimePeriod.Days -> pluralStringResource(Res.plurals.time_ago_days, period.count, period.count)
        is RelativeTimePeriod.Months -> pluralStringResource(Res.plurals.time_ago_months, period.count, period.count)
        is RelativeTimePeriod.Years -> pluralStringResource(Res.plurals.time_ago_years, period.count, period.count)
    }
}
