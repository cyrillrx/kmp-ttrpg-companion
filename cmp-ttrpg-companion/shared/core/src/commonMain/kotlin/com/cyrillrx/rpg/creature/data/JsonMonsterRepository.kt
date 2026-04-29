package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.Abilities
import com.cyrillrx.rpg.creature.domain.Ability
import com.cyrillrx.rpg.creature.domain.ConditionImmunities
import com.cyrillrx.rpg.creature.domain.Creature
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.DamageAffinity
import com.cyrillrx.rpg.creature.domain.DamageAffinities
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.Proficiency
import com.cyrillrx.rpg.creature.domain.Skills
import com.cyrillrx.rpg.creature.domain.Speeds
import com.cyrillrx.rpg.creature.domain.applyFilter

class JsonMonsterRepository(private val fileReader: FileReader) : MonsterRepository {

    private var cache: List<Monster>? = null

    override suspend fun getAll(filter: MonsterFilter?): List<Monster> {
        val all = cache ?: loadFromFile().parse().also { cache = it }
        return all.applyFilter(filter)
    }

    override suspend fun getById(id: String): Monster? =
        getAll(null).firstOrNull { it.id == id }

    override suspend fun getByIds(ids: List<String>): List<Monster> {
        val all = getAll(null).associateBy { it.id }
        return ids.mapNotNull { all[it] }
    }

    private suspend fun loadFromFile(): List<ApiMonster> {
        val result = fileReader.readFile("files/monsters.json")
        if (result is Result.Success) {
            return result.value.deserialize() ?: listOf()
        }
        return listOf()
    }

