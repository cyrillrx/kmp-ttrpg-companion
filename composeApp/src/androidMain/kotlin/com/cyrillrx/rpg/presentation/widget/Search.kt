package com.cyrillrx.rpg.presentation.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Search(modifier: Modifier = Modifier, query: String, applyFilter: (String) -> Unit, label: @Composable (() -> Unit)? = null) {
    OutlinedTextField(
        value = query,
        onValueChange = applyFilter,
        label = label,
        modifier = modifier
            .padding(4.dp)
            .fillMaxWidth(),
    )
}
