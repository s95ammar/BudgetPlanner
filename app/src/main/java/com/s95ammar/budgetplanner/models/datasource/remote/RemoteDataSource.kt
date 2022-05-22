package com.s95ammar.budgetplanner.models.datasource.remote

import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.ConversionDto
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.CurrenciesListDto

interface RemoteDataSource {
    suspend fun getConversionRate(
        from: String,
        to: String = "",
        amount: Double = 1.0
    ): ConversionDto

    suspend fun getConversionRates(from: String): ConversionDto

    suspend fun getCurrenciesList(): CurrenciesListDto
}
