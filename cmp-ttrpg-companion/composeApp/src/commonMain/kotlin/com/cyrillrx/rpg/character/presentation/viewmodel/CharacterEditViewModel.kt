package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.creature.domain.Ability
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
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

    val coercedValueEvent: SharedFlow<Int>
        field = MutableSharedFlow<Int>(extraBufferCapacity = 1)

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
        val coerced = level.coerceToValidCharacterLevel()
        if (coerced != level) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(level = coerced, editingField = null) }
    }

    fun saveBackground(background: String) {
        updateAndSave { it.copy(background = background.trim(), editingField = null) }
    }

    fun saveStrength(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(str = Ability(coerced, it.abilities.str.savingThrowProficiency)), editingField = null) }
    }

    fun saveDexterity(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(dex = Ability(coerced, it.abilities.dex.savingThrowProficiency)), editingField = null) }
    }

    fun saveConstitution(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(con = Ability(coerced, it.abilities.con.savingThrowProficiency)), editingField = null) }
    }

    fun saveIntelligence(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(int = Ability(coerced, it.abilities.int.savingThrowProficiency)), editingField = null) }
    }

    fun saveWisdom(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(wis = Ability(coerced, it.abilities.wis.savingThrowProficiency)), editingField = null) }
    }

    fun saveCharisma(value: Int) {
        val coerced = value.coerceToValidAbilityScore()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(abilities = it.abilities.copy(cha = Ability(coerced, it.abilities.cha.savingThrowProficiency)), editingField = null) }
    }

    fun saveArmorClass(value: Int) {
        val coerced = value.coerceToValidArmorClass()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(armorClass = coerced, editingField = null) }
    }

    fun saveMaxHitPoints(value: Int) {
        val coerced = value.coerceToValidMaxHitPoints()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(maxHitPoints = coerced, editingField = null) }
    }

    fun saveWalkSpeed(value: Int) {
        val coerced = value.coerceToValidWalkSpeed()
        if (coerced != value) coercedValueEvent.tryEmit(coerced)
        updateAndSave { it.copy(walkSpeed = coerced, editingField = null) }
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
