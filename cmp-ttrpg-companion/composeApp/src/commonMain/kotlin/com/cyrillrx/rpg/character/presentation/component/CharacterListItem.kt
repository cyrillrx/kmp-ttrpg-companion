package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.component.IconLabel
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.formatRelativeTime
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.value_armor_class
import rpg_companion.composeapp.generated.resources.value_hit_points

@Composable
fun CharacterListItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val locale = currentLocale()
    val shortDescription = character.resolveTranslation(locale)?.shortDescription.orEmpty()
    val primaryText = shortDescription.ifBlank { character.name }
    val secondaryText = if (shortDescription.isNotBlank()) character.name else ""
    val relativeTime = character.lastModified.formatRelativeTime()

    AppCard(onClick = onClick, modifier = modifier) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacingCommon),
        ) {
            // Title line: name / short description on the left, class on the right.
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = primaryText,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = character.clazz.toFormattedString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            if (shortDescription.isNotBlank()) {
                Text(
                    text = secondaryText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Stats line: armor class / hit points on the left, last-modified date on the right.
            Row(
                horizontalArrangement = Arrangement.spacedBy(spacingCommon),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
            ) {
                IconLabel(
                    icon = Icons.Outlined.Shield,
                    text = stringResource(Res.string.value_armor_class, character.armorClass),
                )
                IconLabel(
                    icon = Icons.Outlined.Favorite,
                    text = stringResource(Res.string.value_hit_points, character.maxHitPoints),
                )
                if (relativeTime != null) {
                    Text(
                        text = relativeTime,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f),
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListItemLight() {
    AppThemePreview(darkTheme = false) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleCharacterRepository.getAll().forEach {
                CharacterListItem(character = it, onClick = {})
            }
        }
    }
}

@Preview
@Composable
private fun PreviewCharacterListItemDark() {
    AppThemePreview(darkTheme = true) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleCharacterRepository.getAll().forEach {
                CharacterListItem(character = it, onClick = {})
            }
        }
    }
}
