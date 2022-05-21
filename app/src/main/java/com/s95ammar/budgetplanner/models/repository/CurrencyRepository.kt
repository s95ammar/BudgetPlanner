package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.util.CurrencyRatesMap
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

    fun getMainCurrencyCode() = localDataSource.getMainCurrencyCode()

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

    // TODO (temp)
    fun loadConversionRate(fromCode: String, toCode: String): Flow<Double> {
        return flowOf(29.25).onStart { delay(2000) }.flowOn(Dispatchers.IO)
    }

    // TODO (temp)
    fun loadConversionRates(fromCode: String): Flow<CurrencyRatesMap> {
        return flowOf(
            mapOf(
                "USD" to 10.0,
                "EUR" to 20.0,
                "GBP" to 30.0,
                "CAD" to 40.0,
                "AUD" to 50.0,
                "UAH" to 60.0,
                "CZK" to 70.0,
                "NZD" to 80.0
            )
        ).onStart { delay(2000) }.flowOn(Dispatchers.IO)
    }
}
