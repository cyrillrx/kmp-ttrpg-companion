package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.coerceToValidAbilityScore
import com.cyrillrx.rpg.character.domain.coerceToValidArmorClass
import com.cyrillrx.rpg.character.domain.coerceToValidCharacterLevel
import com.cyrillrx.rpg.character.domain.coerceToValidMaxHitPoints
import com.cyrillrx.rpg.character.domain.coerceToValidWalkSpeedInFeet
import com.cyrillrx.rpg.character.domain.defaultWalkSpeed
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.CoercedValue
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.Creature
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterEditViewModel(
    private val characterId: String,
    private val characterRepository: CharacterRepository,
) : ViewModel() {

    val state: StateFlow<CharacterEditState>
        field = MutableStateFlow<CharacterEditState>(CharacterEditState.Loading)

    val coercedValueEvent: SharedFlow<CoercedValue>
        field = MutableSharedFlow<CoercedValue>(extraBufferCapacity = 1)

    init {
        viewModelScope.launch {
            val character = characterRepository.get(characterId)
            state.value = if (character == null) CharacterEditState.NotFound(characterId) else Loaded(character)
        }
    }

    fun editField(field: EditingField) {
        updateEditState { copy(editingField = field) }
    }

    fun cancelEditing() {
        updateEditState { copy(editingField = null) }
    }

    fun saveName(name: String) {
        val trimmed = name.trim()
        if (trimmed.isBlank()) {
            cancelEditing()
            return
        }
        updateAndSave { copy(character = character.copy(name = trimmed), editingField = null) }
    }

    fun saveRace(race: Race) {
        updateAndSave {
            copy(
                character = character.copy(race = race, speeds = character.speeds.copy(walk = race.defaultWalkSpeed())),
                editingField = null,
            )
        }
    }

    fun saveClass(clazz: Character.Class) {
        updateAndSave { copy(character = character.copy(clazz = clazz), editingField = null) }
    }

    fun saveLevel(level: Int) = updateAndSave(level, Int::coerceToValidCharacterLevel) { coerced ->
        copy(character = character.copy(level = coerced), editingField = null)
    }

    fun saveBackground(background: Background?) {
        updateAndSave { copy(character = character.copy(background = background), editingField = null) }
    }

    fun saveStrength(ability: Ability) = saveAbility(ability) { updated -> copy(strength = updated) }

    fun saveDexterity(ability: Ability) = saveAbility(ability) { updated -> copy(dexterity = updated) }

    fun saveConstitution(ability: Ability) = saveAbility(ability) { updated -> copy(constitution = updated) }

    fun saveIntelligence(ability: Ability) = saveAbility(ability) { updated -> copy(intelligence = updated) }

    fun saveWisdom(ability: Ability) = saveAbility(ability) { updated -> copy(wisdom = updated) }

    fun saveCharisma(ability: Ability) = saveAbility(ability) { updated -> copy(charisma = updated) }

    fun saveArmorClass(value: Int) = updateAndSave(value, Int::coerceToValidArmorClass) { coerced ->
        copy(character = character.copy(armorClass = coerced), editingField = null)
    }

    fun saveMaxHitPoints(value: Int) = updateAndSave(value, Int::coerceToValidMaxHitPoints) { coerced ->
        copy(character = character.copy(maxHitPoints = coerced), editingField = null)
    }

    fun saveWalkSpeed(value: Int) {
        if (state.value !is Loaded) return

        val coerced = value.coerceToValidWalkSpeedInFeet()
        if (coerced != value) coercedValueEvent.tryEmit(CoercedValue.Distance(value, coerced))
        updateAndSave {
            copy(character = character.copy(speeds = character.speeds.copy(walk = coerced)), editingField = null)
        }
    }

    fun saveLanguages(languages: List<Language>) {
        updateAndSave { copy(character = character.copy(languages = languages), editingField = null) }
    }

    fun saveShortDescription(description: String) {
        val trimmed = description.trim()
        val locale = currentLocale()
        updateAndSave {
            val cleared = character.translations
                .mapValues { (_, translation) -> translation.copy(shortDescription = "") }
                .filter { (_, translation) -> !translation.isEmpty() }
                .toMutableMap()
            if (trimmed.isNotBlank()) {
                val current = cleared[locale] ?: Character.Translation()
                cleared[locale] = current.copy(shortDescription = trimmed)
            }
            copy(character = character.copy(translations = cleared.toMap()), editingField = null)
        }
    }

    fun saveAlignment(alignment: Creature.Alignment) {
        updateAndSave { copy(character = character.copy(alignment = alignment), editingField = null) }
    }

    private fun saveAbility(ability: Ability, update: Abilities.(Ability) -> Abilities) {
        if (state.value !is Loaded) return

        val coerced = ability.value.coerceToValidAbilityScore()
        if (coerced != ability.value) coercedValueEvent.tryEmit(CoercedValue.Numeric(ability.value, coerced))
        val coercedAbility = ability.copy(value = coerced)
        updateAndSave {
            copy(character = character.copy(abilities = character.abilities.update(coercedAbility)), editingField = null)
        }
    }

    private fun updateEditState(applyEdit: Loaded.() -> Loaded) {
        state.update { current -> if (current is Loaded) current.applyEdit() else current }
    }

    private fun updateAndSave(candidate: Int, coerce: (Int) -> Int, applyEdit: Loaded.(Int) -> Loaded) {
        if (state.value !is Loaded) return

        val coerced = coerce(candidate)
        if (coerced != candidate) coercedValueEvent.tryEmit(CoercedValue.Numeric(candidate, coerced))
        updateAndSave { applyEdit(coerced) }
    }

    private fun updateAndSave(applyEdit: Loaded.() -> Loaded) {
        val characterBeforeEdit = (state.value as? Loaded)?.character ?: return

        updateEditState(applyEdit)
        viewModelScope.launch {
            val loaded = state.value as? Loaded ?: return@launch
            if (loaded.character == characterBeforeEdit) return@launch
            characterRepository.save(loaded.character)
        }
    }
}
