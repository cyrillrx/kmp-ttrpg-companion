package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.domain.StoredSortOrder
import com.cyrillrx.rpg.core.presentation.component.SortHeader
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.sort_last_modified
import rpg_companion.composeapp.generated.resources.sort_name

private val sortOrderOptions = listOf(
    StoredSortOrder.LAST_MODIFIED to Res.string.sort_last_modified,
    StoredSortOrder.NAME to Res.string.sort_name,
)

@Composable
fun CharacterSortHeader(
    sortOrder: StoredSortOrder,
    onSortOrderSelected: (StoredSortOrder) -> Unit,
    modifier: Modifier = Modifier,
) {
    SortHeader(
        selected = sortOrder,
        options = sortOrderOptions,
        onSelected = onSortOrderSelected,
        modifier = modifier,
    )
}

@Preview
@Composable
private fun PreviewCharacterSortHeaderLight() {
    AppThemePreview(darkTheme = false) {
        CharacterSortHeader(sortOrder = StoredSortOrder.LAST_MODIFIED, onSortOrderSelected = {})
    }
}

@Preview
@Composable
private fun PreviewCharacterSortHeaderDark() {
    AppThemePreview(darkTheme = true) {
        CharacterSortHeader(sortOrder = StoredSortOrder.NAME, onSortOrderSelected = {})
    }
}
