package com.cyrillrx.rpg.dnd.inventory

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
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.api.inventory.ApiMagicalItem
import com.cyrillrx.rpg.presentation.theme.AppTheme
import com.cyrillrx.rpg.presentation.widget.Search

@Composable
fun InventoryScreen(magicalItems: List<ApiMagicalItem>, query: String, applyFilter: (String) -> Unit) {
    Column {
        Search(
            query = query,
            applyFilter = applyFilter,
        ) { Text(stringResource(id = R.string.spell_search_hint)) }

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
fun MagicalItemCard(item: ApiMagicalItem) {
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
            Text(
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
    val item = sampleMagicalItem()
    val items = listOf(item, item, item)
    AppTheme(darkTheme = true) {
        InventoryScreen(items, "Search") {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val item = sampleMagicalItem()
    val items = listOf(item, item, item)
    AppTheme(darkTheme = false) {
        InventoryScreen(items, "Search") {}
    }
}

@Preview
@Composable
fun PreviewMagicalItemCard() {
    val item = sampleMagicalItem()
    MagicalItemCard(item)
}

private fun ApiMagicalItem.getColor(): Color {
    val weaponColor = Color(155, 11, 78)
    val armorColor = Color(0, 122, 179)
    val objectColor = Color(0, 179, 140)
    return when (type) {
        ApiMagicalItem.Type.ARMOR -> armorColor
        ApiMagicalItem.Type.POTION -> objectColor
        ApiMagicalItem.Type.RING -> objectColor
        ApiMagicalItem.Type.ROD -> weaponColor
        ApiMagicalItem.Type.SCROLL -> objectColor
        ApiMagicalItem.Type.STAFF -> weaponColor
        ApiMagicalItem.Type.WAND -> objectColor
        ApiMagicalItem.Type.WEAPON -> weaponColor
        ApiMagicalItem.Type.WONDROUS_ITEM -> objectColor
        else -> objectColor
    }
}

private fun sampleMagicalItem(): ApiMagicalItem {
    val description =
        "Lorsque vous faites une attaque au corps à corps avec cette arme, vous pouvez prononcer son mot de commande : « Prompte mort à ceux qui m'ont fait du tort ». La cible de votre attaque devient votre ennemi juré jusqu'à ce qu'il meure ou jusqu'à l'aube sept jours plus tard.\n" +
            "Vous ne pouvez avoir qu'un seul ennemi juré à la fois. Quand votre ennemi juré meurt, vous pouvez en choisir un nouveau après la prochaine aube.\n" +
            "Lorsque vous effectuez une attaque au corps à corps contre votre ennemi juré avec cette arme, vous avez un avantage au jet d'attaque.\n" +
            "Si l'attaque touche, votre ennemi juré subit 3d6 dégâts tranchants supplémentaires.\n" +
            "Tant que votre ennemi juré est en vie, vous avez un désavantage aux jets d'attaque avec toutes les autres armes."
    return ApiMagicalItem(
        title = "Hache du serment",
        subtitle = "Arme (hache d'arme), unique (harmonisation exigée)",
        description = description,
        type = ApiMagicalItem.Type.WEAPON,
        rarety = ApiMagicalItem.Rarety.RARE,
        attunement = true,
    )
}
