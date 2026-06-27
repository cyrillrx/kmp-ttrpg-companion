package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_new_character
import rpg_companion.composeapp.generated.resources.btn_new_character_subtitle
import rpg_companion.composeapp.generated.resources.btn_quick_create
import rpg_companion.composeapp.generated.resources.btn_quick_create_subtitle

/** The pair of "New character" / "Quick create" action cards shown above the character list. */
@Composable
fun CharacterCreateActions(
    onNewCharacterClicked: () -> Unit,
    onQuickCreateClicked: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spacingMedium),
        modifier = modifier.fillMaxWidth().height(IntrinsicSize.Max),
    ) {
        CharacterActionCard(
            icon = Icons.Filled.Add,
            title = stringResource(Res.string.btn_new_character),
            subtitle = stringResource(Res.string.btn_new_character_subtitle),
            onClick = onNewCharacterClicked,
            filled = true,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
        CharacterActionCard(
            icon = Icons.Filled.Bolt,
            title = stringResource(Res.string.btn_quick_create),
            subtitle = stringResource(Res.string.btn_quick_create_subtitle),
            onClick = onQuickCreateClicked,
            filled = false,
            modifier = Modifier.weight(1f).fillMaxHeight(),
        )
    }
}

@Composable
private fun CharacterActionCard(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    filled: Boolean,
    modifier: Modifier = Modifier,
) {
    val containerColor = if (filled) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
    val contentColor = if (filled) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface

    Card(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        border = if (filled) {
            null
        } else {
            BorderStroke(
                width = borderWidth,
                color = MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha),
            )
        },
        colors = CardDefaults.cardColors(containerColor = containerColor),
        modifier = modifier,
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            modifier = Modifier.padding(spacingCommon),
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = contentColor,
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = contentColor,
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = contentColor,
            )
        }
    }
}

@Composable
private fun CharacterCreateActionsPreview() {
    CharacterCreateActions(
        onNewCharacterClicked = {},
        onQuickCreateClicked = {},
        modifier = Modifier.padding(spacingCommon),
    )
}

@Preview
@Composable
private fun PreviewCharacterCreateActionsLight() {
    AppThemePreview(darkTheme = false) { CharacterCreateActionsPreview() }
}

@Preview
@Composable
private fun PreviewCharacterCreateActionsDark() {
    AppThemePreview(darkTheme = true) { CharacterCreateActionsPreview() }
}
