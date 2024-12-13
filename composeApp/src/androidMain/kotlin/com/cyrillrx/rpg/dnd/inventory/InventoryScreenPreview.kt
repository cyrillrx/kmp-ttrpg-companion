package com.cyrillrx.rpg.dnd.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.theme.AppTheme
import com.cyrillrx.rpg.magicalitems.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitems.presentation.MagicalItemListState
import com.cyrillrx.rpg.magicalitems.presentation.component.InventoryScreen
import com.cyrillrx.rpg.magicalitems.presentation.component.MagicalItemCard

@Preview
@Composable
fun PreviewSpellBookScreenDark() {
    val state = MagicalItemListState.WithData("", SampleMagicalItemsRepository().getAll())
    AppTheme(darkTheme = true) {
        InventoryScreen(state) {}
    }
}

@Preview
@Composable
fun PreviewSpellBookScreenLight() {
    val state = MagicalItemListState.WithData("", SampleMagicalItemsRepository().getAll())
    AppTheme(darkTheme = false) {
        InventoryScreen(state) {}
    }
}

@Preview
@Composable
fun PreviewMagicalItemCard() {
    val item = SampleMagicalItemsRepository().get()
    MagicalItemCard(item)
}
