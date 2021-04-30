package com.s95ammar.budgetplanner.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

inline fun <T> flowOnDispatcher(
    dispatcher: CoroutineDispatcher,
    crossinline block: suspend () -> T
): Flow<T> {
    return flow<T> { block() }.flowOn(dispatcher)
}
