package com.s95ammar.budgetplanner.models.datasource.remote.api

import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.ConversionDto
import com.s95ammar.budgetplanner.models.datasource.remote.api.dto.CurrenciesListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("convert")
    suspend fun getConversionRate(
        @Query("from") from: String,
        @Query("to") to: String = "",
        @Query("amount") amount: Double = 1.0
    ): ConversionDto

    @GET("convert")
    suspend fun getConversionRates(
        @Query("from") from: String
    ): ConversionDto

    @GET("list")
    suspend fun getCurrenciesList(): CurrenciesListDto

}
