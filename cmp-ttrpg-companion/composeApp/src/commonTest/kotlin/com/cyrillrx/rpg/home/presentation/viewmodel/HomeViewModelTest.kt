package com.cyrillrx.rpg.home.presentation.viewmodel

import com.cyrillrx.rpg.character.data.SampleCharacterRepository
import com.cyrillrx.rpg.character.domain.Character
import com.cyrillrx.rpg.character.domain.CharacterFilter
import com.cyrillrx.rpg.character.domain.CharacterRepository
import com.cyrillrx.rpg.core.domain.Stored
import com.cyrillrx.rpg.home.presentation.HomeState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `the home screen keeps the two most recently updated sheets`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel(DatedCharacterRepository())

        advanceUntilIdle()

        val body = assertIs<HomeState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = listOf("Newest", "Middle"), actual = body.characters.map { it.value.name })
    }

    @Test
    fun `sheets sharing a date are kept in id order`() = runTest(testDispatcher) {
        val viewModel = HomeViewModel(TiedCharacterRepository())

        advanceUntilIdle()

        val body = assertIs<HomeState.Body.WithData>(viewModel.state.value.body)
        assertEquals(expected = listOf("a", "b"), actual = body.characters.map { it.value.id })
    }
}

/** Returns sheets written at the same instant, so only the caller's tiebreaker shows. */
private class TiedCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Stored<Character>> = listOf(
        stored("c"),
        stored("b"),
        stored("a"),
    )

    override suspend fun get(id: String): Character? = null
    override suspend fun getByIds(ids: List<String>): List<Character> = emptyList()
    override suspend fun save(character: Character) = Unit
    override suspend fun delete(id: String) = Unit

    private fun stored(id: String) = Stored(
        value = SampleCharacterRepository.humanFighter().copy(id = id, name = "Same"),
        updatedAt = Instant.fromEpochMilliseconds(1_000L),
    )
}

/** Returns sheets whose timestamps disagree with their position, so only the caller's ordering shows. */
private class DatedCharacterRepository : CharacterRepository {
    override suspend fun getAll(filter: CharacterFilter?): List<Stored<Character>> = listOf(
        stored("Middle", 2_000L),
        stored("Oldest", 1_000L),
        stored("Newest", 3_000L),
    )

    override suspend fun get(id: String): Character? = null
    override suspend fun getByIds(ids: List<String>): List<Character> = emptyList()
    override suspend fun save(character: Character) = Unit
    override suspend fun delete(id: String) = Unit

    private fun stored(name: String, epochMillis: Long) = Stored(
        value = SampleCharacterRepository.humanFighter().copy(id = name, name = name),
        updatedAt = Instant.fromEpochMilliseconds(epochMillis),
    )
}
