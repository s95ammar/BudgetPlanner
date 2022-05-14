package com.s95ammar.budgetplanner.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <T> flowOf(
    crossinline block: suspend () -> T
): Flow<T> = flow {
    emit(block())
}
