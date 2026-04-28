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
