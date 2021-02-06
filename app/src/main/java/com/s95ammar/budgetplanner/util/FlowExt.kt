package com.s95ammar.budgetplanner.util

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

fun<T> flowOnIo(flowBlock: () -> Flow<T>) = flowBlock().flowOn(Dispatchers.IO)