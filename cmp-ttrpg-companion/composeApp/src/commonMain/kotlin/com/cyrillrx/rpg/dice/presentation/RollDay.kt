package com.cyrillrx.rpg.dice.presentation

import com.cyrillrx.rpg.dice.domain.RecordedRoll
import com.cyrillrx.rpg.dice.domain.RollPool
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.toLocalDateTime

data class RollDay(val date: LocalDate, val rolls: List<DayRoll>)

data class DayRoll(val id: Long, val pool: RollPool, val time: LocalTime)

enum class RelativeDay { TODAY, YESTERDAY, EARLIER }

/** Keeps the order of the rolls, so a newest-first history yields its days newest first. */
fun List<RecordedRoll>.groupByDay(timeZone: TimeZone): List<RollDay> = map { recorded ->
    val rolledAt = recorded.rolledAt.toLocalDateTime(timeZone)
    rolledAt.date to DayRoll(recorded.id, recorded.pool, rolledAt.time)
}.groupBy(keySelector = { it.first }, valueTransform = { it.second })
    .map { (date, rolls) -> RollDay(date, rolls) }

fun LocalDate.relativeTo(today: LocalDate): RelativeDay = when (this) {
    today -> RelativeDay.TODAY
    today.minus(1, DateTimeUnit.DAY) -> RelativeDay.YESTERDAY
    else -> RelativeDay.EARLIER
}

// e.g. "09:05"
fun LocalTime.toClockTime(): String = "${hour.twoDigits()}:${minute.twoDigits()}"

internal fun Int.twoDigits(): String = toString().padStart(2, '0')
