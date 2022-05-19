package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
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

    fun getDbCurrenciesFlow() = localDataSource.getAllCurrenciesFlow()
        .flowOn(Dispatchers.IO)

    // TODO (temp)
    fun loadCurrenciesList(): Flow<List<CurrencyEntity>> {
        return flowOf(
            listOf(
                CurrencyEntity("CAD", "Canadian dollar"),
                CurrencyEntity("AUD", "Australian dollar"),
                CurrencyEntity("UAH", "Ukrainian hryvnia"),
                CurrencyEntity("CZK", "Czech koruna"),
                CurrencyEntity("NZD", "New Zealand dollar")
            )
        ).onStart { delay(2000) }.flowOn(Dispatchers.IO)
    }

    fun insertCurrency(currencyEntity: CurrencyEntity) = flowOf {
        localDataSource.insertCurrency(currencyEntity)
    }.flowOn(Dispatchers.IO)
}
