package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.navigation.CharacterRouter
import com.cyrillrx.rpg.character.presentation.toCharacter
import com.cyrillrx.rpg.character.domain.coerceToValidAbilityScore
import com.cyrillrx.rpg.character.domain.coerceToValidArmorClass
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterLevel
import com.cyrillrx.rpg.character.domain.coerceToValidMaxHitPoints
import com.cyrillrx.rpg.character.domain.coerceToValidWalkSpeed
import com.cyrillrx.rpg.character.domain.defaultWalkSpeed
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

    val state: StateFlow<CharacterEditState>
        field = MutableStateFlow<CharacterEditState>(CharacterEditState.Loading)

    init {
        viewModelScope.launch {
            val character = characterRepository.get(characterId)
            originalCharacter = character
            state.value = if (character == null) CharacterEditState.NotFound(characterId) else Loaded.from(character)
        }
    }

    fun editField(field: EditingField) {
        updateEditState { it.copy(editingField = field) }
    }

    fun cancelEditing() {
        updateEditState { it.copy(editingField = null) }
    }

    fun saveName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            cancelEditing()
            return
        }
        updateAndSave { it.copy(name = trimmed, editingField = null) }
    }

    fun saveRace(race: Race) {
        updateAndSave { it.copy(race = race, walkSpeed = race.defaultWalkSpeed(), editingField = null) }
    }

    fun saveClass(clazz: Character.Class) {
        updateAndSave { it.copy(clazz = clazz, editingField = null) }
    }

    fun saveLevel(level: Int) {
        updateAndSave { it.copy(level = level.coerceToValidCharacterLevel(), editingField = null) }
    }

    fun saveBackground(background: String) {
        updateAndSave { it.copy(background = background.trim(), editingField = null) }
    }

    fun saveStrength(value: Int) {
        updateAndSave { it.copy(strength = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveDexterity(value: Int) {
        updateAndSave { it.copy(dexterity = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveConstitution(value: Int) {
        updateAndSave { it.copy(constitution = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveIntelligence(value: Int) {
        updateAndSave { it.copy(intelligence = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveWisdom(value: Int) {
        updateAndSave { it.copy(wisdom = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveCharisma(value: Int) {
        updateAndSave { it.copy(charisma = value.coerceToValidAbilityScore(), editingField = null) }
    }

    fun saveArmorClass(value: Int) {
        updateAndSave { it.copy(armorClass = value.coerceToValidArmorClass(), editingField = null) }
    }

    fun saveMaxHitPoints(value: Int) {
        updateAndSave { it.copy(maxHitPoints = value.coerceToValidMaxHitPoints(), editingField = null) }
    }

    fun saveWalkSpeed(value: Int) {
        updateAndSave { it.copy(walkSpeed = value.coerceToValidWalkSpeed(), editingField = null) }
    }

    fun saveLanguages(languages: List<Language>) {
        updateAndSave { it.copy(languages = languages, editingField = null) }
    }

    fun saveAlignment(alignment: Creature.Alignment) {
        updateAndSave { it.copy(alignment = alignment, editingField = null) }
    }

    private fun updateEditState(transform: (Loaded) -> Loaded) {
        state.update { current -> if (current is Loaded) transform(current) else current }
    }

    private fun updateAndSave(transform: (Loaded) -> Loaded) {
        val original = originalCharacter ?: return
        updateEditState(transform)
        viewModelScope.launch {
            val loaded = state.value as? Loaded ?: return@launch
            characterRepository.save(loaded.toCharacter(original))
        }
    }
}