    companion object {
        private fun List<ApiMonster>.parse(): List<Monster> {
            val (monsters, errors) = partitionBy { it.toMonster() }
            errors.forEach { println("WARNING: monster import error: $it") }
            return monsters
        }

        private fun ApiMonster.toMonster(): Result<Monster, MonsterImportError> {
            val id = id
                ?: return Result.Failure(MonsterImportError.MissingId)
            val source = source
                ?: return Result.Failure(MonsterImportError.MissingSource(id))
            val apiType = type
                ?: return Result.Failure(MonsterImportError.MissingType(id))
            val type = apiType.toType()
                ?: return Result.Failure(MonsterImportError.UnknownType(id, apiType))
            val apiSize = size
                ?: return Result.Failure(MonsterImportError.MissingSize(id))
            val size = apiSize.toSize()
                ?: return Result.Failure(MonsterImportError.UnknownSize(id, apiSize))
            val apiAlignment = alignment
                ?: return Result.Failure(MonsterImportError.MissingAlignment(id))
            val alignment = apiAlignment.toAlignment()
                ?: return Result.Failure(MonsterImportError.UnknownAlignment(id, apiAlignment))
            val apiAbilities = abilities
                ?: return Result.Failure(MonsterImportError.MissingAbilities(id))
            val apiTranslations = translations
                ?: return Result.Failure(MonsterImportError.MissingTranslations(id))
            val (translationMap, translationErrors) = apiTranslations.partitionBy { locale, t ->
                t.toDomain(id, locale)
            }
            translationErrors.forEach { println("WARNING: monster import error: $it") }
            val translations = translationMap.takeIf { it.isNotEmpty() }
                ?: return Result.Failure(MonsterImportError.MissingTranslations(id))

            val enTranslation = translations["en"] ?: translations.values.first()

            return Result.Success(
                Monster(
                    id = id,
                    source = source,
                    type = type,
                    size = size,
                    alignment = alignment,
                    challengeRating = challengeRating ?: -1f,
                    hitDice = hitDice.orEmpty(),
                    abilities = apiAbilities.toDomain(),
                    armorClass = armorClass ?: 10,
                    maxHitPoints = maxHitPoints ?: 0,
                    speeds = speeds.toDomain(),
                    languages = enTranslation.languages ?: emptyList(),
                    skills = skills.toSkills(),
                    damageAffinities = damageAffinities.toDamageAffinities(),
                    conditionImmunities = conditionImmunities.toConditionImmunities(),
                    translations = translations,
                ),
            )
        }

        private fun ApiMonster.ApiSpeeds?.toDomain(): Speeds = Speeds(
            walk = this?.walk,
            fly = this?.fly,
            swim = this?.swim,
            climb = this?.climb,
            burrow = this?.burrow,
            hover = this?.hover ?: false,
        )

        private fun ApiMonster.Translation.toDomain(
            monsterId: String,
            locale: String,
        ): Result<Monster.Translation, MonsterImportError> {
            val name = name
                ?: return Result.Failure(MonsterImportError.InvalidTranslation(monsterId, locale))
            val description = description
                ?: return Result.Failure(MonsterImportError.InvalidTranslation(monsterId, locale))
            val senses = senses
                ?: return Result.Failure(MonsterImportError.InvalidTranslation(monsterId, locale))
            return Result.Success(
                Monster.Translation(
                    name = name,
                    subtype = subtype,
                    description = description,
                    senses = senses,
                    languages = languages ?: emptyList(),
                ),
            )
        }

        private fun ApiMonster.ApiAbilities.toDomain(): Abilities =
            Abilities(
                str = Ability(str?.value ?: Ability.DEFAULT_VALUE, str?.savingThrowProficiency.toProficiency()),
                dex = Ability(dex?.value ?: Ability.DEFAULT_VALUE, dex?.savingThrowProficiency.toProficiency()),
                con = Ability(con?.value ?: Ability.DEFAULT_VALUE, con?.savingThrowProficiency.toProficiency()),
                int = Ability(int?.value ?: Ability.DEFAULT_VALUE, int?.savingThrowProficiency.toProficiency()),
                wis = Ability(wis?.value ?: Ability.DEFAULT_VALUE, wis?.savingThrowProficiency.toProficiency()),
                cha = Ability(cha?.value ?: Ability.DEFAULT_VALUE, cha?.savingThrowProficiency.toProficiency()),
            )

        private fun String?.toProficiency(): Proficiency = when (this) {
            "proficient" -> Proficiency.PROFICIENT
            "expert" -> Proficiency.EXPERT
            else -> Proficiency.NONE
        }

        private fun Map<String, String>?.toSkills(): Skills {
            if (this == null) return Skills()
            return Skills(
                acrobatics = get("acrobatics").toProficiency(),
                animalHandling = get("animalHandling").toProficiency(),
                arcana = get("arcana").toProficiency(),
                athletics = get("athletics").toProficiency(),
                deception = get("deception").toProficiency(),
                history = get("history").toProficiency(),
                insight = get("insight").toProficiency(),
                intimidation = get("intimidation").toProficiency(),
                investigation = get("investigation").toProficiency(),
                medicine = get("medicine").toProficiency(),
                nature = get("nature").toProficiency(),
                perception = get("perception").toProficiency(),
                performance = get("performance").toProficiency(),
                persuasion = get("persuasion").toProficiency(),
                religion = get("religion").toProficiency(),
                sleightOfHand = get("sleightOfHand").toProficiency(),
                stealth = get("stealth").toProficiency(),
                survival = get("survival").toProficiency(),
            )
        }

        private fun Map<String, String>?.toDamageAffinities(): DamageAffinities {
            if (this == null) return DamageAffinities()
            return DamageAffinities(
                acid = get("acid").toDamageAffinity(),
                bludgeoning = get("bludgeoning").toDamageAffinity(),
                cold = get("cold").toDamageAffinity(),
                fire = get("fire").toDamageAffinity(),
                force = get("force").toDamageAffinity(),
                lightning = get("lightning").toDamageAffinity(),
                necrotic = get("necrotic").toDamageAffinity(),
                piercing = get("piercing").toDamageAffinity(),
                poison = get("poison").toDamageAffinity(),
                psychic = get("psychic").toDamageAffinity(),
                radiant = get("radiant").toDamageAffinity(),
                slashing = get("slashing").toDamageAffinity(),
                thunder = get("thunder").toDamageAffinity(),
                bludgeoningNonMagical = get("bludgeoningNonMagical").toDamageAffinity(),
                piercingNonMagical = get("piercingNonMagical").toDamageAffinity(),
                slashingNonMagical = get("slashingNonMagical").toDamageAffinity(),
            )
        }

        private fun String?.toDamageAffinity(): DamageAffinity = when (this) {
            "resistant" -> DamageAffinity.RESISTANT
            "immune" -> DamageAffinity.IMMUNE
            "vulnerable" -> DamageAffinity.VULNERABLE
            else -> DamageAffinity.NONE
        }

        private fun Map<String, Boolean>?.toConditionImmunities(): ConditionImmunities {
            if (this == null) return ConditionImmunities()
            return ConditionImmunities(
                blinded = get("blinded") ?: false,
                charmed = get("charmed") ?: false,
                deafened = get("deafened") ?: false,
                exhaustion = get("exhaustion") ?: false,
                frightened = get("frightened") ?: false,
                grappled = get("grappled") ?: false,
                incapacitated = get("incapacitated") ?: false,
                paralyzed = get("paralyzed") ?: false,
                petrified = get("petrified") ?: false,
                poisoned = get("poisoned") ?: false,
                prone = get("prone") ?: false,
                restrained = get("restrained") ?: false,
                stunned = get("stunned") ?: false,
                unconscious = get("unconscious") ?: false,
            )
        }

        private fun String.toType(): Monster.Type? =
            Monster.Type.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toSize(): Creature.Size? =
            Creature.Size.entries.find { it.name.equals(this, ignoreCase = true) }

        private fun String.toAlignment(): Creature.Alignment? =
            Creature.Alignment.entries.find { it.name.equals(this, ignoreCase = true) }
    }
}
