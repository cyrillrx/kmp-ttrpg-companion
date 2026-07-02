package com.cyrillrx.rpg.character.presentation.component.dialog

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.core.presentation.theme.iconButtonSize
import kotlinx.coroutines.delay

@Composable
internal fun DecrementIncrementRow(
    value: Int,
    minValue: Int,
    maxValue: Int,
    onDecrement: () -> Unit,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = modifier.fillMaxWidth(),
    ) {
        RepeatingIconButton(
            onClick = onDecrement,
            enabled = value > minValue,
        ) {
            Text("-", style = MaterialTheme.typography.headlineMedium)
        }
        Text(
            text = value.toString(),
            style = MaterialTheme.typography.displaySmall,
            fontWeight = FontWeight.Bold,
        )
        RepeatingIconButton(
            onClick = onIncrement,
            enabled = value < maxValue,
        ) {
            Text("+", style = MaterialTheme.typography.headlineMedium)
        }
    }
}

private const val REPEAT_INITIAL_DELAY_MS = 400L
private const val REPEAT_INTERVAL_MS = 80L

@Composable
private fun RepeatingIconButton(
    onClick: () -> Unit,
    enabled: Boolean,
    content: @Composable () -> Unit,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val currentOnClick by rememberUpdatedState(onClick)
    val currentEnabled by rememberUpdatedState(enabled)
    // Once the long-press repetition has fired, the click emitted on release must be swallowed.
    var consumedByRepeat by remember { mutableStateOf(false) }
    LaunchedEffect(isPressed) {
        if (!isPressed) return@LaunchedEffect
        consumedByRepeat = false
        delay(REPEAT_INITIAL_DELAY_MS)
        while (currentEnabled) {
            consumedByRepeat = true
            currentOnClick()
            delay(REPEAT_INTERVAL_MS)
        }
    }
    FilledTonalIconButton(
        onClick = { if (!consumedByRepeat) onClick() },
        enabled = enabled,
        interactionSource = interactionSource,
        modifier = Modifier.size(iconButtonSize),
        content = content,
    )
}
