package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_back

@Composable
fun SearchBarWithBack(
    hint: String,
    query: String,
    onQueryChanged: (String) -> Unit,
    onNavigateUpClicked: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.statusBarsPadding(),
    ) {
        IconButton(onClick = { onNavigateUpClicked() }) {
            Icon(
                Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = stringResource(Res.string.btn_back),
            )
        }
        SearchBar(
            hint = hint,
            query = query,
            onQueryChanged = onQueryChanged,
        )
    }
}

@Preview
@Composable
private fun PreviewSearchBarLight() {
    PreviewSearchBar(darkTheme = false)
}

@Preview
@Composable
private fun PreviewSearchBarDark() {
    PreviewSearchBar(darkTheme = true)
}

@Composable
private fun PreviewSearchBar(darkTheme: Boolean) {
    AppThemePreview(darkTheme = darkTheme) {
        SearchBarWithBack(
            hint = "Test hint",
            query = "Test Query",
            onQueryChanged = {},
            onNavigateUpClicked = {},
        )
    }
}
