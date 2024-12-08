package com.cyrillrx.rpg.core.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
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
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import com.cyrillrx.rpg.core.presentation.theme.Purple700

@Composable
fun SearchBar(
    hint: String,
    query: String,
    onQueryChanged: (String) -> Unit,
    onImeSearch: () -> Unit,
    modifier: Modifier = Modifier,
) {
    CompositionLocalProvider(
        LocalTextSelectionColors provides TextSelectionColors(
            handleColor = Purple700,
            backgroundColor = Purple700,
        ),
    ) {

        OutlinedTextField(
            value = query,
            onValueChange = onQueryChanged,
            shape = RoundedCornerShape(50),
            colors = TextFieldDefaults.colors(),
            placeholder = { Text(text = hint) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                )
            },
            singleLine = true,
            keyboardActions = KeyboardActions(onSearch = { onImeSearch() }),
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
            modifier = modifier
                .background(
                    shape = RoundedCornerShape(50),
                    color = Purple700,
                )
                .minimumInteractiveComponentSize(),
        )
    }
}
