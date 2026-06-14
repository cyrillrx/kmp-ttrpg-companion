package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.app.currentLocale
import com.cyrillrx.rpg.character.data.RamCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Background
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.character.presentation.CoercedValue
import com.cyrillrx.rpg.creature.domain.AbilityScore
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.Proficiency
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertIs
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterEditViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private val fighter = SampleCharacterRepository.humanFighter()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private suspend fun repoWithFighter(): RamCharacterRepository {
        val repo = RamCharacterRepository()
        repo.save(fighter)
        return repo
    }

    private fun buildViewModel(
        characterId: String = fighter.id,
        repo: CharacterRepository,
    ) = CharacterEditViewModel(characterId, repo)

    private class SaveCountingRepository(private val delegate: CharacterRepository) : CharacterRepository by delegate {
        var saveCount = 0
            private set

        override suspend fun save(character: Character) {
            saveCount++
            delegate.save(character)
        }
    }

    // ─── Init ──────────────────────────────────────────────────────────────────

    @Test
    fun `state is Loading before repository load completes`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        assertIs<CharacterEditState.Loading>(viewModel.state.value)
    }

    @Test
    fun `state is Loaded after load`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(fighter.name, loaded.character.name)
    }

    @Test
    fun `state is NotFound when character is not found`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(characterId = "unknown-id", repo = repoWithFighter())
        advanceUntilIdle()
        val notFound = assertIs<CharacterEditState.NotFound>(viewModel.state.value)
        assertEquals("unknown-id", notFound.characterId)
    }

    // ─── Editing field ─────────────────────────────────────────────────────────

    @Test
    fun `editField sets editingField`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.editField(EditingField.Race)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(EditingField.Race, loaded.editingField)
    }

    @Test
    fun `cancelEditing clears editingField`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.editField(EditingField.Race)
        viewModel.cancelEditing()
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertNull(loaded.editingField)
    }

    // ─── saveName ──────────────────────────────────────────────────────────────

    @Test
    fun `saveName with blank input does not update name`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveName("   ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(fighter.name, loaded.character.name)
    }

    @Test
    fun `saveName trims whitespace`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveName("  Aria  ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("Aria", loaded.character.name)
    }

    // ─── saveBackground ────────────────────────────────────────────────────────

    @Test
    fun `saveBackground sets background value`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveBackground(Background.SOLDIER)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(Background.SOLDIER, loaded.character.background)
    }

    @Test
    fun `saveBackground with null clears background`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveBackground(null)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertNull(loaded.character.background)
    }

    // ─── Numeric field coercion ─────────────────────────────────────────────────

    @Test
    fun `saveLevel coerces to minimum 1`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveLevel(0)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(1, loaded.character.level)
    }

    @Test
    fun `saveLevel with unchanged value closes dialog without persisting`() = runTest(testDispatcher) {
        val repo = SaveCountingRepository(repoWithFighter())
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.editField(EditingField.Level)
        viewModel.saveLevel(fighter.level)
        advanceUntilIdle()
        assertEquals(0, repo.saveCount)
        assertNull(assertIs<CharacterEditState.Loaded>(viewModel.state.value).editingField)
    }

    @Test
    fun `saveArmorClass with unchanged value closes dialog without persisting`() = runTest(testDispatcher) {
        val repo = SaveCountingRepository(repoWithFighter())
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.editField(EditingField.ArmorClass)
        viewModel.saveArmorClass(fighter.armorClass)
        advanceUntilIdle()
        assertEquals(0, repo.saveCount)
        assertNull(assertIs<CharacterEditState.Loaded>(viewModel.state.value).editingField)
    }

    @Test
    fun `saveStrength coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveStrength(AbilityScore(0))
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.strength.value)
        viewModel.saveStrength(AbilityScore(31))
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.strength.value)
    }

    @Test
    fun `saveStrength persists saving throw proficiency`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveStrength(AbilityScore(16, Proficiency.PROFICIENT))
        val strength = assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.strength
        assertEquals(16, strength.value)
        assertEquals(Proficiency.PROFICIENT, strength.savingThrowProficiency)
    }

    @Test
    fun `saveStrength with unchanged ability closes dialog without persisting`() = runTest(testDispatcher) {
        val repo = SaveCountingRepository(repoWithFighter())
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.editField(EditingField.Strength)
        viewModel.saveStrength(fighter.abilities.strength)
        advanceUntilIdle()
        assertEquals(0, repo.saveCount)
        assertNull(assertIs<CharacterEditState.Loaded>(viewModel.state.value).editingField)
    }

    @Test
    fun `saveDexterity coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveDexterity(AbilityScore(0))
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.dexterity.value)
        viewModel.saveDexterity(AbilityScore(31))
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.dexterity.value)
    }

    @Test
    fun `saveConstitution coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveConstitution(AbilityScore(0))
        assertEquals(
            expected = 1,
            actual = assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.constitution.value,
        )
        viewModel.saveConstitution(AbilityScore(31))
        assertEquals(
            expected = 30,
            actual = assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.constitution.value,
        )
    }

    @Test
    fun `saveIntelligence coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveIntelligence(AbilityScore(0))
        assertEquals(
            expected = 1,
            actual = assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.intelligence.value,
        )
        viewModel.saveIntelligence(AbilityScore(31))
        assertEquals(
            expected = 30,
            actual = assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.intelligence.value,
        )
    }

    @Test
    fun `saveWisdom coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveWisdom(AbilityScore(0))
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.wisdom.value)
        viewModel.saveWisdom(AbilityScore(31))
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.wisdom.value)
    }

    @Test
    fun `saveCharisma coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveCharisma(AbilityScore(0))
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.charisma.value)
        viewModel.saveCharisma(AbilityScore(31))
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).character.abilities.charisma.value)
    }

    @Test
    fun `saveArmorClass coerces to minimum 0`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveArmorClass(-1)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(0, loaded.character.armorClass)
    }

    @Test
    fun `saveMaxHitPoints coerces to minimum 1`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveMaxHitPoints(0)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(1, loaded.character.maxHitPoints)
    }

    // ─── coercedValueEvent ────────────────────────────────────────────────────

    @Test
    fun `coercedValueEvent emits Numeric when level is out of range`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val events = mutableListOf<CoercedValue>()
        val job = launch { viewModel.coercedValueEvent.collect { events.add(it) } }
        advanceUntilIdle()
        viewModel.saveLevel(25) // max is 20, coerced to 20
        advanceUntilIdle()
        assertEquals(listOf<CoercedValue>(CoercedValue.Numeric(25, 20)), events)
        job.cancel()
    }

    @Test
    fun `coercedValueEvent does not emit when input is already valid`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val events = mutableListOf<CoercedValue>()
        val job = launch { viewModel.coercedValueEvent.collect { events.add(it) } }
        advanceUntilIdle()
        viewModel.saveLevel(5) // valid, no coercion
        advanceUntilIdle()
        assertTrue(events.isEmpty())
        job.cancel()
    }

    @Test
    fun `coercedValueEvent emits Distance when walk speed is invalid`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val events = mutableListOf<CoercedValue>()
        val job = launch { viewModel.coercedValueEvent.collect { events.add(it) } }
        advanceUntilIdle()
        viewModel.saveWalkSpeed(27) // not a multiple of 5, coerced to 25
        advanceUntilIdle()
        assertEquals(listOf<CoercedValue>(CoercedValue.Distance(27, 25)), events)
        job.cancel()
    }

    // ─── saveRace ─────────────────────────────────────────────────────────────

    @Test
    fun `saveRace resets walk speed to race default`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveRace(Race.DWARF)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(25, loaded.character.speeds.walk)
    }

    // ─── Persistence ───────────────────────────────────────────────────────────

    @Test
    fun `saveName persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveName("Thorin")
        advanceUntilIdle()
        assertEquals("Thorin", repo.get(fighter.id)?.name)
    }

    @Test
    fun `saveRace persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveRace(Race.ELF)
        advanceUntilIdle()
        assertEquals(Race.ELF, repo.get(fighter.id)?.race)
    }

    @Test
    fun `saveClass persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveClass(Character.Class.WIZARD)
        advanceUntilIdle()
        assertEquals(Character.Class.WIZARD, repo.get(fighter.id)?.clazz)
    }

    @Test
    fun `saveAlignment persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveAlignment(Creature.Alignment.CHAOTIC_EVIL)
        advanceUntilIdle()
        assertEquals(Creature.Alignment.CHAOTIC_EVIL, repo.get(fighter.id)?.alignment)
    }

    @Test
    fun `saveLanguages persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveLanguages(listOf(Language.ELVISH, Language.DRACONIC))
        advanceUntilIdle()
        assertEquals(listOf(Language.ELVISH, Language.DRACONIC), repo.get(fighter.id)?.languages)
    }

    // ─── saveShortDescription ─────────────────────────────────────────────────

    private val otherLocale get() = if (currentLocale() == "fr") "en" else "fr"

    @Test
    fun `saveShortDescription saves description to current locale`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveShortDescription("Hero of the realm")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("Hero of the realm", loaded.character.resolveTranslation(currentLocale())?.shortDescription)
    }

    @Test
    fun `saveShortDescription trims whitespace`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveShortDescription("  Hero  ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("Hero", loaded.character.resolveTranslation(currentLocale())?.shortDescription)
    }

    @Test
    fun `saveShortDescription clears editingField`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.editField(EditingField.ShortDescription)
        viewModel.saveShortDescription("Hero")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertNull(loaded.editingField)
    }

    @Test
    fun `saveShortDescription with blank input results in empty translations`() = runTest(testDispatcher) {
        val repo = RamCharacterRepository()
        repo.save(fighter.copy(translations = mapOf(otherLocale to Character.Translation(shortDescription = "Hero"))))
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveShortDescription("  ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertTrue(loaded.character.translations.isEmpty())
    }

    @Test
    fun `saveShortDescription removes other locale translation when it becomes empty`() = runTest(testDispatcher) {
        val repo = RamCharacterRepository()
        repo.save(fighter.copy(translations = mapOf(otherLocale to Character.Translation(shortDescription = "Hero"))))
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveShortDescription("Hero")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertFalse(loaded.character.translations.containsKey(otherLocale))
    }

    @Test
    fun `saveShortDescription preserves other translation fields when clearing short description`() = runTest(
        testDispatcher,
    ) {
        val repo = RamCharacterRepository()
        repo.save(
            fighter.copy(
                translations = mapOf(
                    otherLocale to Character.Translation(shortDescription = "Hero", description = "A brave warrior"),
                ),
            ),
        )
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveShortDescription("Hero")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("", loaded.character.translations[otherLocale]?.shortDescription)
        assertEquals("A brave warrior", loaded.character.translations[otherLocale]?.description)
    }

    @Test
    fun `saveShortDescription persists to repository`() = runTest(testDispatcher) {
        val repo = repoWithFighter()
        val viewModel = buildViewModel(repo = repo)
        advanceUntilIdle()
        viewModel.saveShortDescription("Thorin the Brave")
        advanceUntilIdle()
        assertEquals("Thorin the Brave", repo.get(fighter.id)?.resolveTranslation(currentLocale())?.shortDescription)
    }
}
