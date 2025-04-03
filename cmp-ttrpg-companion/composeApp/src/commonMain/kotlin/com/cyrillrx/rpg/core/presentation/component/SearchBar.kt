package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun SearchBar(
    hint: String,
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .widthIn(max = 400.dp)
        .minimumInteractiveComponentSize(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        shape = RoundedCornerShape(50),
        colors = TextFieldDefaults.colors(),
        placeholder = { Text(
            text = hint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        ) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
            )
        },
        singleLine = true,
        keyboardActions = KeyboardActions(onSearch = { keyboardController?.hide() }),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Search,
        ),
        trailingIcon = {
            AnimatedVisibility(visible = query.isNotBlank()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface,
                    )
                }
            }
        },
        modifier = modifier,
    )
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
        SearchBar(
            hint = "Test hint",
            query = "Test Query",
            onQueryChanged = {},
        )
    }
}
