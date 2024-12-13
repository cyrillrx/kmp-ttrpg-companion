package com.cyrillrx.rpg.core.presentation.componenent

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.baseline_bookmark_24
import rpg_companion.composeapp.generated.resources.baseline_bookmark_border_24

@Composable
fun BookmarkButton(
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    val imageRes = if (checked) {
        Res.drawable.baseline_bookmark_24
    } else {
        Res.drawable.baseline_bookmark_border_24
    }

    Icon(
        imageVector = Icons.Default.Done,
//        imageVector = imageResource(resource = ),
        contentDescription = "",
        modifier = modifier,
    )
}
