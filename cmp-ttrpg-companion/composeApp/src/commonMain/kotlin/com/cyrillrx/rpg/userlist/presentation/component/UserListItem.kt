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
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.creature_count
import rpg_companion.composeapp.generated.resources.magical_item_count
import rpg_companion.composeapp.generated.resources.spell_count

@Composable
fun UserListItem(
    list: UserList,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val count = list.itemIds.size
    val countText = pluralStringResource(
        resource = when (list.type) {
            UserList.Type.SPELL -> Res.plurals.spell_count
            UserList.Type.MAGICAL_ITEM -> Res.plurals.magical_item_count
            UserList.Type.CREATURE -> Res.plurals.creature_count
        },
        quantity = count,
        count,
    )
    Card(onClick = onClick, modifier = modifier) {
        Column(modifier = Modifier.padding(spacingCommon)) {
            Text(
                text = list.name,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = countText,
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
