package com.cyrillrx.rpg.character.presentation.component

import com.cyrillrx.rpg.character.domain.Character
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ClassIconStateTest {

    @Test
    fun `UNKNOWN class returns Unknown state without invoking loader`() = runTest {
        val result = resolveClassIconState(Character.Class.UNKNOWN) { error("loader must not be called") }
        assertEquals(ClassIconState.Unknown, result)
    }

    @Test
    fun `successful load returns Loaded state with the loaded bytes`() = runTest {
        val bytes = byteArrayOf(1, 2, 3)
        val result = resolveClassIconState(Character.Class.FIGHTER) { bytes }
        assertIs<ClassIconState.Loaded>(result)
        assertContentEquals(bytes, result.bytes)
    }

    @Test
    fun `failed load returns Error state`() = runTest {
        val result = resolveClassIconState(Character.Class.FIGHTER) { throw Exception("file not found") }
        assertEquals(ClassIconState.Error, result)
    }
}
