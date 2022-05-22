package com.s95ammar.budgetplanner.models.datasource.remote.api

import com.s95ammar.budgetplanner.BuildConfig

object ApiConfig {
    const val BASE_URL = "https://currency-converter5.p.rapidapi.com/currency/"
    val HEADER_HOST = Header("x-rapidapi-host", "currency-converter5.p.rapidapi.com")
    val HEADER_KEY = Header("x-rapidapi-key", BuildConfig.CURRENCY_API_KEY)

    const val TIMEOUT_SECONDS = 30L
}