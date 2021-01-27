package com.s95ammar.budgetplanner.models.api

import com.s95ammar.budgetplanner.BuildConfig


object BudgetPlannerApiConfig {
    const val BASE_URL = BuildConfig.BASE_URL
    const val TOKEN_HEADER_KEY = "Authorization"
    const val TOKEN_HEADER_VALUE_PREFIX = "Bearer "
    val NO_TOKEN_PATHS = listOf("/auth/register", "/auth/login")
    const val TIMEOUT_SECONDS = 30L
}