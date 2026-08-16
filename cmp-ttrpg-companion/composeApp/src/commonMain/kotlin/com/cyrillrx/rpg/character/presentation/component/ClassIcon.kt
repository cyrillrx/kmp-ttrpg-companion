package com.cyrillrx.rpg.character.presentation.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import coil3.compose.AsyncImage
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.presentation.ClassIconState
import com.cyrillrx.rpg.character.presentation.resolveClassIconState
import org.jetbrains.compose.resources.ExperimentalResourceApi
import rpg_companion.composeapp.generated.resources.Res

@OptIn(ExperimentalResourceApi::class)
@Composable
internal fun ClassIcon(
    clazz: Character.Class,
    tint: Color,
    modifier: Modifier = Modifier,
) {
    val iconState by produceState<ClassIconState>(ClassIconState.Loading, clazz) {
        value = resolveClassIconState(clazz) { Res.readBytes(it) }
    }
    when (val s = iconState) {
        ClassIconState.Loading -> Unit
        is ClassIconState.Loaded -> AsyncImage(
            model = s.bytes,
            contentDescription = null,
            colorFilter = ColorFilter.tint(tint),
            modifier = modifier,
        )
        ClassIconState.Error -> Icon(
            imageVector = Icons.Filled.Warning,
            contentDescription = null,
            tint = tint,
            modifier = modifier,
        )
        ClassIconState.Unknown -> Icon(
            imageVector = Icons.Filled.QuestionMark,
            contentDescription = null,
            tint = tint,
            modifier = modifier,
        )
    }
}
