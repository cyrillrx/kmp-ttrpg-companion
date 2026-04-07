package com.cyrillrx.rpg.magicalitem.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.component.ErrorLayout
import com.cyrillrx.rpg.core.presentation.component.Loader
import com.cyrillrx.rpg.core.presentation.state.DetailState
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.magicalitem.data.SampleMagicalItemRepository
import com.cyrillrx.rpg.magicalitem.domain.MagicalItem
import com.cyrillrx.rpg.magicalitem.presentation.MagicalItemAddToListProvider
import com.cyrillrx.rpg.magicalitem.presentation.navigation.MagicalItemRouter
import com.cyrillrx.rpg.magicalitem.presentation.viewmodel.MagicalItemDetailViewModel
import com.cyrillrx.rpg.userlist.data.SampleUserListRepository
import com.cyrillrx.rpg.userlist.presentation.AddToListProvider
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_add_to_list
import rpg_companion.composeapp.generated.resources.error_magical_item_not_found

@Composable
fun MagicalItemCardScreen(
    viewModel: MagicalItemDetailViewModel,
    router: MagicalItemRouter,
    addToListProvider: AddToListProvider<MagicalItem>,
) {
    val state by viewModel.state.collectAsState()
    when (val s = state) {
        DetailState.Loading -> Loader()
        is DetailState.NotFound -> ErrorLayout(stringResource(Res.string.error_magical_item_not_found, s.id))
        is DetailState.Found -> MagicalItemCardContent(
            magicalItem = s.item,
            onNavigateUpClicked = router::navigateUp,
            addToListProvider = addToListProvider,
        )
    }
}

@Composable
private fun MagicalItemCardContent(
    magicalItem: MagicalItem,
    onNavigateUpClicked: () -> Unit,
    addToListProvider: AddToListProvider<MagicalItem>,
) {
    var addToListItemId by remember { mutableStateOf<String?>(null) }

    Scaffold { paddingValues ->
        Column(modifier = Modifier.padding(paddingValues)) {
            MagicalItemCard(
                magicalItem = magicalItem,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onNavigateUpClicked() },
            )
            Button(
                onClick = { addToListItemId = magicalItem.id },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = spacingMedium),
            ) {
                Text(stringResource(Res.string.btn_add_to_list))
            }
        }
    }

    addToListItemId?.let { id ->
        addToListProvider.BottomSheet(
            entityId = id,
            onDismiss = { addToListItemId = null },
        )
    }
}

@Preview
@Composable
fun PreviewMagicalItemCardScreenLight() {
    PreviewMagicalItemCardScreen(darkTheme = false)
}

@Preview
@Composable
fun PreviewMagicalItemCardScreenDark() {
    PreviewMagicalItemCardScreen(darkTheme = true)
}

@Composable
private fun PreviewMagicalItemCardScreen(darkTheme: Boolean) {
    val magicalItem = SampleMagicalItemRepository.getFirst()
    val addToListProvider = MagicalItemAddToListProvider(SampleMagicalItemRepository(), SampleUserListRepository())
    AppThemePreview(darkTheme = darkTheme) {
        MagicalItemCardContent(magicalItem, onNavigateUpClicked = {}, addToListProvider = addToListProvider)
    }
}
