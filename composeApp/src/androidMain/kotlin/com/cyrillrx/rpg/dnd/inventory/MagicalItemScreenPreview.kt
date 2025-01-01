package com.cyrillrx.rpg.dnd.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemCard
import com.cyrillrx.rpg.magicalitem.presentation.component.MagicalItemListScreen

private val stateWithSampleData = MagicalItemListState(
    searchQuery = "",
    body = MagicalItemListState.Body.WithData(SampleMagicalItemsRepository().getAll()),
)

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    AppTheme(darkTheme = true) {
        MagicalItemListScreen(stateWithSampleData, {}, {})
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    AppTheme(darkTheme = false) {
        MagicalItemListScreen(stateWithSampleData, {}, {})
    }
}

@Preview
@Composable
fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemsRepository().get()
    MagicalItemCard(item)
}
