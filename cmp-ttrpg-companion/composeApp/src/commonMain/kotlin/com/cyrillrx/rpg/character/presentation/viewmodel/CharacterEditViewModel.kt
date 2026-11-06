package com.cyrillrx.rpg.character.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cyrillrx.core.data.ExportFile
import com.cyrillrx.rpg.app.APP_VERSION
import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.data.toExportFile
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.ClassLevels
import com.cyrillrx.rpg.character.domain.D20Check
import com.cyrillrx.rpg.character.domain.HitPointAdjustment
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.domain.adjustHitPoints
import com.cyrillrx.rpg.character.domain.coerceToValidAbilityScore
import com.cyrillrx.rpg.character.domain.coerceToValidWalkSpeedInFeet
import com.cyrillrx.rpg.character.domain.defaultWalkSpeed
import com.cyrillrx.rpg.character.domain.modifierFor
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.CoercedValue
import com.cyrillrx.rpg.character.presentation.SheetRoll
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.coerceToValidArmorClass
import com.cyrillrx.rpg.dice.domain.Die
import com.cyrillrx.rpg.dice.domain.DieRoll
import com.cyrillrx.rpg.dice.domain.RollContext
import com.cyrillrx.rpg.dice.domain.RollHistoryRepository
import com.cyrillrx.rpg.dice.domain.RollPool
import com.cyrillrx.rpg.dice.domain.rollWithAdvantage
import com.cyrillrx.rpg.dice.domain.rollWithDisadvantage
import com.cyrillrx.rpg.dice.presentation.RollRecorder
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlin.random.Random
import kotlin.time.Clock

class CharacterEditViewModel(
    private val characterId: String,
    private val characterRepository: CharacterRepository,
    historyRepository: RollHistoryRepository,
    private val appVersion: String = APP_VERSION,
    private val clock: Clock = Clock.System,
    private val timeZone: TimeZone = TimeZone.currentSystemDefault(),
    private val random: Random = Random.Default,
    adjustmentCount: Int = 0,
) : ViewModel() {

    val state: StateFlow<CharacterEditState>
        field = MutableStateFlow<CharacterEditState>(CharacterEditState.Loading)

    val coercedValueEvent: SharedFlow<CoercedValue>
        field = MutableSharedFlow<CoercedValue>(extraBufferCapacity = 1)

    val exportEvent: SharedFlow<ExportFile>
        field = MutableSharedFlow<ExportFile>(extraBufferCapacity = 1)

    val pendingAdjustmentCount: StateFlow<Int>
        field = MutableStateFlow(adjustmentCount)

    private val recorder = RollRecorder(historyRepository)

    init {
        viewModelScope.launch {
            val character = characterRepository.get(characterId)
            state.value = if (character == null) CharacterEditState.NotFound(characterId) else Loaded(character)
        }
    }

    override fun onCleared() = recorder.close()

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

    fun saveClasses(classes: ClassLevels, primaryClass: Character.Class) {
        updateAndSave {
            copy(character = character.copy(classes = classes, primaryClass = primaryClass), editingField = null)
        }
    }

    fun saveBackground(background: Background?) {
        updateAndSave { copy(character = character.copy(background = background), editingField = null) }
    }

    fun saveStrength(ability: AbilityScore) = saveAbility(ability) { updated -> copy(strength = updated) }

    fun saveDexterity(ability: AbilityScore) = saveAbility(ability) { updated -> copy(dexterity = updated) }

    fun saveConstitution(ability: AbilityScore) = saveAbility(ability) { updated -> copy(constitution = updated) }

    fun saveIntelligence(ability: AbilityScore) = saveAbility(ability) { updated -> copy(intelligence = updated) }

    fun saveWisdom(ability: AbilityScore) = saveAbility(ability) { updated -> copy(wisdom = updated) }

    fun saveCharisma(ability: AbilityScore) = saveAbility(ability) { updated -> copy(charisma = updated) }

    fun saveArmorClass(value: Int) = updateAndSave(value, Int::coerceToValidArmorClass) { coerced ->
        copy(character = character.copy(armorClass = coerced), editingField = null)
    }

    fun saveHitPoints(adjustment: HitPointAdjustment, amount: Int) {
        updateAndSave {
            copy(character = character.adjustHitPoints(adjustment, amount), editingField = null)
        }
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

    fun saveSkills(skills: Skills) {
        updateAndSave { copy(character = character.copy(skills = skills), editingField = null) }
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

    fun exportCharacter() {
        val character = (state.value as? Loaded)?.character ?: return
        exportEvent.tryEmit(character.toExportFile(appVersion, clock.now(), timeZone))
    }

    fun onAdjustmentsReported() {
        pendingAdjustmentCount.value = 0
    }

    fun rollCheck(check: D20Check) = roll(check) { Die.D20.roll(random) }

    fun rerollCheck() = rollAgain { Die.D20.roll(random) }

    fun rollCheckWithAdvantage() = rollAgain { Die.D20.rollWithAdvantage(random) }

    fun rollCheckWithDisadvantage() = rollAgain { Die.D20.rollWithDisadvantage(random) }

    fun dismissRoll() = updateEditState { copy(sheetRoll = null) }

    private fun rollAgain(rollD20: () -> DieRoll) {
        val check = currentCheck() ?: return

        roll(check, rollD20)
    }

    private fun currentCheck(): D20Check? = (state.value as? Loaded)?.sheetRoll?.check

    private fun roll(check: D20Check, rollD20: () -> DieRoll) {
        val loaded = state.value as? Loaded ?: return

        val pool = RollPool(listOf(rollD20()), loaded.character.modifierFor(check))
        val rollCount = (loaded.sheetRoll?.rollCount ?: 0) + 1
        updateEditState { copy(sheetRoll = SheetRoll(check, pool, rollCount)) }
        // TODO(#370): show this context in the roll history.
        recorder.record(pool, RollContext(characterId, check))
    }

    private fun saveAbility(ability: AbilityScore, update: Abilities.(AbilityScore) -> Abilities) {
        if (state.value !is Loaded) return

        val coerced = ability.value.coerceToValidAbilityScore()
        if (coerced != ability.value) coercedValueEvent.tryEmit(CoercedValue.Numeric(ability.value, coerced))
        val coercedAbility = ability.copy(value = coerced)
        updateAndSave {
            val updatedCharacter = character.copy(abilities = character.abilities.update(coercedAbility))
            copy(character = updatedCharacter, editingField = null)
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
        val editedCharacter = (state.value as? Loaded)?.character ?: return
        if (editedCharacter == characterBeforeEdit) return

        viewModelScope.launch {
            characterRepository.save(editedCharacter)
        }
    }
}
