package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.component.dnd.SUBTITLE_SEPARATOR
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.joinNonNull
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.item_requires_attunement

@Composable
fun MagicalItemListItem(
    magicalItem: MagicalItem,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val translation = magicalItem.resolveTranslation(currentLocale())
    AppCard(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(spacingCommon),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = translation.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Type + subtype + attunement
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = magicalItem.type.toFormattedString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = magicalItem.getColor(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    getSubtitle(magicalItem, translation)?.let { subtitle ->
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.width(spacingMedium))

            Text(
                text = magicalItem.rarity.toFormattedString(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun getSubtitle(magicalItem: MagicalItem, translation: MagicalItem.Translation): String? {
    val subtype = translation.subtype?.let { " ($it)" }
    val attunement = if (magicalItem.attunement) {
        SUBTITLE_SEPARATOR + stringResource(Res.string.item_requires_attunement)
    } else {
        null
    }
    return joinNonNull(subtype, attunement)
}

@Preview
@Composable
private fun PreviewMagicalItemListItemLight() {
    AppThemePreview(darkTheme = false) {
        MagicalItemListItem(magicalItem = SampleMagicalItemRepository.getFirst(), onClick = {})
    }
}

@Preview
@Composable
private fun PreviewMagicalItemListItemDark() {
    AppThemePreview(darkTheme = true) {
        MagicalItemListItem(magicalItem = SampleMagicalItemRepository.getFirst(), onClick = {})
    }
}
