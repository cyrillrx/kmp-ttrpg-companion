package com.cyrillrx.rpg.xml.bestiary

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.bestiary.Abilities

class AbilitiesView : ConstraintLayout {

    private var tvStrValue: TextView
    private var tvDexValue: TextView
    private var tvConValue: TextView
    private var tvIntValue: TextView
    private var tvWisValue: TextView
    private var tvChaValue: TextView

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

        inflate(context, R.layout.layout_abilities, this)

        findViewById<TextView>(R.id.tvStrLabel).text = "Str"
        findViewById<TextView>(R.id.tvDexLabel).text = "Dex"
        findViewById<TextView>(R.id.tvConLabel).text = "Con"
        findViewById<TextView>(R.id.tvIntLabel).text = "Int"
        findViewById<TextView>(R.id.tvWisLabel).text = "Wis"
        findViewById<TextView>(R.id.tvChaLabel).text = "Cha"

        tvStrValue = findViewById(R.id.tvStrValue)
        tvDexValue = findViewById(R.id.tvDexValue)
        tvConValue = findViewById(R.id.tvConValue)
        tvIntValue = findViewById(R.id.tvIntValue)
        tvWisValue = findViewById(R.id.tvWisValue)
        tvChaValue = findViewById(R.id.tvChaValue)
    }

    fun setAbilities(abilities: Abilities) {
        setStrength(abilities.str.value)
        setDexterity(abilities.dex.value)
        setConstitution(abilities.con.value)
        setIntelligence(abilities.int.value)
        setWisdom(abilities.wis.value)
        setCharisma(abilities.cha.value)
    }

    fun setStrength(value: String) {
        tvStrValue.text = value
    }

    fun setStrength(value: Int) {
        setStrength(value.addModifier())
    }

    fun setDexterity(value: String) {
        tvDexValue.text = value
    }

    fun setDexterity(value: Int) {
        setDexterity(value.addModifier())
    }

    fun setConstitution(value: String) {
        tvConValue.text = value
    }

    fun setConstitution(value: Int) {
        setConstitution(value.addModifier())
    }

    fun setIntelligence(value: String) {
        tvIntValue.text = value
    }

    fun setIntelligence(value: Int) {
        setIntelligence(value.addModifier())
    }

    fun setWisdom(value: String) {
        tvWisValue.text = value
    }

    fun setWisdom(value: Int) {
        setWisdom(value.addModifier())
    }

    fun setCharisma(value: String) {
        tvChaValue.text = value
    }

    fun setCharisma(value: Int) {
        setCharisma(value.addModifier())
    }

    private fun Int.addModifier(): String {
        val value = this
        val modifier = getModifier(value)
        val signedModifier = if (modifier >= 0) "+$modifier" else "$modifier"
        return "$value ($signedModifier)"
    }

    private fun getModifier(abilityValue: Int): Int = when {
        abilityValue < 4 -> -4
        abilityValue < 6 -> -3
        abilityValue < 8 -> -2
        abilityValue < 10 -> -1
        abilityValue < 12 -> 0
        abilityValue < 14 -> 1
        abilityValue < 16 -> 2
        abilityValue < 18 -> 3
        abilityValue < 20 -> 4
        else -> 5
    }
}
