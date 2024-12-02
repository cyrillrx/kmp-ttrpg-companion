package com.cyrillrx.rpg.bestiary.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.models.bestiary.Creature

@Composable
fun BestiaryScreen(creatures: List<Creature>, navigateToCreature: (Creature) -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(creatures) { creature ->

            CreatureItem(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clickable { navigateToCreature(creature) },
                creature,
            )
        }
    }
}
