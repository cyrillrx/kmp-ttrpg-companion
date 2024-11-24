package com.cyrillrx.rpg.xml.group

import com.cyrillrx.rpg.models.Character
import com.cyrillrx.rpg.template.ListActivity

class CharacterListActivity : ListActivity<CharacterAdapter>() {

    override val adapter = CharacterAdapter()

    override fun sendRequest() {

        startLoading()

        val characters = listOf(
            Character("Bob"),
            Character("Lee"),
            Character("Joe"),
        )

        adapter.addAll(characters)

        stopLoading()
    }
}
