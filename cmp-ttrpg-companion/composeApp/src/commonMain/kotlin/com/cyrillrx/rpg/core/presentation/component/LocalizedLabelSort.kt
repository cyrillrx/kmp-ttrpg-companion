package com.cyrillrx.rpg.core.presentation.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.cyrillrx.core.domain.sortedByLocalizedName
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun <T> List<T>.sortedByLocalizedName(labelOf: (T) -> StringResource): List<T> {
    val localizedNames = associateWith { stringResource(labelOf(it)) }
    return remember(localizedNames) {
        sortedByLocalizedName { localizedNames.getValue(it) }
    }
}
