package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_magical_item_not_found

@Composable
fun MagicalItemCardScreen(
    viewModel: MagicalItemDetailViewModel,
    onNavigateUpClicked: () -> Unit,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        DetailState.NotFound -> ErrorLayout(Res.string.error_magical_item_not_found)
        is DetailState.Found -> MagicalItemCardScreen(s.item, onNavigateUpClicked)
    }
}

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
    val magicalItem = SampleMagicalItemRepository().get()
    MagicalItemCardScreen(magicalItem, {})
}
