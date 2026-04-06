package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import kotlin.time.Clock
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_count
import rpg_companion.composeapp.generated.resources.magical_item_count
import rpg_companion.composeapp.generated.resources.spell_count
import rpg_companion.composeapp.generated.resources.time_ago_days
import rpg_companion.composeapp.generated.resources.time_ago_hours
import rpg_companion.composeapp.generated.resources.time_ago_minutes
import rpg_companion.composeapp.generated.resources.time_ago_months
import rpg_companion.composeapp.generated.resources.time_ago_now
import rpg_companion.composeapp.generated.resources.time_ago_years

@Composable
fun UserListItem(
    list: UserList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val count = list.itemIds.size
    val countItemText = pluralStringResource(
        resource = when (list.type) {
            UserList.Type.SPELL -> Res.plurals.spell_count
            UserList.Type.MAGICAL_ITEM -> Res.plurals.magical_item_count
            UserList.Type.CREATURE -> Res.plurals.creature_count
        },
        quantity = count,
        count,
    )
    val relativeTimeText = list.lastModified
        .takeIf { it.toEpochMilliseconds() != 0L }
        ?.let { Clock.System.now() - it }
        ?.let { elapsed ->
            val minutes = elapsed.inWholeMinutes.toInt()
            val hours = elapsed.inWholeHours.toInt()
            val days = elapsed.inWholeDays.toInt()
            when {
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
    val subtitle = if (relativeTimeText == null) countItemText else "$countItemText - $relativeTimeText"
    Card(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.padding(spacingCommon)) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewUserListItemLight() {
    UserListItemPreview(false)
}

@Preview
@Composable
private fun PreviewUserListItemDark() {
    UserListItemPreview(true)
}

@Composable
private fun UserListItemPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        UserListItem(
            list = SampleUserListRepository.getFirst(),
            onClick = {},
        )
    }
}
