package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.iconButtonSize
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_cancel
import rpg_companion.composeapp.generated.resources.btn_confirm

@Composable
internal fun Dialog(
    title: String,
    subtitle: String? = null,
    onDismiss: () -> Unit,
    onConfirm: (() -> Unit)? = null,
    confirmEnabled: Boolean = true,
    content: @Composable () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            if (subtitle == null) {
                Text(title)
            } else {
                Column {
                    Text(title)
                    Text(
                        text = subtitle,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        },
        text = content,
        confirmButton = {
            if (onConfirm != null) {
                TextButton(onClick = onConfirm, enabled = confirmEnabled) {
                    Text(stringResource(Res.string.btn_confirm))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(Res.string.btn_cancel))
            }
        },
    )
}

@Composable
internal fun DecrementIncrementRow(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth(),
    ) {
        FilledTonalIconButton(
            onClick = onDecrement,
            enabled = value > minValue,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Text("-", style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        FilledTonalIconButton(
            onClick = onIncrement,
            enabled = value < maxValue,
            modifier = Modifier.size(iconButtonSize),
        ) {
            Text("+", style = MaterialTheme.typography.headlineMedium)
        }
    }
}
