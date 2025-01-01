package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun ErrorLayout(errorMessage: String) {
    Text(
        text = errorMessage,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(spacingCommon),
    )
}

@Composable
fun ErrorLayout(errorMessage: StringResource) {
    ErrorLayout(stringResource(errorMessage))
}
