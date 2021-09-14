package com.cyrillrx.rpg.group

import com.cyrillrx.rpg.models.Character
import com.cyrillrx.rpg.template.ListActivity

/**
 * @author Cyril Leroux
 *         Created on 14/02/2020.
 */
class CharacterListActivity : ListActivity<CharacterAdapter>() {

    override val adapter = CharacterAdapter()

    override fun sendRequest() {

        startLoading()

        val characters = listOf(
            Character("Bob"),
            Character("Lee"),
            Character("Joe")
        )

        adapter.addAll(characters)

        stopLoading()
    }
}