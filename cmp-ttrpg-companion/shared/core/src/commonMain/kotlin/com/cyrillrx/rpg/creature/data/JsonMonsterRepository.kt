package com.cyrillrx.rpg.creature.data

import com.cyrillrx.core.data.FileReader
import com.cyrillrx.core.data.LazyCache
import com.cyrillrx.core.data.deserialize
import com.cyrillrx.core.domain.Result
import com.cyrillrx.core.domain.partitionBy
import com.cyrillrx.rpg.creature.data.api.ApiMonster
import com.cyrillrx.rpg.creature.domain.Monster
import com.cyrillrx.rpg.creature.domain.MonsterFilter
import com.cyrillrx.rpg.creature.domain.MonsterRepository
import com.cyrillrx.rpg.creature.domain.applyFilter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

class JsonMonsterRepository(
    private val fileReader: FileReader,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) : MonsterRepository {

    private val cache = LazyCache { loadFromFile().parse() }

    override suspend fun getAll(filter: MonsterFilter?): List<Monster> = withContext(ioDispatcher) {
        cache.get().applyFilter(filter)
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
            val types = apiType.toTypes()
                ?: return Result.Failure(MonsterImportError.UnknownType(id, apiType))
            val apiSize = size
                ?: return Result.Failure(MonsterImportError.MissingSize(id))
            val size = apiSize.toSize()
                ?: return Result.Failure(MonsterImportError.UnknownSize(id, apiSize))
            val apiAlignment = alignment
                ?: return Result.Failure(MonsterImportError.MissingAlignment(id))
            val alignment = apiAlignment.toAlignment()
                ?: return Result.Failure(MonsterImportError.UnknownAlignment(id, apiAlignment))
            val challengeRating = challengeRating
                ?: return Result.Failure(MonsterImportError.MissingChallengeRating(id))
            val apiAbilities = abilities
                ?: return Result.Failure(MonsterImportError.MissingAbilities(id))
            val armorClass = armorClass
                ?: return Result.Failure(MonsterImportError.MissingArmorClass(id))
            val maxHitPoints = maxHitPoints
                ?: return Result.Failure(MonsterImportError.MissingMaxHitPoints(id))
            val apiSkills = skills
                ?: return Result.Failure(MonsterImportError.MissingSkills(id))
            val apiDamageAffinities = damageAffinities
                ?: return Result.Failure(MonsterImportError.MissingDamageAffinities(id))
            val apiConditionImmunities = conditionImmunities
                ?: return Result.Failure(MonsterImportError.MissingConditionImmunities(id))
            val apiTranslations = translations
                ?: return Result.Failure(MonsterImportError.MissingTranslations(id))
            val translations = apiTranslations.toTranslations(id)
                ?: return Result.Failure(MonsterImportError.MissingTranslations(id))

            return Result.Success(
                Monster(
                    id = id,
                    source = source,
                    types = types,
                    size = size,
                    alignment = alignment,
                    challengeRating = challengeRating,
                    hitDice = hitDice.orEmpty(),
                    abilities = createAbilities(apiAbilities, savingThrows),
                    armorClass = armorClass,
                    maxHitPoints = maxHitPoints,
                    speeds = speeds.toSpeeds(),
                    skills = apiSkills.toSkills(),
                    damageAffinities = apiDamageAffinities.toDamageAffinities(),
                    conditionImmunities = apiConditionImmunities.toConditionImmunities(),
                    translations = translations,
                ),
            )
        }

        private fun Map<String, ApiMonster.Translation>.toTranslations(
            monsterId: String,
        ): Map<String, Monster.Translation>? {
            val (parsedTranslations, translationErrors) = partitionBy { locale, t ->
                t.toTranslation(monsterId, locale)
            }
            translationErrors.forEach { println("WARNING: monster import error: $it") }
            return parsedTranslations.takeIf { it.isNotEmpty() }
        }

        private fun ApiMonster.Translation.toTranslation(
            monsterId: String,
            locale: String,
        ): Result<Monster.Translation, MonsterImportError> {
            val name = name
                ?: return Result.Failure(MonsterImportError.InvalidTranslation(monsterId, locale, field = "name"))
            val description = description
                ?: return Result.Failure(
                    MonsterImportError.InvalidTranslation(monsterId, locale, field = "description"),
                )
            val senses = senses
                ?: return Result.Failure(MonsterImportError.InvalidTranslation(monsterId, locale, field = "senses"))

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

        private fun String.toTypes(): Set<Monster.Type>? {
            val monsterType = Monster.Type.valueOfOrNull(type = this)
            if (monsterType != null) return setOf(monsterType)

            if (isTypeSwarm()) return setOf(Monster.Type.SWARM)

            val monsterTypes = split("_or_")
            if (monsterTypes.size <= 1) return null

            return monsterTypes.mapTo(LinkedHashSet()) { type -> Monster.Type.valueOfOrNull(type) ?: return null }
        }

        private fun String.isTypeSwarm(): Boolean =
            equals("swarm", ignoreCase = true) || startsWith("swarm_of", ignoreCase = true)
    }
}
