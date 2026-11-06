package com.cyrillrx.rpg.dice.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.domain.toSignedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.historyRollTileMinSize
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.DoubleRoll
import com.cyrillrx.rpg.dice.domain.KeepRule
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.SimpleRoll
import com.cyrillrx.rpg.dice.presentation.DayRoll
import com.cyrillrx.rpg.dice.presentation.DiceHistoryState
import com.cyrillrx.rpg.dice.presentation.RelativeDay
import com.cyrillrx.rpg.dice.presentation.RollDay
import com.cyrillrx.rpg.dice.presentation.relativeTo
import com.cyrillrx.rpg.dice.presentation.toClockTime
import com.cyrillrx.rpg.dice.presentation.toTwoDigits
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.number
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.die_label
import rpg_companion.composeapp.generated.resources.label_roll_day
import rpg_companion.composeapp.generated.resources.label_today
import rpg_companion.composeapp.generated.resources.label_yesterday

@Composable
fun RollHistoryList(state: DiceHistoryState.Body.WithData, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        state.days.forEach { day ->
            item(key = day.date.toString()) { DayHeader(day.date, state.today) }
            items(day.rolls, key = DayRoll::id) { roll ->
                Column(modifier = Modifier.animateItem()) {
                    HistoryEntry(roll)
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
private fun DayHeader(date: LocalDate, today: LocalDate) {
    val label = when (date.relativeTo(today)) {
        RelativeDay.TODAY -> stringResource(Res.string.label_today)
        RelativeDay.YESTERDAY -> stringResource(Res.string.label_yesterday)
        RelativeDay.EARLIER -> stringResource(
            Res.string.label_roll_day,
            date.day.toTwoDigits(),
            date.month.number.toTwoDigits(),
            date.year.toString(),
        )
    }

    Text(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier.padding(
            start = spacingCommon,
            end = spacingCommon,
            top = spacingCommon,
            bottom = spacingSmall,
        ),
    )
}

@Composable
private fun HistoryEntry(roll: DayRoll) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = spacingCommon, vertical = spacingMedium),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = roll.pool.toNotation().orEmpty(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = roll.pool.total().toString(),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary,
            )
        }

        Row(verticalAlignment = Alignment.Bottom) {
            HistoryRolls(roll.pool, modifier = Modifier.weight(1f))
            Text(
                text = roll.time.toClockTime(),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = spacingMedium),
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun HistoryRolls(pool: RollPool, modifier: Modifier = Modifier) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        itemVerticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        pool.rollsByDie().forEach { (die, rolls) ->
            Text(
                text = stringResource(Res.string.die_label, die.sides),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            rolls.forEach { HistoryRollTile(it) }
        }

        if (pool.modifier != 0) {
            Text(
                text = pool.modifier.toSignedString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun HistoryRollTile(roll: DieRoll) {
    Surface(
        shape = MaterialTheme.shapes.extraSmall,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        modifier = Modifier.sizeIn(minWidth = historyRollTileMinSize, minHeight = historyRollTileMinSize),
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(horizontal = spacingSmall)) {
            when (roll) {
                is SimpleRoll -> Text(text = roll.value.toString(), style = MaterialTheme.typography.labelLarge)
                is DoubleRoll -> DoubleRollContent(
                    roll,
                    keptStyle = MaterialTheme.typography.labelLarge,
                    discardedStyle = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewRollHistoryListLight() {
    AppThemePreview(darkTheme = false) { RollHistoryList(previewHistory) }
}

@Preview
@Composable
private fun PreviewRollHistoryListDark() {
    AppThemePreview(darkTheme = true) { RollHistoryList(previewHistory) }
}

internal val previewHistory = DiceHistoryState.Body.WithData(
    days = listOf(
        RollDay(
            date = LocalDate(year = 2026, month = 11, day = 2),
            rolls = listOf(
                DayRoll(
                    id = 3,
                    pool = RollPool(
                        listOf(
                            SimpleRoll(Die.D8, value = 6),
                            SimpleRoll(Die.D6, value = 5),
                            SimpleRoll(Die.D6, value = 4),
                        ),
                        modifier = 2,
                    ),
                    time = LocalTime(hour = 21, minute = 4),
                ),
                DayRoll(
                    id = 2,
                    pool = RollPool(
                        listOf(
                            DoubleRoll(
                                kept = SimpleRoll(Die.D20, value = 17),
                                discarded = SimpleRoll(Die.D20, value = 3),
                                rule = KeepRule.HIGHER,
                            ),
                        ),
                        modifier = 2,
                    ),
                    time = LocalTime(hour = 21, minute = 3),
                ),
            ),
        ),
        RollDay(
            date = LocalDate(year = 2026, month = 10, day = 30),
            rolls = listOf(
                DayRoll(
                    id = 1,
                    pool = RollPool(listOf(SimpleRoll(Die.D100, value = 42))),
                    time = LocalTime(hour = 22, minute = 15),
                ),
            ),
        ),
    ),
    today = LocalDate(year = 2026, month = 11, day = 2),
)
