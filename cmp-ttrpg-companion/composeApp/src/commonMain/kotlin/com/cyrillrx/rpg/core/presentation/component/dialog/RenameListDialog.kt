package com.cyrillrx.rpg.core.presentation.component.dialog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.accessibilityId
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.dialog_rename_list_title
import rpg_companion.composeapp.generated.resources.hint_list_name

@Composable
fun RenameListDialog(
    currentName: String,
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf(currentName) }
    val trimmedName = name.trim()

    EditDialog(
        title = stringResource(Res.string.dialog_rename_list_title),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(trimmedName) },
        confirmEnabled = trimmedName.isNotBlank() && trimmedName != currentName,
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(Res.string.hint_list_name),
            modifier = Modifier.accessibilityId("input_list_name"),
        )
    }
}

@Preview
@Composable
private fun PreviewRenameListDialogLight() {
    AppThemePreview(darkTheme = false) {
        RenameListDialog(currentName = "My list", onConfirm = {}, onDismiss = {})
    }
}

@Preview
@Composable
private fun PreviewRenameListDialogDark() {
    AppThemePreview(darkTheme = true) {
        RenameListDialog(currentName = "My list", onConfirm = {}, onDismiss = {})
    }
}
