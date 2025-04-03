package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.core.presentation.component.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.ui.tooling.preview.Preview

private val borderStroke = spacingMedium
private val textPadding = spacingMedium

@Composable
fun MagicalItemCard(
    magicalItem: MagicalItem,
    modifier: Modifier = Modifier,
) {
    val color = magicalItem.getColor()
    Card(
        modifier = modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderStroke, color),
    ) {
        Column(Modifier.padding(borderStroke)) {
            Text(
                text = magicalItem.title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
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
                text = magicalItem.subtitle,
                fontSize = 14.sp,
                fontStyle = FontStyle.Italic,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = textPadding,
                        end = textPadding,
                        top = textPadding,
                    ),
            )
            HtmlText(
                text = magicalItem.description,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(textPadding),
            )
        }
    }
}

private fun MagicalItem.getColor(): Color {
    val weaponColor = Color(155, 11, 78)
    val armorColor = Color(0, 122, 179)
    val objectColor = Color(0, 179, 140)
    return when (type) {
        MagicalItem.Type.ARMOR -> armorColor
        MagicalItem.Type.POTION -> objectColor
        MagicalItem.Type.RING -> objectColor
        MagicalItem.Type.ROD -> weaponColor
        MagicalItem.Type.SCROLL -> objectColor
        MagicalItem.Type.STAFF -> weaponColor
        MagicalItem.Type.WAND -> objectColor
        MagicalItem.Type.WEAPON -> weaponColor
        MagicalItem.Type.WONDROUS_ITEM -> objectColor
        else -> objectColor
    }
}

@Preview
@Composable
private fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemsRepository().get()
    MagicalItemCard(item)
}
