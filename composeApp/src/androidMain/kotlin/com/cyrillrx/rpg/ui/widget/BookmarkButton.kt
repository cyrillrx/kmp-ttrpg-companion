package com.cyrillrx.rpg.ui.widget

import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.cyrillrx.rpg.R

@Composable
fun BookmarkButton(
    checked: Boolean,
    modifier: Modifier = Modifier,
) {
    val imageRes = if (checked) {
        R.drawable.baseline_bookmark_24
    } else {
        R.drawable.baseline_bookmark_border_24
    }

    Icon(
        imageVector = ImageVector.vectorResource(id = imageRes),
        contentDescription = "",
        modifier = modifier,
    )
}
