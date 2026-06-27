package com.cyrillrx.rpg.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.presentation.component.CharacterCreateActions
import com.cyrillrx.rpg.character.presentation.component.CharacterListItem
import com.cyrillrx.rpg.core.presentation.theme.AppThemePreview
import com.cyrillrx.rpg.core.presentation.theme.iconSizeLarge
import com.cyrillrx.rpg.core.presentation.theme.spacingMedium
import com.cyrillrx.rpg.core.presentation.theme.spacingSmall
import com.cyrillrx.rpg.home.presentation.navigation.HomeRouter
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.btn_character_sheets
import rpg_companion.composeapp.generated.resources.btn_see_all

@Composable
fun CharacterPreviewSection(
    state: HomeState,
    router: HomeRouter,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(spacingSmall),
        modifier = modifier,
    ) {
        SectionHeaderWithAction(
            title = stringResource(Res.string.btn_character_sheets),
            actionLabel = stringResource(Res.string.btn_see_all),
            onActionClick = router::openCharacterSheetList,
        )

        CharacterCreateActions(
            onNewCharacterClicked = router::openCreateCharacter,
            onQuickCreateClicked = router::openPresetGallery,
        )

        when (val body = state.body) {
            is HomeState.Body.Loading ->
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth().padding(spacingMedium),
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(iconSizeLarge))
                }

            is HomeState.Body.Error ->
                Text(
                    text = stringResource(body.errorMessage),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(vertical = spacingMedium),
                )

            is HomeState.Body.WithData ->
                body.characters.forEach { character ->
                    CharacterListItem(
                        character = character,
                        onClick = { router.openCharacterDetail(character) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
        }
    }
}

@Composable
private fun CharacterPreviewSectionPreview() {
    CharacterPreviewSection(
        state = HomeState(body = HomeState.Body.WithData(SampleCharacterRepository.getAll())),
        router = PreviewHomeRouter,
    )
}

@Preview
@Composable
private fun PreviewCharacterPreviewSectionLight() {
    AppThemePreview(darkTheme = false) { CharacterPreviewSectionPreview() }
}

@Preview
@Composable
private fun PreviewCharacterPreviewSectionDark() {
    AppThemePreview(darkTheme = true) { CharacterPreviewSectionPreview() }
}
