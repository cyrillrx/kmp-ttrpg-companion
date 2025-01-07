package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemsRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MagicalItemCardScreen(
    magicalItem: MagicalItem,
    onNavigateUpClicked: () -> Unit,
) {
    Scaffold { paddingValues ->
        MagicalItemCard(
            magicalItem = magicalItem,
            modifier = Modifier
                .clickable { onNavigateUpClicked() }
                .padding(paddingValues),
        )
    }
}

@Preview
@Composable
fun PreviewMagicalItemCardScreen() {
    val magicalItem = SampleMagicalItemsRepository().get()
    MagicalItemCardScreen(magicalItem, {})
}
