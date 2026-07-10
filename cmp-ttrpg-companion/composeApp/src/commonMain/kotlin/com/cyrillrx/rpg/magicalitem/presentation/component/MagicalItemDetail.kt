package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.toFormattedString
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.item_requires_attunement

@Composable
fun MagicalItemDetail(
    magicalItem: MagicalItem,
    modifier: Modifier = Modifier,
    titleModifier: Modifier = Modifier,
) {
    val translation = magicalItem.resolveTranslation(currentLocale())
    val accent = magicalItem.getColor()

    Column(
        modifier = modifier.padding(spacingCommon),
        verticalArrangement = Arrangement.spacedBy(spacingCommon),
    ) {
        MagicalItemHeader(magicalItem, translation, accent, titleModifier)
        MarkdownText(text = translation.description, modifier = Modifier.fillMaxWidth())
    }
}

@Composable
private fun MagicalItemHeader(
    magicalItem: MagicalItem,
    translation: MagicalItem.Translation,
    accent: Color,
    titleModifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Text(
            text = translation.name,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = titleModifier,
        )
        translation.subtype?.takeIf { it.isNotBlank() }?.let { subtype ->
            Text(
                text = subtype,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        Text(
            text = "${magicalItem.type.toFormattedString()} - ${magicalItem.rarity.toFormattedString()}".uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = accent,
        )
        HorizontalDivider(
            color = accent,
            modifier = Modifier.padding(horizontal = spacingCommon, vertical = spacingSmall),
        )
        if (magicalItem.attunement) {
            MagicalItemBadge(stringResource(Res.string.item_requires_attunement), accent)
        }
    }
}

@Composable
private fun MagicalItemBadge(text: String, accent: Color) {
    Text(
        text = text.uppercase(),
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = accent,
        modifier = Modifier
            .clip(MaterialTheme.shapes.small)
            .background(accent.copy(alpha = 0.15f))
            .padding(horizontal = spacingSmall, vertical = spacingSmall / 2),
    )
}

@Preview
@Composable
private fun PreviewMagicalItemDetailLight() {
    AppThemePreview(darkTheme = false) {
        MagicalItemDetail(SampleMagicalItemRepository.getFirst())
    }
}

@Preview
@Composable
private fun PreviewMagicalItemDetailDark() {
    AppThemePreview(darkTheme = true) {
        MagicalItemDetail(SampleMagicalItemRepository.getFirst())
    }
}
