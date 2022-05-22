package com.s95ammar.budgetplanner.models.datasource.remote

import com.s95ammar.budgetplanner.models.datasource.remote.api.ApiService
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.ConversionDto
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.CurrenciesListDto
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RemoteDataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : RemoteDataSource {

    override suspend fun getConversionRate(
        from: String,
        to: String,
        amount: Double
    ): ConversionDto {
        return apiService.getConversionRate(from, to, amount)
    }

    override suspend fun getConversionRates(from: String): ConversionDto {
        return apiService.getConversionRates(from)
    }

    override suspend fun getCurrenciesList(): CurrenciesListDto {
        return apiService.getCurrenciesList()
    }

}
