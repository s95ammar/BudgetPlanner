package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val localDataSource: LocalDataSource
) {

    fun setMainCurrencyCode(code: String) = flowOf {
        localDataSource.setMainCurrencyCode(code)
    }.flowOn(Dispatchers.IO)

    fun getMainCurrencyFlow() = localDataSource.getMainCurrencyFlow()
        .flowOn(Dispatchers.IO)
}