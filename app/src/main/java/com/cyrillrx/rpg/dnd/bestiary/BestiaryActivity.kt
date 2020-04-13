package com.cyrillrx.rpg.dnd.bestiary

import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.bestiary.BestiaryItem
import com.cyrillrx.rpg.template.ListActivity
import com.cyrillrx.utils.deserialize

/**
 * @author Cyril Leroux
 *         Created on 12/03/2020.
 */
class BestiaryActivity : ListActivity<BestiaryAdapter>() {

    override val adapter = BestiaryAdapter()

    override fun sendRequest() {

        startLoading()

        val serializedBestiary = AssetReader.readAsString(this, "bestiaire.json")
        val bestiary: Bestiary = serializedBestiary?.deserialize() ?: Bestiary()

        adapter.addAll(bestiary)

        stopLoading()
    }

    class Bestiary : ArrayList<BestiaryItem>()
}