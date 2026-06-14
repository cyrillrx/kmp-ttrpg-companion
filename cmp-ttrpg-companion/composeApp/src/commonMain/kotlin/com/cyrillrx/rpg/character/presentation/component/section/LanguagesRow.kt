package com.cyrillrx.rpg.character.presentation.component.section

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.borderAlpha
import com.cyrillrx.rpg.core.presentation.theme.borderWidth
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_languages
import rpg_companion.composeapp.generated.resources.label_languages_none

@Composable
internal fun LanguagesRow(
    languages: List<Language>,
    onTap: () -> Unit,
) {
    Card(
        onClick = onTap,
        border = BorderStroke(borderWidth, MaterialTheme.colorScheme.outline.copy(alpha = borderAlpha)),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = spacingCommon, vertical = spacingMedium),
        ) {
            Text(
                text = stringResource(Res.string.label_languages),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            if (languages.isEmpty()) {
                Text(
                    text = stringResource(Res.string.label_languages_none),
                    style = MaterialTheme.typography.bodyMedium,
                    fontStyle = FontStyle.Italic,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else {
                Text(
                    text = languages.map { it.toFormattedString() }.joinToString(" · "),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
