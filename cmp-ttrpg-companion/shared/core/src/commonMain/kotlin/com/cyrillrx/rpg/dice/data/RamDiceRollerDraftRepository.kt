package com.cyrillrx.rpg.dice.data

import com.cyrillrx.rpg.dice.domain.DiceRollerDraft
import com.cyrillrx.rpg.dice.domain.DiceRollerDraftRepository

class RamDiceRollerDraftRepository(private var draft: DiceRollerDraft = DiceRollerDraft()) :
    DiceRollerDraftRepository {

    override suspend fun load(): DiceRollerDraft = draft

    override suspend fun save(draft: DiceRollerDraft) {
        this.draft = draft
    }
}
