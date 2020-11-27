package com.s95ammar.budgetplanner.models.api

object BudgetPlannerApiConfig {
    const val BASE_URL = "http://192.168.1.143:8081"
    const val TOKEN_HEADER_KEY = "Authorization"
    const val TOKEN_HEADER_VALUE_PREFIX = "Bearer "
    val NO_TOKEN_PATHS = listOf("/auth/register", "/auth/login")
    const val TIMEOUT_SECONDS = 30L
}