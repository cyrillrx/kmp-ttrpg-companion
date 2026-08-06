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
import rpg_companion.composeapp.generated.resources.btn_create_collection
import rpg_companion.composeapp.generated.resources.hint_collection_name
import rpg_companion.composeapp.generated.resources.title_create_collection

@Composable
fun CreateCollectionDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    var name by remember { mutableStateOf("") }
    val trimmedName = name.trim()

    EditDialog(
        title = stringResource(Res.string.title_create_collection),
        onDismiss = onDismiss,
        onConfirm = { onConfirm(trimmedName) },
        confirmEnabled = trimmedName.isNotBlank(),
        confirmLabel = stringResource(Res.string.btn_create_collection),
    ) {
        DialogTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = stringResource(Res.string.hint_collection_name),
            modifier = Modifier.accessibilityId("input_collection_name"),
        )
    }
}

@Preview
@Composable
private fun PreviewCreateCollectionDialogLight() {
    AppThemePreview(darkTheme = false) {
        CreateCollectionDialog(onConfirm = {}, onDismiss = {})
    }
}

@Preview
@Composable
private fun PreviewCreateCollectionDialogDark() {
    AppThemePreview(darkTheme = true) {
        CreateCollectionDialog(onConfirm = {}, onDismiss = {})
    }
}
