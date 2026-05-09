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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.MarkdownText
import com.cyrillrx.rpg.core.presentation.component.dnd.getColor
import com.cyrillrx.rpg.core.presentation.component.dnd.getSubtitle
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.ui.tooling.preview.Preview

private val borderStroke = spacingMedium
private val textPadding = spacingMedium

@Composable
fun MagicalItemCard(
    magicalItem: MagicalItem,
    modifier: Modifier = Modifier
        .padding(4.dp)
        .fillMaxSize(),
    content: @Composable ColumnScope.() -> Unit = {
        val translation = magicalItem.resolveTranslation(currentLocale())
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
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.Bold),
        color = Color.White,
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
    Text(
        text = magicalItem.getSubtitle(translation),
        style = MaterialTheme.typography.bodyMedium.copy(fontStyle = FontStyle.Italic),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = textPadding,
                end = textPadding,
                top = textPadding,
            ),
    )
}

@Preview
@Composable
private fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemRepository.getFirst()
    MagicalItemCard(item)
}
