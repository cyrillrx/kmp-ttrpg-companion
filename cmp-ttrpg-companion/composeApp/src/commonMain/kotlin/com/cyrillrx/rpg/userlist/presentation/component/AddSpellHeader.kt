package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.component.dnd.toIcon
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.spell.data.SampleSpellRepository
import com.cyrillrx.rpg.spell.domain.Spell
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddSpellHeader(spell: Spell, modifier: Modifier = Modifier) {
    val school = spell.schools.firstOrNull()
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = spell.title,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(spacingSmall),
        ) {
            Icon(
                imageVector = school.toIcon(),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(iconSizeMedium),
            )
            Text(
                text = spell.getSubtitle(),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }

        HorizontalDivider(modifier = Modifier.padding(spacingMedium))
    }
}

@Preview
@Composable
private fun PreviewAddSpellHeaderLight() {
    AddSpellHeaderPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewAddSpellHeaderDark() {
    AddSpellHeaderPreview(darkTheme = true)
}

@Composable
private fun AddSpellHeaderPreview(darkTheme: Boolean) {
    val spell = SampleSpellRepository.fireball()
    AppThemePreview(darkTheme = darkTheme) {
        AddSpellHeader(spell)
    }
}
