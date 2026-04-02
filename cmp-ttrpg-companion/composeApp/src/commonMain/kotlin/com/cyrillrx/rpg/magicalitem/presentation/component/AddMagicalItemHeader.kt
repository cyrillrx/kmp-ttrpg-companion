package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddMagicalItemHeader(magicalItem: MagicalItem, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(spacingCommon),
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(spacingSmall),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                text = magicalItem.title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
            )

            Text(
                text = "${magicalItem.type.toFormattedString()} · ${magicalItem.rarity.toFormattedString()}",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )

            HorizontalDivider(modifier = Modifier.padding(spacingMedium))
        }
    }
}

@Preview
@Composable
private fun PreviewAddMagicalItemHeaderLight() {
    AddMagicalItemHeaderPreview(darkTheme = false)
}

@Preview
@Composable
private fun PreviewAddMagicalItemHeaderDark() {
    AddMagicalItemHeaderPreview(darkTheme = true)
}

@Composable
private fun AddMagicalItemHeaderPreview(darkTheme: Boolean) {
    val magicalItem = SampleMagicalItemRepository.getFirst()
    AppThemePreview(darkTheme = darkTheme) {
        AddMagicalItemHeader(magicalItem)
    }
}
