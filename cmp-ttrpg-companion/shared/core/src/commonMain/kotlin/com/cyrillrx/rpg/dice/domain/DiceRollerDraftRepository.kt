package com.cyrillrx.rpg.dice.domain

interface DiceRollerDraftRepository {
    /** The last saved draft, or an empty one when none was saved yet. */
    suspend fun load(): DiceRollerDraft

    suspend fun save(draft: DiceRollerDraft)
}
