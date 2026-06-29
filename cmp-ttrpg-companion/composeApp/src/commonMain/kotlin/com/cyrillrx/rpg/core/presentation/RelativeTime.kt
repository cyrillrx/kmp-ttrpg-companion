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

/**
 * Formats this instant as a human-readable "x ago" string (now / minutes / hours / days / months / years).
 * Returns null for the epoch-zero sentinel, i.e. when no modification date is known.
 */
@Composable
fun Instant.formatRelativeTime(): String? {
    if (toEpochMilliseconds() == 0L) return null

    val elapsed = Clock.System.now() - this
    val minutes = elapsed.inWholeMinutes.toInt()
    val hours = elapsed.inWholeHours.toInt()
    val days = elapsed.inWholeDays.toInt()
    return when {
        minutes < 1 -> stringResource(Res.string.time_ago_now)
        hours < 1 -> pluralStringResource(Res.plurals.time_ago_minutes, minutes, minutes)
        days < 1 -> pluralStringResource(Res.plurals.time_ago_hours, hours, hours)
        days < 30 -> pluralStringResource(Res.plurals.time_ago_days, days, days)
        days < 365 -> {
            val months = (days / 30).coerceAtLeast(1)
            pluralStringResource(Res.plurals.time_ago_months, months, months)
        }

        else -> {
            val years = (days / 365).coerceAtLeast(1)
            pluralStringResource(Res.plurals.time_ago_years, years, years)
        }
    }
}
