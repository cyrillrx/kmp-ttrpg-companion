package com.cyrillrx.rpg.xml.bestiary

import com.cyrillrx.rpg.AssetReader
import com.cyrillrx.rpg.api.bestiary.ApiBestiaryItem
import com.cyrillrx.rpg.xml.template.ListActivity
import com.cyrillrx.utils.deserialize

class BestiaryLegacyActivity : ListActivity<BestiaryAdapter>() {

    override val adapter = BestiaryAdapter()

    override fun sendRequest() {
        startLoading()

        val serializedBestiary = AssetReader.readAsString(this, "bestiaire.json")
        val bestiary: List<ApiBestiaryItem> = serializedBestiary?.deserialize() ?: listOf()

        adapter.addAll(bestiary)

        stopLoading()
    }
}
