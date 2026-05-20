package com.cyrillrx.rpg.character.presentation.viewmodel

import com.cyrillrx.rpg.character.data.RamCharacterRepository
import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.character.domain.Language
import com.cyrillrx.rpg.character.domain.Race
import com.cyrillrx.rpg.character.presentation.CharacterEditState
import com.cyrillrx.rpg.character.presentation.CharacterEditState.Loaded.EditingField
import com.cyrillrx.rpg.creature.domain.Creature
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.launch
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

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
    ) = CharacterEditViewModel(characterId, NoOpCharacterRouter(), repo)

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
        assertEquals(fighter.name, loaded.name)
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
    fun `saveName with blank input cancels editing without updating name`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.editField(EditingField.Race)
        viewModel.saveName("   ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(fighter.name, loaded.name)
        assertNull(loaded.editingField)
    }

    @Test
    fun `saveName trims whitespace`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveName("  Aria  ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("Aria", loaded.name)
    }

    // ─── saveBackground ────────────────────────────────────────────────────────

    @Test
    fun `saveBackground trims whitespace`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveBackground("  Soldier  ")
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals("Soldier", loaded.background)
    }

    // ─── Numeric field coercion ─────────────────────────────────────────────────

    @Test
    fun `saveLevel coerces to minimum 1`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveLevel(0)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(1, loaded.level)
    }

    @Test
    fun `saveStrength coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveStrength(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).strength)
        viewModel.saveStrength(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).strength)
    }

    @Test
    fun `saveDexterity coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveDexterity(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).dexterity)
        viewModel.saveDexterity(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).dexterity)
    }

    @Test
    fun `saveConstitution coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveConstitution(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).constitution)
        viewModel.saveConstitution(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).constitution)
    }

    @Test
    fun `saveIntelligence coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveIntelligence(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).intelligence)
        viewModel.saveIntelligence(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).intelligence)
    }

    @Test
    fun `saveWisdom coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveWisdom(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).wisdom)
        viewModel.saveWisdom(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).wisdom)
    }

    @Test
    fun `saveCharisma coerces to range 1-30`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveCharisma(0)
        assertEquals(1, assertIs<CharacterEditState.Loaded>(viewModel.state.value).charisma)
        viewModel.saveCharisma(31)
        assertEquals(30, assertIs<CharacterEditState.Loaded>(viewModel.state.value).charisma)
    }

    @Test
    fun `saveArmorClass coerces to minimum 0`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveArmorClass(-1)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(0, loaded.armorClass)
    }

    @Test
    fun `saveMaxHitPoints coerces to minimum 1`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveMaxHitPoints(0)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(1, loaded.maxHitPoints)
    }

    // ─── coercedValueEvent ────────────────────────────────────────────────────

    @Test
    fun `coercedValueEvent emits coerced value when input is out of range`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val events = mutableListOf<Int>()
        val job = launch { viewModel.coercedValueEvent.collect { events.add(it) } }
        advanceUntilIdle()
        viewModel.saveLevel(25)  // max is 20, coerced to 20
        advanceUntilIdle()
        assertEquals(listOf(20), events)
        job.cancel()
    }

    @Test
    fun `coercedValueEvent does not emit when input is already valid`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        val events = mutableListOf<Int>()
        val job = launch { viewModel.coercedValueEvent.collect { events.add(it) } }
        advanceUntilIdle()
        viewModel.saveLevel(5)  // valid, no coercion
        advanceUntilIdle()
        assertEquals(emptyList<Int>(), events)
        job.cancel()
    }

    // ─── saveRace ─────────────────────────────────────────────────────────────

    @Test
    fun `saveRace resets walk speed to race default`() = runTest(testDispatcher) {
        val viewModel = buildViewModel(repo = repoWithFighter())
        advanceUntilIdle()
        viewModel.saveRace(Race.DWARF)
        val loaded = assertIs<CharacterEditState.Loaded>(viewModel.state.value)
        assertEquals(25, loaded.walkSpeed)
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
}
