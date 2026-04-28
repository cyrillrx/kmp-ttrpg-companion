package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.userlist.presentation.component.EmptyListLayout
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.message_list_is_empty

interface ListItemProvider<T> {
    val emptyLayoutIcon: ImageVector
    val emptyLayoutBtnText: StringResource
    val onEmptyLayoutBtnClicked: () -> Unit get() = {}

    fun getId(entity: T): String
    fun getDisplayName(entity: T, locale: String): String

    @Composable
    fun ListItem(entity: T, modifier: Modifier)

    @Composable
    fun EmptyLayout() {
        EmptyListLayout(
            icon = emptyLayoutIcon,
            message = stringResource(Res.string.message_list_is_empty),
            btnText = stringResource(emptyLayoutBtnText),
            onBtnClicked = onEmptyLayoutBtnClicked,
        )
    }
}
