package com.cyrillrx.rpg.core.presentation.componenent

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon

@Composable
fun EmptySearch(searchQuery: String) {
    Text(
        text = "No results found for '$searchQuery'",
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}
