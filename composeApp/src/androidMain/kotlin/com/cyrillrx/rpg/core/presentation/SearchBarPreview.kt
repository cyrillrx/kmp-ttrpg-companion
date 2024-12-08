package com.cyrillrx.rpg.core.presentation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview
@Composable
private fun SearchBarPreview () {
    MaterialTheme {
        SearchBar(
            hint = "Test hint",
            query = "Test Query",
            onQueryChanged = {},
            onImeSearch = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
