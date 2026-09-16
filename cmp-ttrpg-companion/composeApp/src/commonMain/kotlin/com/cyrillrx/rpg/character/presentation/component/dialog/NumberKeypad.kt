package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_backspace
import rpg_companion.composeapp.generated.resources.btn_clear

private val digitRows = listOf(1..3, 4..6, 7..9)

@Composable
internal fun NumberKeypad(
    onDigit: (Int) -> Unit,
    onClear: () -> Unit,
    onBackspace: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier.fillMaxWidth(),
    ) {
        digitRows.forEach { row ->
            KeyRow {
                row.forEach { digit -> DigitKey(digit, onDigit) }
            }
        }
        KeyRow {
            Key(onClick = onClear, contentDescription = stringResource(Res.string.btn_clear)) {
                KeyLabel("C")
            }
            DigitKey(0, onDigit)
            Key(onClick = onBackspace, contentDescription = stringResource(Res.string.btn_backspace)) {
                Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = null)
            }
        }
    }
}

@Composable
private fun KeyRow(content: @Composable RowScope.() -> Unit) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = Modifier.fillMaxWidth(),
        content = content,
    )
}

@Composable
private fun RowScope.DigitKey(digit: Int, onDigit: (Int) -> Unit) {
    Key(onClick = { onDigit(digit) }) { KeyLabel(digit.toString()) }
}

@Composable
private fun RowScope.Key(
    onClick: () -> Unit,
    contentDescription: String? = null,
    content: @Composable () -> Unit,
) {
    val modifier = Modifier.weight(1f)
    OutlinedButton(
        onClick = onClick,
        modifier = if (contentDescription == null) {
            modifier
        } else {
            modifier.semantics { this.contentDescription = contentDescription }
        },
    ) {
        content()
    }
}

@Composable
private fun KeyLabel(label: String) {
    Text(
        text = label,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
    )
}
