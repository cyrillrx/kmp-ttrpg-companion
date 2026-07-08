package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_back

@Composable
fun SimpleTopBar(
    titleResource: StringResource,
    onNavigateUpClicked: (() -> Unit)? = null,
    titleAlpha: Float = 1f,
    actions: @Composable RowScope.() -> Unit = {},
) {
    SimpleTopBar(stringResource(titleResource), onNavigateUpClicked, titleAlpha, actions)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleTopBar(
    title: String,
    onNavigateUpClicked: (() -> Unit)? = null,
    titleAlpha: Float = 1f,
    actions: @Composable RowScope.() -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                modifier = Modifier.graphicsLayer { alpha = titleAlpha },
            )
        },
        navigationIcon = {
            if (onNavigateUpClicked != null) {
                IconButton(onClick = onNavigateUpClicked) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(Res.string.btn_back),
                    )
                }
            }
        },
        actions = actions,
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
    )
}
