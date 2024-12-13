package com.cyrillrx.rpg.core.presentation.preview

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cyrillrx.rpg.core.presentation.componenent.SearchBar

@Preview
@Composable
private fun SearchBarPreview() {
    MaterialTheme {
        SearchBar(
            hint = "Test hint",
            query = "Test Query",
            onQueryChanged = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
