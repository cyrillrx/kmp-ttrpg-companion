package com.cyrillrx.rpg.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Search(query: String, applyFilter: (String) -> Unit, label: @Composable (() -> Unit)? = null) {
    OutlinedTextField(
        value = query,
        onValueChange = applyFilter,
        label = label,
        modifier = Modifier
            .padding(4.dp)
            .fillMaxWidth(),
    )
}