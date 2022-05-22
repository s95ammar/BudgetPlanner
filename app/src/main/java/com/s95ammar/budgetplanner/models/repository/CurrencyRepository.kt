package com.s95ammar.budgetplanner.models.repository

import com.s95ammar.budgetplanner.models.datasource.local.LocalDataSource
import com.s95ammar.budgetplanner.models.datasource.local.db.entity.CurrencyEntity
import com.s95ammar.budgetplanner.models.datasource.remote.RemoteDataSource
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.ConversionDto
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.CurrenciesListDto
import com.s95ammar.budgetplanner.util.flowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurrencyRepository @Inject constructor(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) {

    fun setMainCurrencyCode(code: String) = flowOf {
        localDataSource.setMainCurrencyCode(code)
    }.flowOn(Dispatchers.IO)

    fun getMainCurrencyCode() = localDataSource.getMainCurrencyCode()

    fun getMainCurrencyFlow() = localDataSource.getMainCurrencyFlow()
        .flowOn(Dispatchers.IO)

    fun getDbCurrenciesFlow() = localDataSource.getAllCurrenciesFlow()
        .flowOn(Dispatchers.IO)

    fun insertCurrencyIfDoesNotExist(currencyEntity: CurrencyEntity) = flowOf {
        localDataSource.insertCurrencyIfDoesNotExist(currencyEntity)
    }.flowOn(Dispatchers.IO)

    fun loadCurrenciesList(): Flow<CurrenciesListDto> {
        return flowOf {
            remoteDataSource.getCurrenciesList()
        }.flowOn(Dispatchers.IO)
    }

    fun loadConversionRates(fromCode: String): Flow<ConversionDto> {
        return flowOf {
            remoteDataSource.getConversionRates(fromCode)
        }.flowOn(Dispatchers.IO)
    }
}
