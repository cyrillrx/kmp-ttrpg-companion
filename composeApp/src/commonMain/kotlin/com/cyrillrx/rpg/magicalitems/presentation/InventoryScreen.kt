package com.cyrillrx.rpg.magicalitems.presentation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.cyrillrx.rpg.core.presentation.HtmlText
import com.cyrillrx.rpg.core.presentation.SearchBar
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.magicalitems.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitems.domain.MagicalItem
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.spell_search_hint

@Composable
fun InventoryScreen(inventoryViewModel: InventoryViewModel) {
    InventoryScreen(
        magicalItems = inventoryViewModel.magicalItems,
        query = inventoryViewModel.query,
        applyFilter = inventoryViewModel::applyFilter,
    )
}

@Composable
fun InventoryScreen(magicalItems: List<MagicalItem>, query: String, applyFilter: (String) -> Unit) {
    Column {
        SearchBar(
            hint = stringResource(Res.string.spell_search_hint),
            query = query,
            onQueryChanged = applyFilter,
            onImeSearch = {},
        )

        LazyRow(modifier = Modifier.fillMaxSize()) {
            items(magicalItems) { item ->
                Box(modifier = Modifier.fillParentMaxSize()) {
                    MagicalItemCard(item)
                }
            }
        }
    }
}

private val borderStroke = 8.dp
private val textPadding = 8.dp

@Composable
fun MagicalItemCard(item: MagicalItem) {
    val color = item.getColor()
    Card(
        modifier = Modifier
            .padding(4.dp)
            .fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(borderStroke, color),
    ) {
        Column(Modifier.padding(borderStroke)) {
            Text(
                text = item.title,
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
                text = item.subtitle,
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
                text = item.description,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(textPadding),
            )
        }
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    val item = SampleMagicalItemsRepository().get()
    val items = listOf(item, item, item)
    AppTheme(darkTheme = true) {
        InventoryScreen(items, "Search") {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val item = SampleMagicalItemsRepository().get()
    val items = listOf(item, item, item)
    AppTheme(darkTheme = false) {
        InventoryScreen(items, "Search") {}
    }
}

@Preview
@Composable
fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemsRepository().get()
    MagicalItemCard(item)
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
