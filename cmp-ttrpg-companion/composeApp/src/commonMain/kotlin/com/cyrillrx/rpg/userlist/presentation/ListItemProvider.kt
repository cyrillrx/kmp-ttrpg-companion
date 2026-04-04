package com.cyrillrx.rpg.userlist.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.cyrillrx.rpg.userlist.presentation.component.EmptyListLayout
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

interface ListItemProvider<T> {
    fun getId(entity: T): String
    fun getDisplayName(entity: T): String

    @Composable
    fun ListItem(entity: T, modifier: Modifier)

    val emptyLayoutIcon: ImageVector
    val emptyLayoutBtnText: StringResource
    val onEmptyLayoutBtnClicked: () -> Unit get() = {}

    @Composable
    fun EmptyLayout() {
        EmptyListLayout(
            icon = emptyLayoutIcon,
            btnText = stringResource(emptyLayoutBtnText),
            onBtnClicked = onEmptyLayoutBtnClicked,
        )
    }
}
