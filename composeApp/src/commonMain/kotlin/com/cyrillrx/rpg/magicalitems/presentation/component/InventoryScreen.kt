package com.cyrillrx.rpg.magicalitems.presentation.component

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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.cyrillrx.rpg.core.presentation.component.EmptySearch
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.HtmlText
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.component.SearchBar
import com.cyrillrx.rpg.magicalitems.domain.MagicalItem
import com.cyrillrx.rpg.magicalitems.presentation.MagicalItemListAction
import com.cyrillrx.rpg.magicalitems.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitems.presentation.viewmodel.InventoryViewModel
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.hint_search_magical_item

@Composable
fun InventoryScreen(
    viewModel: InventoryViewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    InventoryScreen(
        state = state,
        onAction = { action -> viewModel.onAction(action) },
    )
}

@Composable
fun InventoryScreen(
    state: MagicalItemListState,
    onAction: (MagicalItemListAction) -> Unit,
) {
    Column {
        SearchBar(
            hint = stringResource(Res.string.hint_search_magical_item),
            query = state.searchQuery,
            onQueryChanged = { onAction(MagicalItemListAction.OnSearchQueryChanged(it)) },
        )

        when (state) {
            is MagicalItemListState.Loading -> Loader()
            is MagicalItemListState.Empty -> EmptySearch(state.searchQuery)
            is MagicalItemListState.Error -> ErrorLayout(state.errorMessage)
            is MagicalItemListState.WithData -> MagicalItemsList(state.searchResults, onAction)
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

@Composable
private fun MagicalItemsList(
    magicalItems: List<MagicalItem>,
    onAction: (MagicalItemListAction) -> Unit,
) {
    LazyRow(modifier = Modifier.fillMaxSize()) {
        items(magicalItems) { item ->
            Box(modifier = Modifier.fillParentMaxSize()) {
                MagicalItemCard(item)
            }
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
