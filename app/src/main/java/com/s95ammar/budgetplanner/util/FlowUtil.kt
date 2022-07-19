package com.s95ammar.budgetplanner.util

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

inline fun <T> flowOf(
    crossinline block: suspend () -> T
): Flow<T> = flow {
    emit(block())
}

fun <T, S> MutableStateFlow<T>.addSource(
    source: Flow<S>,
    coroutineScope: CoroutineScope,
    onSourceEmission: MutableStateFlow<T>.(S) -> Unit
) = apply {
    coroutineScope.launch {
        source.collect { onSourceEmission(it) }
    }
}
