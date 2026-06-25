package com.cyrillrx.rpg.character.presentation.component.tab

import androidx.compose.runtime.Composable
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.component.section.LanguagesRow
import com.cyrillrx.rpg.character.presentation.component.section.SheetDivider
import org.jetbrains.compose.resources.stringResource
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.label_languages

@Composable
internal fun ProfileTabContent(
    state: CharacterEditState.Loaded,
    onFieldTapped: (EditingField) -> Unit,
) {
    SheetDivider(stringResource(Res.string.label_languages))

    LanguagesRow(
        languages = state.character.languages,
        onTap = { onFieldTapped(EditingField.Languages) },
    )
}
