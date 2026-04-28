package com.cyrillrx.core.domain

sealed interface Result<out Data, out E : Error> {
    data class Success<out Data>(val value: Data) : Result<Data, Nothing>
    data class Failure<out E : Error>(val error: E) : Result<Nothing, E>
}

fun <T, Data, E : Error> List<T>.partitionBy(
    transform: (T) -> Result<Data, E>,
): Pair<List<Data>, List<E>> {
    val successes = mutableListOf<Data>()
    val failures = mutableListOf<E>()
    for (item in this) {
        when (val result = transform(item)) {
            is Result.Success -> successes.add(result.value)
            is Result.Failure -> failures.add(result.error)
        }
    }
    return successes to failures
}

fun <Data, E : Error> List<Result<Data, E>>.partition(): Pair<List<Data>, List<E>> = partitionBy { it }

fun <K, V, NewV, E : Error> Map<K, V>.partitionBy(
    transform: (K, V) -> Result<NewV, E>,
): Pair<Map<K, NewV>, List<E>> {
    val successes = mutableMapOf<K, NewV>()
    val failures = mutableListOf<E>()
    for ((key, value) in this) {
        when (val result = transform(key, value)) {
            is Result.Success -> successes[key] = result.value
            is Result.Failure -> failures.add(result.error)
        }
    }
    return successes to failures
}
