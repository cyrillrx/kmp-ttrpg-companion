package com.cyrillrx.rpg.dnd.inventory

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.common.theme.AppTheme
import com.cyrillrx.rpg.magicalitems.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitems.presentation.InventoryScreen

@Preview
@Composable
fun PreviewInventoryScreen() {
    val item = SampleMagicalItemsRepository().get()
    val items = listOf(item, item, item)
    AppTheme(darkTheme = false) {
        InventoryScreen(items, "") {}
    }
}
