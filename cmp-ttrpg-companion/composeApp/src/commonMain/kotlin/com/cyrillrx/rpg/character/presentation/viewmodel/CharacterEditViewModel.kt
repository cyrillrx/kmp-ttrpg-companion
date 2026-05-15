package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.EditingField
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.toCharacter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterEditViewModel(
    private val characterId: String,
    private val router: CharacterRouter,
    private val characterRepository: CharacterRepository,
) : ViewModel() {
    private var originalCharacter: Character? = null

    val state: StateFlow<CharacterEditState?>
        field = MutableStateFlow(null)

    init {
        viewModelScope.launch {
            val character = characterRepository.get(characterId)
            originalCharacter = character
            if (character != null) state.value = CharacterEditState.from(character)
        }
    }

    fun onFieldTapped(field: EditingField) {
        state.update { it?.copy(editingField = field) }
    }

    fun onDialogDismissed() {
        state.update { it?.copy(editingField = null) }
    }

    fun onNameConfirmed(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            onDialogDismissed()
            return
        }
        updateAndSave { it.copy(name = trimmed, editingField = null) }
    }

    fun onRaceConfirmed(race: Race) {
        updateAndSave { it.copy(race = race, editingField = null) }
    }

    fun onClassConfirmed(clazz: Character.Class) {
        updateAndSave { it.copy(clazz = clazz, editingField = null) }
    }

    fun onLevelConfirmed(level: Int) {
        updateAndSave { it.copy(level = level.coerceAtLeast(1), editingField = null) }
    }

    fun onBackgroundConfirmed(background: String) {
        updateAndSave { it.copy(background = background.trim(), editingField = null) }
    }

    fun onStrConfirmed(value: Int) {
        updateAndSave { it.copy(str = value.coerceIn(1, 30), editingField = null) }
    }

    fun onDexConfirmed(value: Int) {
        updateAndSave { it.copy(dex = value.coerceIn(1, 30), editingField = null) }
    }

    fun onConConfirmed(value: Int) {
        updateAndSave { it.copy(con = value.coerceIn(1, 30), editingField = null) }
    }

    fun onIntelligenceConfirmed(value: Int) {
        updateAndSave { it.copy(intelligence = value.coerceIn(1, 30), editingField = null) }
    }

    fun onWisConfirmed(value: Int) {
        updateAndSave { it.copy(wis = value.coerceIn(1, 30), editingField = null) }
    }

    fun onChaConfirmed(value: Int) {
        updateAndSave { it.copy(cha = value.coerceIn(1, 30), editingField = null) }
    }

    fun onArmorClassConfirmed(value: Int) {
        updateAndSave { it.copy(armorClass = value.coerceAtLeast(0), editingField = null) }
    }

    fun onMaxHitPointsConfirmed(value: Int) {
        updateAndSave { it.copy(maxHitPoints = value.coerceAtLeast(1), editingField = null) }
    }

    fun onWalkSpeedConfirmed(value: Int?) {
        updateAndSave { it.copy(walkSpeed = value?.coerceAtLeast(0), editingField = null) }
    }

    fun onLanguagesConfirmed(languages: List<Language>) {
        updateAndSave { it.copy(languages = languages, editingField = null) }
    }

    private fun updateAndSave(transform: (CharacterEditState) -> CharacterEditState) {
        val original = originalCharacter ?: return
        state.update { it?.let(transform) }
        viewModelScope.launch {
            state.value?.let { characterRepository.save(it.toCharacter(original)) }
        }
    }
}
