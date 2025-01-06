package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ErrorLayout(errorMessage: String) {
    Text(
        text = errorMessage,
        color = MaterialTheme.colorScheme.onBackground,
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

@Preview
@Composable
private fun PreviewErrorLayoutLight() {
    AppThemePreview(darkTheme = false) {
        ErrorLayout("Dummy error message")
    }
}

@Preview
@Composable
private fun PreviewErrorLayoutDark() {
    AppThemePreview(darkTheme = true) {
        ErrorLayout("Dummy error message")
    }
}
