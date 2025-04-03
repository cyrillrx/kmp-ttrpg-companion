package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.ability_label_cha
import rpg_companion.composeapp.generated.resources.ability_label_con
import rpg_companion.composeapp.generated.resources.ability_label_dex
import rpg_companion.composeapp.generated.resources.ability_label_int
import rpg_companion.composeapp.generated.resources.ability_label_str
import rpg_companion.composeapp.generated.resources.ability_label_wis

@Composable
fun AbilitiesLayout(
    str: String,
    dex: String,
    con: String,
    int: String,
    wis: String,
    cha: String,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = stringResource(Res.string.ability_label_str),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.ability_label_dex),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.ability_label_con),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.ability_label_int),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.ability_label_wis),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = stringResource(Res.string.ability_label_cha),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = str,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = dex,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = con,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = int,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = wis,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
            Text(
                text = cha,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun PreviewAbilitiesLayout() {
    AppThemePreview(darkTheme = false) {
        AbilitiesLayout(
            str = "10 (+0)",
            dex = "16 (+3)",
            con = "12 (+1)",
            int = "10 (+0)",
            wis = "18 (+4)",
            cha = "10 (+0)",
        )
    }
}
