package com.cyrillrx.rpg.userlist.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.creature.data.SampleCreatureRepository
import com.cyrillrx.rpg.creature.domain.Creature
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddCreatureHeader(creature: Creature, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = creature.name,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
        )

        Text(
            text = creature.type.toFormattedString(),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        HorizontalDivider(modifier = Modifier.padding(spacingMedium))
    }
}

@Preview
@Composable
private fun PreviewAddCreatureHeaderLight() {
    AddCreatureHeaderPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewAddCreatureHeaderDark() {
    AddCreatureHeaderPreview(darkTheme = true)
}

@Composable
private fun AddCreatureHeaderPreview(darkTheme: Boolean) {
    val creature = SampleCreatureRepository.getFirst()
    AppThemePreview(darkTheme = darkTheme) {
        AddCreatureHeader(creature)
    }
}
