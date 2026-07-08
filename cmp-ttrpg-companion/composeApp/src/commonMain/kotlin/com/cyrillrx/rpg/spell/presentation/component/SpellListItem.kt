package com.cyrillrx.rpg.spell.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.AppCard
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedLevel
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.component.dnd.toIcon
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeSmall
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SpellListItem(
    spell: Spell,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val translation = spell.resolveTranslation(currentLocale())
    val school = spell.school
    val accent = spell.getColor()

    AppCard(onClick = onClick, modifier = modifier) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(spacingCommon),
        ) {
            // Spell info
            Column(
                verticalArrangement = Arrangement.spacedBy(spacingSmall),
                modifier = Modifier.weight(1f),
            ) {
                // Title
                Text(
                    text = translation.name,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )

                // School + Casting time
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = school.toIcon(),
                        contentDescription = null,
                        tint = accent,
                        modifier = Modifier.size(iconSizeSmall),
                    )
                    Spacer(modifier = Modifier.width(spacingSmall))
                    Text(
                        text = school.toFormattedString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = accent,
                    )
                    Text(
                        text = " · ",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    Text(
                        text = translation.castingTime,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }

            Spacer(modifier = Modifier.width(spacingMedium))

            // Level
            Text(
                text = spell.toFormattedLevel(),
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewSpellListItemLight() {
    SpellListItemPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewSpellListItemDark() {
    SpellListItemPreview(darkTheme = true)
}

@Composable
private fun SpellListItemPreview(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        Column(verticalArrangement = Arrangement.spacedBy(spacingSmall)) {
            SampleSpellRepository.getAll().forEach {
                SpellListItem(spell = it, onClick = {})
            }
        }
    }
}
