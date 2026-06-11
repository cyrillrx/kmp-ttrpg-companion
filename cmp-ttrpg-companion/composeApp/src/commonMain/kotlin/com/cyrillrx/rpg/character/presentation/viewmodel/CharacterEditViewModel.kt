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

    fun saveStrength(value: Int) = saveAbility(value, Abilities::strength) { coerced ->
        copy(strength = strength.copy(value = coerced))
    }

    fun saveDexterity(value: Int) = saveAbility(value, Abilities::dexterity) { coerced ->
        copy(dexterity = dexterity.copy(value = coerced))
    }

    fun saveConstitution(value: Int) = saveAbility(value, Abilities::constitution) { coerced ->
        copy(constitution = constitution.copy(value = coerced))
    }

    fun saveIntelligence(value: Int) = saveAbility(value, Abilities::intelligence) { coerced ->
        copy(intelligence = intelligence.copy(value = coerced))
    }

    fun saveWisdom(value: Int) = saveAbility(value, Abilities::wisdom) { coerced ->
        copy(wisdom = wisdom.copy(value = coerced))
    }

    fun saveCharisma(value: Int) = saveAbility(value, Abilities::charisma) { coerced ->
        copy(charisma = charisma.copy(value = coerced))
    }

    fun saveArmorClass(value: Int) = updateAndSave(value, Int::coerceToValidArmorClass) { coerced ->
        copy(character = character.copy(armorClass = coerced), editingField = null)
    }

    fun saveMaxHitPoints(value: Int) = updateAndSave(value, Int::coerceToValidMaxHitPoints) { coerced ->
        copy(character = character.copy(maxHitPoints = coerced), editingField = null)
    }

    fun saveWalkSpeed(value: Int) {
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
            val translations = character.translations.toMutableMap()
            val currentTranslation = translations[locale] ?: Character.Translation()
            val newTranslation = currentTranslation.copy(shortDescription = trimmed)
            if (newTranslation == Character.Translation()) {
                translations.remove(locale)
            } else {
                translations[locale] = newTranslation
            }
            copy(character = character.copy(translations = translations.toMap()), editingField = null)
        }
    }

    fun saveAlignment(alignment: Creature.Alignment) {
        updateAndSave { copy(character = character.copy(alignment = alignment), editingField = null) }
    }

    private fun saveAbility(candidate: Int, getAbility: Abilities.() -> Ability, update: Abilities.(Int) -> Abilities) {
        updateAndSave(candidate, Int::coerceToValidAbilityScore) { coerced ->
            val formerValue = character.abilities.getAbility().value
            if (formerValue == candidate || formerValue == coerced) {
                copy(editingField = null)
            } else {
                copy(character = character.copy(abilities = character.abilities.update(coerced)), editingField = null)
            }
        }
    }

    private fun updateEditState(applyEdit: Loaded.() -> Loaded) {
        state.update { current -> if (current is Loaded) current.applyEdit() else current }
    }

    private fun updateAndSave(candidate: Int, coerce: (Int) -> Int, applyEdit: Loaded.(Int) -> Loaded) {
        val coerced = coerce(candidate)
        if (coerced != candidate) coercedValueEvent.tryEmit(CoercedValue.Numeric(candidate, coerced))
        updateAndSave { applyEdit(coerced) }
    }

    private fun updateAndSave(applyEdit: Loaded.() -> Loaded) {
        updateEditState(applyEdit)
        viewModelScope.launch {
            val loaded = state.value as? Loaded ?: return@launch
            characterRepository.save(loaded.character)
        }
    }
}
