package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.userlist.domain.UserList
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserListItem(
    list: UserList,
    onClick: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
                    .padding(spacingCommon).weight(1f),
            )
            IconButton(onClick = onDelete) {
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Preview
@Composable
private fun PreviewUserListItemLight() {
    AppThemePreview(darkTheme = false) {
        UserListItem(
            list = UserList("1", "Sorts de combat", UserList.Type.SPELL, listOf("spell1")),
            onClick = {},
            onDelete = {},
        )
    }
}

@Preview
@Composable
private fun PreviewUserListItemDark() {
    AppThemePreview(darkTheme = true) {
        UserListItem(
            list = UserList("1", "Sorts de combat", UserList.Type.SPELL, listOf("spell1")),
            onClick = {},
            onDelete = {},
        )
    }
}
