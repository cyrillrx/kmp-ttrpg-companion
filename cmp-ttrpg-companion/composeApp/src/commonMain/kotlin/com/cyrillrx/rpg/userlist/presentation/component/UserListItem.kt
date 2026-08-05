package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.time.Instant

@Composable
fun UserListItem(
    list: UserList,
    updatedAt: Instant,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    AppCard(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.padding(spacingCommon)) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = list.subtitle(updatedAt),
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
        val sample = SampleUserListRepository.getFirst()
        UserListItem(
            list = sample.value,
            updatedAt = sample.updatedAt,
            onClick = {},
        )
    }
}
