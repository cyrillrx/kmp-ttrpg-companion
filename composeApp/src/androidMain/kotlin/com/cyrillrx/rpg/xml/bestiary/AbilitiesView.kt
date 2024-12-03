package com.cyrillrx.rpg.xml.bestiary

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.cyrillrx.rpg.R
import com.cyrillrx.rpg.models.bestiary.Abilities
import com.cyrillrx.rpg.models.bestiary.Ability

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
        setStrength(abilities.str)
        setDexterity(abilities.dex)
        setConstitution(abilities.con)
        setIntelligence(abilities.int)
        setWisdom(abilities.wis)
        setCharisma(abilities.cha)
    }

    private fun setStrength(ability: Ability) {
        tvStrValue.text = ability.getValueWithModifier()
    }

    private fun setDexterity(ability: Ability) {
        tvDexValue.text = ability.getValueWithModifier()
    }

    private fun setConstitution(ability: Ability) {
        tvConValue.text = ability.getValueWithModifier()
    }

    private fun setIntelligence(ability: Ability) {
        tvIntValue.text = ability.getValueWithModifier()
    }

    private fun setWisdom(ability: Ability) {
        tvWisValue.text = ability.getValueWithModifier()
    }

    private fun setCharisma(ability: Ability) {
        tvChaValue.text = ability.getValueWithModifier()
    }
}
