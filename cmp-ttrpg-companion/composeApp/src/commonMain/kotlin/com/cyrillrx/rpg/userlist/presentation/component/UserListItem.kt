package com.cyrillrx.rpg.userlist.presentation.component

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
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun UserListItem(
    list: UserList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(onClick = onClick, modifier = modifier) {
        Text(
            text = list.name,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(spacingCommon),
        )
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
