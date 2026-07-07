package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontStyle
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.ui.tooling.preview.Preview

private val borderStroke = spacingMedium
private val textPadding = spacingMedium

@Composable
fun MagicalItemCard(
    magicalItem: MagicalItem,
    modifier: Modifier = Modifier
        .padding(spacingSmall)
        .fillMaxSize(),
    content: @Composable ColumnScope.() -> Unit = {
        val translation = magicalItem.resolveTranslation(currentLocale())
        Text(
            text = magicalItem.getSubtitle(translation),
            style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = textPadding, end = textPadding, top = textPadding),
        )
        MarkdownText(
            text = translation.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(textPadding),
        )
    },
) {
    val color = magicalItem.getColor()
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        border = BorderStroke(borderStroke, color),
    ) {
        Column(Modifier.padding(borderStroke)) {
            MagicalItemCardHeader(magicalItem)
            content()
        }
    }
}

@Composable
internal fun MagicalItemCardHeader(magicalItem: MagicalItem) {
    val translation = magicalItem.resolveTranslation(currentLocale())
    val color = magicalItem.getColor()
    Text(
        text = translation.name,
        style = MaterialTheme.typography.titleLarge,
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier
            .fillMaxWidth()
            .background(color)
            .padding(
                start = textPadding,
                end = textPadding,
                top = textPadding / 2,
                bottom = textPadding / 2,
            ),
    )
}

@Preview
@Composable
private fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemRepository.getFirst()
    MagicalItemCard(item)
}
