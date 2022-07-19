package com.s95ammar.budgetplanner.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

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

@Composable
fun <T> Flow<T>.collectAsStateSafe(
    initial: T,
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    val lifecycleOwner = LocalLifecycleOwner.current
    val flowLifecycleAware = remember(this, lifecycleOwner) {
        flowWithLifecycle(lifecycleOwner.lifecycle, Lifecycle.State.STARTED)
    }

    return flowLifecycleAware.collectAsState(initial, context)
}

@Composable
fun <T> Flow<T>.collectAsStateSafe(
    context: CoroutineContext = EmptyCoroutineContext
): State<T?> {
    return collectAsStateSafe(null, context)
}

@Suppress("StateFlowValueCalledInComposition")
@Composable
fun <T> StateFlow<T>.collectAsStateSafe(
    context: CoroutineContext = EmptyCoroutineContext
): State<T> {
    return collectAsStateSafe(value, context)
}
