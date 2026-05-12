package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.presentation.CharacterPresetGalleryState
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rpg_companion.composeapp.generated.resources.Res
import rpg_companion.composeapp.generated.resources.error_while_loading_characters
import kotlin.coroutines.cancellation.CancellationException
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class CharacterPresetGalleryViewModel(
    private val router: CharacterRouter,
    private val pcPresetRepository: CharacterRepository,
    private val npcPresetRepository: CharacterRepository,
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    val state: StateFlow<CharacterPresetGalleryState>
        field = MutableStateFlow(CharacterPresetGalleryState())

    init {
        loadPresets()
    }

    fun onTabSelected(tabIndex: Int) {
        state.update { it.copy(selectedTabIndex = tabIndex) }
    }

    @OptIn(ExperimentalUuidApi::class)
    fun onPresetSelected(preset: Character) {
        viewModelScope.launch {
            characterRepository.save(preset.copy(id = Uuid.random().toString()))
            router.navigateUp()
        }
    }

    private fun loadPresets() {
        viewModelScope.launch {
            try {
                val pcPresets = pcPresetRepository.getAll(null)
                val npcPresets = npcPresetRepository.getAll(null)
                state.update {
                    it.copy(
                        body = CharacterPresetGalleryState.Body.WithData(
                            pcPresets = pcPresets,
                            npcPresets = npcPresets,
                        ),
                    )
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                state.update {
                    it.copy(body = CharacterPresetGalleryState.Body.Error(Res.string.error_while_loading_characters))
                }
            }
        }
    }
}
