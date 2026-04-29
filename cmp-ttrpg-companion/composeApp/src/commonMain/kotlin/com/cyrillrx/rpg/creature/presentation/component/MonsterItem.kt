package com.cyrillrx.rpg.creature.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.core.presentation.component.HtmlText
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.spacingCommon
import com.cyrillrx.rpg.creature.data.SampleMonsterRepository
import com.cyrillrx.rpg.creature.domain.Monster
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun MonsterItem(
    monster: Monster,
    modifier: Modifier = Modifier,
) {
    val translation = monster.resolveTranslation(currentLocale())
    Column(modifier = modifier
        .background(MaterialTheme.colorScheme.background)
        .padding(spacingCommon)) {
        Text(
            text = translation?.name.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
        )

        MainStatLayout(
            monster = monster,
            modifier = Modifier.fillMaxWidth(),
        )

        val abilities = monster.abilities
        AbilitiesLayout(
            str = abilities.str.getValueWithModifier(),
            dex = abilities.dex.getValueWithModifier(),
            con = abilities.con.getValueWithModifier(),
            int = abilities.int.getValueWithModifier(),
            wis = abilities.wis.getValueWithModifier(),
            cha = abilities.cha.getValueWithModifier(),
        )

        HtmlText(text = translation?.description.orEmpty())
    }
}

@Preview
@Composable
private fun PreviewMonsterItemLight() {
    AppThemePreview(darkTheme = false) {
        MonsterItem(monster = SampleMonsterRepository.getFirst())
    }
}

@Preview
@Composable
private fun PreviewMonsterItemDark() {
    AppThemePreview(darkTheme = true) {
        MonsterItem(monster = SampleMonsterRepository.getFirst())
    }
}
