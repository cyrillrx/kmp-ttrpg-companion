package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.domain.StoredSortOrder
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
fun StoredSortHeader(
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
private fun PreviewStoredSortHeaderLight() {
    AppThemePreview(darkTheme = false) {
        StoredSortHeader(sortOrder = StoredSortOrder.LAST_MODIFIED, onSortOrderSelected = {})
    }
}

@Preview
@Composable
private fun PreviewStoredSortHeaderDark() {
    AppThemePreview(darkTheme = true) {
        StoredSortHeader(sortOrder = StoredSortOrder.NAME, onSortOrderSelected = {})
    }
}
