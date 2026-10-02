package com.cyrillrx.rpg.core.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/**
 * A scope detached from `viewModelScope`, which `onCleared()` cancels: a write started as the user
 * leaves the screen must still reach the repository.
 */
fun detachedCommitScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
