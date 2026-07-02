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
import rpg_companion.composeapp.generated.resources.btn_create_list
import rpg_companion.composeapp.generated.resources.hint_list_name
import rpg_companion.composeapp.generated.resources.title_create_list

@Composable
fun CreateListDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }

    EditDialog(
        title = stringResource(Res.string.title_create_list),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(name) },
        confirmEnabled = name.isNotBlank(),
        confirmLabel = stringResource(Res.string.btn_create_list),
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
private fun PreviewCreateListDialogLight() {
    AppThemePreview(darkTheme = false) {
        CreateListDialog(onConfirm = {}, onDismiss = {})
    }
}

@Preview
@Composable
private fun PreviewCreateListDialogDark() {
    AppThemePreview(darkTheme = true) {
        CreateListDialog(onConfirm = {}, onDismiss = {})
    }
}
